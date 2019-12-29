package br.edu.uniredentor.tachegando.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

import br.edu.uniredentor.tachegando.MapasActivity;
import br.edu.uniredentor.tachegando.R;
import br.edu.uniredentor.tachegando.adapter.PassageiroAdapter;
import br.edu.uniredentor.tachegando.model.Denuncia;
import br.edu.uniredentor.tachegando.model.Viagem;
import br.edu.uniredentor.tachegando.utils.ConstantsUtils;
import br.edu.uniredentor.tachegando.utils.FirebaseUtils;
import br.edu.uniredentor.tachegando.utils.GeralUtils;
import br.edu.uniredentor.tachegando.utils.MapaUtils;
import br.edu.uniredentor.tachegando.utils.SharedUtils;
import br.edu.uniredentor.tachegando.viewmodel.ViewModelPassageiro;
import butterknife.BindView;
import butterknife.ButterKnife;
import mehdi.sakout.fancybuttons.FancyButton;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

/**
 * A simple {@link Fragment} subclass.
 */

public class InformacaoOnibusDialogFragment extends DialogFragment {

    private GoogleMap mapa;
    private MarcacaoUpdate marcacaoUpdate;
    private Viagem viagem;

    private double minhaLatitude;
    private double minhaLongitude;

    @BindView(R.id.textView_nome_rota) TextView textViewNomeDaRota;
    @BindView(R.id.textView_quantidade_denuncias) TextView textViewQuantidadeDeDenuncias;
    @BindView(R.id.button_denunciar) FancyButton buttonDenuncia;
    @BindView(R.id.button_entrar_sair) FancyButton buttonEntrarOuSair;
    @BindView(R.id.textView_distancia) TextView textViewDistancia;
    @BindView(R.id.recyclerView_passageiros) RecyclerView recyclerViewPassageiros;
    private PassageiroAdapter adapter;

    public InformacaoOnibusDialogFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewModelPassageiro viewModelPassageiro = ViewModelProviders.of(this).get(ViewModelPassageiro.class);
        LiveData<DocumentSnapshot> liveData = viewModelPassageiro.getDataSnapshotLiveData(viagem.getId());
        liveData.observe(this, dataSnapshot -> {
            if(dataSnapshot != null){
                try {
                    viagem = dataSnapshot.toObject(Viagem.class);
                    adapter.atualiza(viagem.getPassageiros());
                    defineBotaoDeEntrarOuSair();
                }catch (Exception e){
                    dismiss();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_informacao_onibus_dialog, container, false);
        ButterKnife.bind(this, view);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        mostraChat();
        adapter = new PassageiroAdapter(viagem.getPassageiros());
        recyclerViewPassageiros.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewPassageiros.setAdapter(adapter);

        getToolbar(view);
        setTextos();

        buttonEntrarOuSair.setOnClickListener(v -> {

            AlertDialog.Builder alerta = new AlertDialog.Builder(getContext());
            if (viagem.isPassageiro(GeralUtils.getIdDoUsuario())) {
                alerta.setTitle("Ônibus")
                        .setMessage("Deseja sair do onibus?")
                        .setNegativeButton("Não", null)
                        .setPositiveButton("Sim", (dialog, which) -> saiDoOnibus(GeralUtils.getIdDoUsuario()));
                alerta.show();
            } else {
                if (GeralUtils.ehUsuario(getActivity())) {
                    alerta.setTitle(getString(R.string.onibus))
                            .setMessage(getString(R.string.deseja_entrar_no_onibus))
                            .setNegativeButton(getString(R.string.nao), null)
                            .setPositiveButton(getString(R.string.sim), (dialog, which) -> entraOnibus(GeralUtils.getIdDoUsuario()));
                    alerta.show();
                }
            }
        });

        buttonDenuncia.setOnClickListener(v -> {
            AlertDialog.Builder alerta = new AlertDialog.Builder(getContext());
            if (GeralUtils.ehUsuario(getActivity())) {
                alerta.setTitle(getString(R.string.onibus))
                        .setMessage(getString(R.string.denunciar_viagem))
                        .setNegativeButton(getString(R.string.nao), null)
                        .setPositiveButton(getString(R.string.sim), (dialog, which) -> {
                            Denuncia denuncia = new Denuncia();
                            denuncia.setIdDenunciante(GeralUtils.getIdDoUsuario());
                            FirebaseUtils.denuncia(viagem, denuncia);
                            dismiss();
                        });
                alerta.show();
            }
        });

        return view;
    }

    private void defineBotaoDeEntrarOuSair() {
        if (viagem.isPassageiro(GeralUtils.getIdDoUsuario())) {
            buttonEntrarOuSair.setText(getString(R.string.sair));
            buttonEntrarOuSair.setBackgroundColor(getResources().getColor(R.color.amarelo));
        } else {
            buttonEntrarOuSair.setBackgroundColor(getResources().getColor(R.color.verde));
            buttonEntrarOuSair.setText(getString(R.string.entrar));
        }
    }

    private void setTextos() {
        textViewNomeDaRota.setText(viagem.getNome());
        textViewQuantidadeDeDenuncias.setText(viagem.getDenuncias().size() + " " + "denúncias");
        textViewDistancia.setText(getString(R.string.distancia) + " " + defineDistancia() + " m");
        defineBotaoDeEntrarOuSair();
    }

    private String defineDistancia() {
        Location origem = new Location("");
        origem.setLatitude(viagem.getLatitude());
        origem.setLongitude(viagem.getLongitude());

        Location destino = new Location("");
        destino.setLatitude(minhaLatitude);
        destino.setLongitude(minhaLongitude);

        float distancia = origem.distanceTo(destino);
        return String.format("%.1f", distancia);
    }

    private Toolbar getToolbar(final View view) {
        final Toolbar toolbar = view.findViewById(R.id.toolbar_principal);
        toolbar.setTitle(viagem.getNome());

        toolbar.setOnMenuItemClickListener(item -> {

            switch (item.getItemId()) {

                case R.id.item_trajeto:
                    FirebaseUtils.getViagem(viagem.getId()).collection(ConstantsUtils.TRAJETO).addSnapshotListener((queryDocumentSnapshots, e) -> {
                        ArrayList<LatLng> locais = new ArrayList<>();
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                            LatLng latLng = new LatLng((Double) snapshot.get(ConstantsUtils.LATITUDE), (Double) snapshot.get(ConstantsUtils.LONGITUDE));
                            locais.add(latLng);
                        }
                        Polyline polyline = MapaUtils.mostrarTrajeto(mapa, locais);
                        marcacaoUpdate.limpar(polyline);
                        dismiss();

                    });

                    break;
            }
            return true;
        });
        toolbar.inflateMenu(R.menu.menu_informacao_dialog);
        return toolbar;
    }

    private void saiDoOnibus(String id) {
        FirebaseUtils.removePassageiro(viagem, id);
        dismiss();
    }

    private void entraOnibus(String id) {
        FirebaseUtils.adicionaPassageiro(id, viagem);
        SharedUtils.save(viagem.getId(), getActivity());
    }


    private void mostraChat() {
        ChatFragment chat = new ChatFragment();
        chat.setViagem(viagem);
        getChildFragmentManager().beginTransaction().replace(R.id.linearLayout_chat, chat, "chat").commit();
    }

    public InformacaoOnibusDialogFragment setMapa(GoogleMap mapa) {
        this.mapa = mapa;
        return this;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    public InformacaoOnibusDialogFragment setMarcacaoUpdate(MapasActivity activity) {
        this.marcacaoUpdate = activity;
        return this;
    }

    public InformacaoOnibusDialogFragment setViagem(Viagem viagem) {
        this.viagem = viagem;
        return this;

    }

    public InformacaoOnibusDialogFragment setLocalizacao(double latitude, double longitude) {
        this.minhaLatitude = latitude;
        this.minhaLongitude = longitude;
        return this;
    }

    public interface MarcacaoUpdate {
        void limpar(Polyline pontos);
    }
}

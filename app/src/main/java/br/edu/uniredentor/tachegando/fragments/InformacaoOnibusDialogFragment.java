package br.edu.uniredentor.tachegando.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import br.edu.uniredentor.tachegando.MapasActivity;
import br.edu.uniredentor.tachegando.R;
import br.edu.uniredentor.tachegando.adapter.PassageiroAdapter;
import br.edu.uniredentor.tachegando.model.Denuncia;
import br.edu.uniredentor.tachegando.model.Passageiro;
import br.edu.uniredentor.tachegando.model.Viagem;
import br.edu.uniredentor.tachegando.utils.ConstantsUtils;
import br.edu.uniredentor.tachegando.utils.FirebaseUtils;
import br.edu.uniredentor.tachegando.utils.GeralUtils;
import br.edu.uniredentor.tachegando.utils.MapaUtils;
import br.edu.uniredentor.tachegando.utils.SharedUtils;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

/**
 * A simple {@link Fragment} subclass.
 */

public class InformacaoOnibusDialogFragment extends DialogFragment {

    private GoogleMap mapa;
    private MarcacaoUpdate marcacaoUpdate;
    private Viagem viagem;
    private TextView textViewNomeDaRota;
    private TextView textViewQuantidadeDeDenuncias;
    private Button buttonDenuncia;
    private TextView textViewDistancia;
    private double minhaLatitude;
    private double minhaLongitude;

    public InformacaoOnibusDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_informacao_onibus_dialog, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mostraChat();
        encontraViews(view);
        RecyclerView recyclerViewPassageiros = view.findViewById(R.id.recyclerView_passageiros);
        recyclerViewPassageiros.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewPassageiros.setAdapter(new PassageiroAdapter(viagem.getPassageiros()));
        recyclerViewPassageiros.addItemDecoration(new DividerItemDecoration(getContext(), VERTICAL));


        getToolbar(view);
        setTextos();

        buttonDenuncia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(getContext());
                if (GeralUtils.ehUsuario(getActivity())) {
                    alerta.setTitle(getString(R.string.onibus))
                            .setMessage(getString(R.string.denunciar_viagem))
                            .setNegativeButton(getString(R.string.nao), null)
                            .setPositiveButton(getString(R.string.sim), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Denuncia denuncia = new Denuncia();
                                    denuncia.setIdDenunciante(GeralUtils.getIdDoUsuario());
                                    FirebaseUtils.denuncia(viagem, denuncia);
                                    dismiss();
                                }
                            });
                    alerta.show();
                }
            }
        });

        return view;
    }

    private void setTextos() {
        textViewNomeDaRota.setText(viagem.getNome());
        textViewQuantidadeDeDenuncias.setText(viagem.getDenuncias().size() + " " + "denúncias");
        textViewDistancia.setText(getString(R.string.distancia) + " " + defineDistancia() + " m");
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

    private void encontraViews(View view) {
        textViewNomeDaRota = view.findViewById(R.id.textView_nome_rota);
        textViewQuantidadeDeDenuncias = view.findViewById(R.id.textView_quantidade_denuncias);
        textViewDistancia = view.findViewById(R.id.textView_distancia);
        buttonDenuncia = view.findViewById(R.id.button_denunciar);
    }

    private Toolbar getToolbar(final View view) {
        final Toolbar toolbar = view.findViewById(R.id.toolbar_principal);
        toolbar.setTitle(viagem.getNome());

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(getContext());
                switch (item.getItemId()) {
                    case R.id.item_entrar:
                        if (GeralUtils.ehUsuario(getActivity())) {
                            alerta.setTitle(getString(R.string.onibus))
                                    .setMessage(getString(R.string.deseja_entrar_no_onibus))
                                    .setNegativeButton(getString(R.string.nao), null)
                                    .setPositiveButton(getString(R.string.sim), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            entraOnibus(GeralUtils.getIdDoUsuario());


                                        }
                                    });
                            alerta.show();
                        }                        break;
                    case R.id.item_sair:
                        alerta.setTitle("Ônibus")
                                .setMessage("Deseja sair do onibus?")
                                .setNegativeButton("Não", null)
                                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        saiDoOnibus(GeralUtils.getIdDoUsuario());

                                    }
                                });
                        alerta.show();
                        break;

                    case R.id.item_trajeto:
                        FirebaseUtils.getViagem(viagem.getId()).collection(ConstantsUtils.TRAJETO).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                ArrayList<LatLng> locais = new ArrayList<>();
                                GeralUtils.show("Lat " + queryDocumentSnapshots.getDocuments().size());
                                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                                    GeralUtils.show("Lat " + snapshot.get("latitude"));
                                    LatLng latLng = new LatLng((Double) snapshot.get(ConstantsUtils.LATITUDE), (Double) snapshot.get(ConstantsUtils.LONGITUDE));
                                    locais.add(latLng);
                                }
                                GeralUtils.show("Locias " + locais.size());
                                Polyline polyline = MapaUtils.mostrarTrajeto(mapa, locais);
                                marcacaoUpdate.limpar(polyline);
                                dismiss();

                            }
                        });

                        break;
                }
                return true;
            }
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

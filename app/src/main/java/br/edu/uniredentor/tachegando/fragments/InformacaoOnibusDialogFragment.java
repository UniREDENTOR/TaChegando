package br.edu.uniredentor.tachegando.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

import br.edu.uniredentor.tachegando.MapasActivity;
import br.edu.uniredentor.tachegando.R;
import br.edu.uniredentor.tachegando.adapter.PassageiroAdapter;
import br.edu.uniredentor.tachegando.model.Passageiro;
import br.edu.uniredentor.tachegando.model.Viagem;
import br.edu.uniredentor.tachegando.utils.FirebaseUtils;
import br.edu.uniredentor.tachegando.utils.GeralUtils;
import br.edu.uniredentor.tachegando.utils.MapaUtils;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

/**
 * A simple {@link Fragment} subclass.
 */
public class InformacaoOnibusDialogFragment extends DialogFragment {

    private PassageiroAdapter adapter = new PassageiroAdapter();
    private GoogleMap mapa;
    private MarcacaoUpdate marcacaoUpdate;
    private Viagem viagem;
    private TextView textViewNomeDaRota;
    private ArrayList<Passageiro> passageiros;


    public InformacaoOnibusDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_informacao_onibus_dialog, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mostraChat();

        textViewNomeDaRota = view.findViewById(R.id.textView_nome_rota);
        textViewNomeDaRota.setText(viagem.getNome());
        RecyclerView recyclerViewPassageiros = view.findViewById(R.id.recyclerView_passageiros);
        recyclerViewPassageiros.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewPassageiros.setAdapter(adapter);
        recyclerViewPassageiros.addItemDecoration(new DividerItemDecoration(getContext(), VERTICAL));
        passageiros = new ArrayList<>(Arrays.asList(new Passageiro("https://static-wp-tor15-prd.torcedores.com/wp-content/uploads/2019/09/gabigol-540x338.jpg", "10 minutos"),
                new Passageiro("https://upload.wikimedia.org/wikipedia/commons/4/47/20171114_AUT_URU_4546_%28cropped%29.jpg", "20 minutos"),
                new Passageiro("https://colunadofla.com/wp-content/uploads/2019/09/everton-ribeiro-4.jpg", "25 minutos"),
                new Passageiro("https://www.hojeemdia.com.br/polopoly_fs/1.688211.1566479020!/image/image.jpg_gen/derivatives/landscape_653/image.jpg", "5 minutos", "Matheus") ));
        adapter.atualiza(passageiros);
        getToolbar(view);


        Location origem = new Location("");
        origem.setLatitude(-21.209075);
        origem.setLongitude(-41.886608);

        Location destino = new Location("");
        destino.setLatitude(-21.197089);
        destino.setLongitude(-41.867111);

        float distancia = origem.distanceTo(destino);
        String resultado = String.format("%.2f", distancia);
        TextView textViewDistancia = view.findViewById(R.id.textView_distancia);
        textViewDistancia.setText(getString(R.string.distancia) + resultado + " m");

        return view;
    }

    private Toolbar getToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar_principal);
        toolbar.setTitle(getString(R.string.onibus) + " 1");

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(getContext());
                switch (item.getItemId()){
                    case R.id.item_entrar:
                        if(GeralUtils.ehUsuario(getActivity())){
                            alerta.setTitle(getString(R.string.onibus))
                                    .setMessage(getString(R.string.deseja_entrar_no_onibus))
                                    .setNegativeButton(getString(R.string.nao), null)
                                    .setPositiveButton(getString(R.string.sim), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {}
                                    });
                            alerta.show();
                        }

                        break;

                    case R.id.item_denunciar:
                        if(GeralUtils.ehUsuario(getActivity())){
                            alerta.setTitle(getString(R.string.onibus))
                                    .setMessage(getString(R.string.deseja_denunciar_o_passageiro))
                                    .setNegativeButton(getString(R.string.nao), null)
                                    .setPositiveButton(getString(R.string.sim), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Passageiro passageiroCriador = new Passageiro();
                                    String idUsuario = "";
                                    FirebaseUtils.denuncia(passageiroCriador, idUsuario);
                                }
                            });
                            alerta.show();
                        }

                        break;

                    case R.id.item_sair:
                        alerta.setTitle("Ônibus").setMessage("Deseja sair do onibus?").setNegativeButton("Não", null).setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for(Passageiro passageiro : passageiros) {
                                        if(passageiro.getNome().equals("Matheus")) {
                                            passageiros.remove(passageiro);
                                            adapter.atualiza(passageiros);
                                        }
                                }

                            }
                        });
                        alerta.show();
                        break;

                    case R.id.item_trajeto:
                        FirebaseUtils.getBanco().collection("historico").document("pzmQpv1jXVSUrfWAUSvZ").collection("1").addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                                ArrayList<LatLng> locais = new ArrayList<>();
                                for(DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                                    try{
                                        LatLng latLng = new LatLng((Double) snapshot.get(getString(R.string.latitude)), (Double) snapshot.get(getString(R.string.longitude)));
                                        locais.add(latLng);
                                    }catch (Exception ex){
                                        ex.printStackTrace();
                                    }
                                }
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

    private void mostraChat() {
        getChildFragmentManager().beginTransaction().replace(R.id.linearLayout_chat, new ChatFragment(), "").commit();
    }

    public InformacaoOnibusDialogFragment setMapa(GoogleMap mapa){
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

    public interface MarcacaoUpdate{
        void limpar(Polyline pontos);
    }

}

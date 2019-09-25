package br.edu.uniredentor.tachegando.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;

import br.edu.uniredentor.tachegando.R;
import br.edu.uniredentor.tachegando.adapter.PassageiroAdapter;
import br.edu.uniredentor.tachegando.model.Passageiro;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

/**
 * A simple {@link Fragment} subclass.
 */
public class InformacaoOnibusDialogFragment extends DialogFragment {

    private PassageiroAdapter adapter = new PassageiroAdapter();

    public InformacaoOnibusDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_informacao_onibus_dialog, container, false);
        RecyclerView recyclerViewPassageiros = view.findViewById(R.id.recyclerView_passageiros);
        recyclerViewPassageiros.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewPassageiros.setAdapter(adapter);
        recyclerViewPassageiros.addItemDecoration(new DividerItemDecoration(getContext(), VERTICAL));
        ArrayList<Passageiro> passageiros = new ArrayList<>(Arrays.asList(new Passageiro("https://static-wp-tor15-prd.torcedores.com/wp-content/uploads/2019/09/gabigol-540x338.jpg", "10 minutos"),
                new Passageiro("https://upload.wikimedia.org/wikipedia/commons/4/47/20171114_AUT_URU_4546_%28cropped%29.jpg", "20 minutos"),
                new Passageiro("https://colunadofla.com/wp-content/uploads/2019/09/everton-ribeiro-4.jpg", "25 minutos"),
                new Passageiro("https://www.hojeemdia.com.br/polopoly_fs/1.688211.1566479020!/image/image.jpg_gen/derivatives/landscape_653/image.jpg", "5 minutos") ));
        adapter.atualiza(passageiros);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Toolbar toolbar = view.findViewById(R.id.toolbar_informacao);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_entrar:
                        AlertDialog.Builder alerta = new AlertDialog.Builder(getContext());
                        alerta.setTitle("Ônibus").setMessage("Deseja entrar no ônibus?").setNegativeButton("Não", null).setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alerta.show();
                        break;
                }
                return true;
            }
        });
        toolbar.inflateMenu(R.menu.menu_informacao_dialog);


        Location origem = new Location("");
        origem.setLatitude(-21.209075);
        origem.setLongitude(-41.886608);

        Location destino = new Location("");
        destino.setLatitude(-21.197089);
        destino.setLongitude(-41.867111);

        float distancia = origem.distanceTo(destino);
        String resultado = String.format("%.2f", distancia);
        TextView textViewDistancia = view.findViewById(R.id.textView_distancia);
        textViewDistancia.setText("Distância: " + resultado + " m");
        toolbar.setTitle("Onibus 1");
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

}

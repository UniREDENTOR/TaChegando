package br.edu.uniredentor.tachegando.fragments;


import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        // Inflate the layout for this fragment
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
        return view;
    }

}

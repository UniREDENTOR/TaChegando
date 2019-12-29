package br.edu.uniredentor.tachegando.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.List;

import br.edu.uniredentor.tachegando.R;
import br.edu.uniredentor.tachegando.adapter.ViagemAtivaAdapter;
import br.edu.uniredentor.tachegando.model.Viagem;
import br.edu.uniredentor.tachegando.utils.ConstantsUtils;
import br.edu.uniredentor.tachegando.utils.Singleton;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ViagensAtivasActivity extends AppCompatActivity {


    @BindView(R.id.recycler_view_lista_viagens_ativas) RecyclerView recyclerViewViagemAtiva;
    private ViagemAtivaAdapter viagemAtivaAdapter;
    private List<Viagem> viagemList = new ArrayList<>();
    private GoogleMap googleMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viagem_ativa);
        ButterKnife.bind(this);

        googleMap = Singleton.getInstance().getGoogleMap();
        viagemList = Singleton.getInstance().getViagemList();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        viagemAtivaAdapter = new ViagemAtivaAdapter(viagemList, this, googleMap);
        recyclerViewViagemAtiva.setLayoutManager(layoutManager);
        recyclerViewViagemAtiva.setAdapter(viagemAtivaAdapter);
        recyclerViewViagemAtiva.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
    }
}

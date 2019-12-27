package br.edu.uniredentor.tachegando.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import br.edu.uniredentor.tachegando.R;
import br.edu.uniredentor.tachegando.adapter.ViagemAtivaAdapter;
import br.edu.uniredentor.tachegando.model.Viagem;
import br.edu.uniredentor.tachegando.utils.ConstantsUtils;

public class ViagensAtivasActivity extends AppCompatActivity {


    private RecyclerView recyclerViewViagemAtiva;
    private ViagemAtivaAdapter viagemAtivaAdapter;
    private List<Viagem> viagemList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viagem_ativa);
        recyclerViewViagemAtiva = findViewById(R.id.recycler_view_lista_viagens_ativas);

        Intent i = getIntent();
        viagemList = (List<Viagem>) i.getSerializableExtra(ConstantsUtils.LISTA_VIAGENS_ATIVAS);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        viagemAtivaAdapter = new ViagemAtivaAdapter(viagemList, this);
        recyclerViewViagemAtiva.setLayoutManager(layoutManager);
        recyclerViewViagemAtiva.setAdapter(viagemAtivaAdapter);
        recyclerViewViagemAtiva.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

    }
}

package br.edu.uniredentor.tachegando.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.edu.uniredentor.tachegando.R;
import br.edu.uniredentor.tachegando.adapter.ViagemAtivaAdapter;
import br.edu.uniredentor.tachegando.model.Viagem;

public class ViagensAtivasActivity extends AppCompatActivity {


    private RecyclerView recyclerViewViagemAtiva;
    private ViagemAtivaAdapter viagemAtivaAdapter;
    private List<Viagem> viagemList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viagem_ativa);

        recyclerViewViagemAtiva = findViewById(R.id.recycler_view_lista_viagens_ativas);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewViagemAtiva.setLayoutManager(layoutManager);
        viagemAtivaAdapter = new ViagemAtivaAdapter(viagemList);
        recyclerViewViagemAtiva.setAdapter(viagemAtivaAdapter);
        recyclerViewViagemAtiva.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
    }
}

package br.edu.uniredentor.tachegando.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;


import br.edu.uniredentor.tachegando.R;

public class PerfilPassageiroActivity extends FragmentActivity {

    private TextView textViewNomePassageiro, textViewCorridaPassageiro;
    private ProgressBar reputacaoPassageiro;
    private ImageView imagemPassageiro, imagemCorrida, imagemEmblema;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_passageiro);

        createToolbar();


        textViewNomePassageiro = findViewById(R.id.textView_nome_perfil);
        textViewCorridaPassageiro = findViewById(R.id.textView_qtd_corrida);
        reputacaoPassageiro = findViewById(R.id.progressBar_reputacao_perfil);
        imagemPassageiro = findViewById(R.id.imageView_foto_perfil_passageiro);
        imagemCorrida = findViewById(R.id.imageView_qtd_corrida);
        imagemEmblema = findViewById(R.id.imageView_emblema_perfil);


    }

    @SuppressLint({"NewApi", "ResourceAsColor"})
    private void createToolbar() {
        Toolbar toolbarPerfilActivity = findViewById(R.id.toolbar_principal);

        toolbarPerfilActivity.setTitle(R.string.perfil);
        toolbarPerfilActivity.setElevation(0);

        //infla menu do perfil na toolbar
        toolbarPerfilActivity.inflateMenu(R.menu.menu_perfil_passageiro);
        toolbarPerfilActivity.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_sair_app:
                        finishAffinity();
                        break;
                    case R.id.editar_perfil:
                        Intent i = new Intent(PerfilPassageiroActivity.this, EditarPerfilPassageiroActivity.class);
                        startActivity(i);
                }
                return true;
            }
        });
    }

}
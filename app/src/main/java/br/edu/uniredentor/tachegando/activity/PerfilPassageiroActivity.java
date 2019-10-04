package br.edu.uniredentor.tachegando.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseUser;

import br.edu.uniredentor.tachegando.R;
import br.edu.uniredentor.tachegando.utils.FirebaseUtils;

public class PerfilPassageiroActivity extends AppCompatActivity {

    private TextView textViewNomePassageiro, textViewCorridaPassageiro;
    private ProgressBar reputacaoPassageiro;
    private ImageView imagemPassageiro, imagemCorrida, imagemEmblema;

    private FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_passageiro);

        getSupportActionBar().setTitle("Perfil");
        getSupportActionBar().setElevation(0);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#6A5ACD")));

        textViewNomePassageiro = findViewById(R.id.textView_nome_perfil);
        textViewCorridaPassageiro = findViewById(R.id.textView_qtd_corrida);
        reputacaoPassageiro = findViewById(R.id.progressBar_reputacao_perfil);
        imagemPassageiro = findViewById(R.id.imageView_foto_perfil_passageiro);
        imagemCorrida = findViewById(R.id.imageView_qtd_corrida);
        imagemEmblema = findViewById(R.id.imageView_emblema_perfil);


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseUtils.getCurrentUser() != null) {
            FirebaseUtils.novoUsuario(user.getPhoneNumber(), user.getUid());
        }
        else {
            Log.v("TAG", "nao peguei");
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_perfil_passageiro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_sair_app:
                finishAffinity();
                break;
            case R.id.editar_perfil:
                Intent i = new Intent(this, EditarPerfilPassageiroActivity.class);
                startActivity(i);
        }
        return true;
    }

}
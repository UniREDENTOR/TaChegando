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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import br.edu.uniredentor.tachegando.R;
import br.edu.uniredentor.tachegando.model.Passageiro;
import br.edu.uniredentor.tachegando.utils.FirebaseUtils;


public class PerfilPassageiroActivity extends AppCompatActivity {

    private TextView textViewQtdTempoPercorrido, textViewTiuloPassageiro, textViewNomePassageiro, textViewViagemPassageiro, textViewTelefonePassageiro, textViewReputacaoPassageiro, textViewCreditoPassageiro;
    private ImageView imagemPassageiro, imagemViagem, imagemCredito, imagemReputacao, imagemTituloPassageiro;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_passageiro);

        getSupportActionBar().setTitle("Perfil");
        getSupportActionBar().setElevation(0);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#6A5ACD")));

        inicializaComponentePerfil();
        recuperaPerfilPassageiro();
    }

    private void recuperaPerfilPassageiro() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseUtils.getBanco().collection("users").document(user.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        Log.d("", "Dado" + documentSnapshot);

                        Passageiro passageiro = documentSnapshot.toObject(Passageiro.class);
                        recuperaInformacaoPerfil(passageiro);
                        exibeInfoPassageiro(passageiro);
                    } else {
                        Log.d("", "Não existe");
                    }

                }
            });
        }
        else {
            //Usuario não logado
        }
    }

    private void exibeInfoPassageiro(Passageiro passageiro) {
        String id = passageiro.getId();
        Toast toast = Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void recuperaInformacaoPerfil(Passageiro passageiro) {
        textViewTiuloPassageiro.setText(passageiro.getTitulo());
        textViewNomePassageiro.setText(passageiro.getNome());
        textViewReputacaoPassageiro.setText(String.valueOf(passageiro.getReputacao()));
        textViewViagemPassageiro.setText(String.valueOf(passageiro.getQtdViagem()));
        textViewTelefonePassageiro.setText(passageiro.getTelefone());
        textViewCreditoPassageiro.setText(String.valueOf(passageiro.getCredito()));
        textViewQtdTempoPercorrido.setText(String.valueOf(passageiro.getTempo()));
    }

    private void inicializaComponentePerfil() {
        textViewQtdTempoPercorrido = findViewById(R.id.textView_qtd_tempo_percorrido);
        textViewTiuloPassageiro = findViewById(R.id.textView_titulo_perfil_passageiro);
        textViewNomePassageiro = findViewById(R.id.textView_nome_perfil_passageiro);
        textViewViagemPassageiro = findViewById(R.id.textView_qtd_viagens_passageiro);
        textViewReputacaoPassageiro = findViewById(R.id.textView_reputacao_passageiro);
        textViewTelefonePassageiro = findViewById(R.id.textView_telefone_passageiro);
        textViewCreditoPassageiro = findViewById(R.id.textView_credito_perfil_passageiro);

        imagemPassageiro = findViewById(R.id.imageView_foto_passageiro);
        imagemReputacao = findViewById(R.id.imageView_reputacao);
        imagemViagem = findViewById(R.id.imageView_qtd_corrida);
        imagemCredito = findViewById(R.id.imageView_credito_perfil);
        imagemTituloPassageiro = findViewById(R.id.imageView_emblema_perfil);
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
                FirebaseUtils.signOut();
                break;
            case R.id.editar_perfil:
                Intent i = new Intent(this, EditarPerfilPassageiroActivity.class);
                startActivity(i);
        }
        return true;
    }

}
package br.edu.uniredentor.tachegando.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import br.edu.uniredentor.tachegando.R;
import br.edu.uniredentor.tachegando.model.Passageiro;
import br.edu.uniredentor.tachegando.utils.FirebaseUtils;
import br.edu.uniredentor.tachegando.utils.GeralUtils;


public class PerfilPassageiroActivity extends FragmentActivity {

    private TextView textViewQtdTempoPercorrido, textViewTiuloPassageiro, textViewNomePassageiro, textViewViagemPassageiro, textViewTelefonePassageiro, textViewReputacaoPassageiro, textViewCreditoPassageiro;
    private ImageView imagemPassageiro, imagemViagem, imagemCredito, imagemReputacao, imagemTituloPassageiro;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_passageiro);

        createToolbar();
        inicializaComponentePerfil();
        recuperaPerfilPassageiro();
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
                        FirebaseUtils.signOut();
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

    private void recuperaPerfilPassageiro() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseUtils.getBanco().collection("users").document(user.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        Passageiro passageiro = documentSnapshot.toObject(Passageiro.class);
                        alteraInformacaoPerfil(passageiro);

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

    private void alteraInformacaoPerfil(Passageiro passageiro) {
        textViewTiuloPassageiro.setText(passageiro.getTitulo());
        textViewNomePassageiro.setText(passageiro.getNome());
        GeralUtils.mostraImagemCircular(getApplicationContext(), imagemPassageiro, passageiro.getFoto());
        textViewReputacaoPassageiro.setText(String.valueOf(passageiro.getReputacao()));
        textViewViagemPassageiro.setText(String.valueOf(passageiro.getQtdViagem()));
        textViewTelefonePassageiro.setText(passageiro.getTelefone());
        textViewCreditoPassageiro.setText(String.valueOf(passageiro.getCredito()));
        textViewQtdTempoPercorrido.setText(passageiro.getTempo());
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
}
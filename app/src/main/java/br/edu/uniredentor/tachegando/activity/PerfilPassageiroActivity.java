package br.edu.uniredentor.tachegando.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import br.edu.uniredentor.tachegando.MapasActivity;
import br.edu.uniredentor.tachegando.R;
import br.edu.uniredentor.tachegando.model.Passageiro;
import br.edu.uniredentor.tachegando.utils.FirebaseUtils;
import br.edu.uniredentor.tachegando.utils.GeralUtils;

import static br.edu.uniredentor.tachegando.utils.FirebaseUtils.signOut;


public class PerfilPassageiroActivity extends FragmentActivity {

    private TextView textViewTiuloPassageiro, textViewNomePassageiro, textViewViagemPassageiro, textViewReputacaoPassageiro;
    private ImageView imagemPassageiro;
    private GoogleSignInClient googleSignInClient;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_passageiro);
        inicializaComponentePerfil();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
        createToolbar();

        recuperaPerfilPassageiro();
    }

    @SuppressLint({"NewApi", "ResourceAsColor"})
    private void createToolbar() {
        final Toolbar toolbarPerfilActivity = findViewById(R.id.toolbar_principal);

        toolbarPerfilActivity.setTitle(R.string.perfil);
        toolbarPerfilActivity.setElevation(0);
        //infla menu do perfil na toolbar
        toolbarPerfilActivity.inflateMenu(R.menu.menu_perfil_passageiro);
        toolbarPerfilActivity.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_sair_app:
                        signOut();
                        googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent i = new Intent(getApplicationContext(), LoginPassageiroActivity.class);
                                startActivity(i);
                            }
                        });

                        break;
                    case R.id.item_mapa_app:
                        Intent intent = new Intent(getApplicationContext(), MapasActivity.class);
                        startActivity(intent);
                        break;

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
                        Log.d("Usuário: ", "Não existe");
                    }
                }
            });
        }
    }

    private void alteraInformacaoPerfil(Passageiro passageiro) {
        textViewTiuloPassageiro.setText(passageiro.getTitulo());
        textViewNomePassageiro.setText(passageiro.getNome());
        GeralUtils.mostraImagemCircular(getApplicationContext(), imagemPassageiro, passageiro.getFoto());
        textViewReputacaoPassageiro.setText(String.valueOf(passageiro.getReputacao()));
        textViewViagemPassageiro.setText(String.valueOf(passageiro.getQtdViagem()));

    }


    private void inicializaComponentePerfil() {
        textViewTiuloPassageiro = findViewById(R.id.textView_titulo_perfil_passageiro);
        textViewNomePassageiro = findViewById(R.id.textView_nome_perfil_passageiro);
        textViewViagemPassageiro = findViewById(R.id.textView_qtd_viagens_passageiro);
        textViewReputacaoPassageiro = findViewById(R.id.textView_reputacao_passageiro);
        imagemPassageiro = findViewById(R.id.imageView_foto_passageiro);

    }

    private void exibeInfoPassageiro(Passageiro passageiro) {
        String id = passageiro.getId();
        Toast toast = Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
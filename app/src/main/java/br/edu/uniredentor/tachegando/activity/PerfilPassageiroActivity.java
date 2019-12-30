package br.edu.uniredentor.tachegando.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.edu.uniredentor.tachegando.R;
import br.edu.uniredentor.tachegando.model.Passageiro;
import br.edu.uniredentor.tachegando.utils.FirebaseUtils;
import br.edu.uniredentor.tachegando.utils.GeralUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

import static br.edu.uniredentor.tachegando.utils.FirebaseUtils.signOut;


public class PerfilPassageiroActivity extends FragmentActivity {

    private GoogleSignInClient googleSignInClient;
    private FirebaseUser user;

    @BindView(R.id.toolbar_principal) Toolbar toolbarPerfilActivity;
    @BindView(R.id.textView_titulo_perfil_passageiro) TextView textViewTiuloPassageiro;
    @BindView(R.id.textView_nome_perfil_passageiro) TextView textViewNomePassageiro;
    @BindView(R.id.textView_qtd_viagens_passageiro) TextView textViewViagemPassageiro;
    @BindView(R.id.textView_reputacao_passageiro) TextView textViewReputacaoPassageiro;
    @BindView(R.id.imageView_foto_passageiro) ImageView imagemPassageiro;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_passageiro);
        ButterKnife.bind(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        recuperaPerfilPassageiro();
        createToolbar();
    }

    @SuppressLint({"NewApi", "ResourceAsColor"})
    private void createToolbar() {
        toolbarPerfilActivity.setTitle(R.string.perfil);
        toolbarPerfilActivity.setElevation(0);
        //infla menu do perfil na toolbar
        toolbarPerfilActivity.inflateMenu(R.menu.menu_perfil_passageiro);
        toolbarPerfilActivity.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.item_sair_app:
                    AlertDialog.Builder alerta = new AlertDialog.Builder(this);
                    alerta.setTitle(R.string.sair)
                            .setMessage(R.string.deseja_sair_do_app)
                            .setNegativeButton(R.string.nao, null)
                            .setPositiveButton(R.string.sim, (dialog, which) -> {
                                signOut();
                                googleSignInClient.signOut().addOnCompleteListener(task -> finish());
                            }).create().show();
                    break;
            }
            return true;
        });
    }

    private void recuperaPerfilPassageiro() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseUtils.getBanco().collection("users").document(user.getUid()).addSnapshotListener((documentSnapshot, e) -> {
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    Passageiro passageiro = documentSnapshot.toObject(Passageiro.class);
                    alteraInformacaoPerfil(passageiro);
                } else {
                    Log.d("Usuário: ", "Não existe");
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
}
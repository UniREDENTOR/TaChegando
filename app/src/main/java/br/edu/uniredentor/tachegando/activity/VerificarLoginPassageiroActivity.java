package br.edu.uniredentor.tachegando.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.concurrent.TimeUnit;

import br.edu.uniredentor.tachegando.R;
import br.edu.uniredentor.tachegando.model.Passageiro;
import br.edu.uniredentor.tachegando.utils.FirebaseUtils;

import static br.edu.uniredentor.tachegando.utils.FirebaseUtils.getAuth;

public class VerificarLoginPassageiroActivity extends FragmentActivity {

    private String mVerificacaoId;
    private EditText editTextCodigo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificar_login);

        createToolbar();

        editTextCodigo = findViewById(R.id.editTextCodigo);

        //Recebendo o numero
        Intent intent = getIntent();
        String mobile = intent.getStringExtra("mobile");


        findViewById(R.id.buttonEntrar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codigo = editTextCodigo.getText().toString().trim();
                if (codigo.isEmpty() || codigo.length() < 6) {
                    editTextCodigo.setError(getString(R.string.entre_com_codigo));
                    editTextCodigo.requestFocus();
                    return;
                }
    }

    @SuppressLint("NewApi")
    private void createToolbar() {
        Toolbar toolbarVerificarLogin = findViewById(R.id.toolbar_principal);
        toolbarVerificarLogin.setTitle(R.string.verificacao);
        toolbarVerificarLogin.setElevation(0);

        toolbarVerificarLogin.inflateMenu(R.menu.menu_verificacao_login_passageiro);
        toolbarVerificarLogin.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_cancelar:
                        startActivity(new Intent(VerificarLoginPassageiroActivity.this, LoginPassageiroActivity.class));
                        finishAffinity();
                        break;
                }
                return false;
            }
        });
    }



    private void verificaPassageiro() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DocumentReference getId = FirebaseUtils.getBanco().collection("users").document(user.getUid());
        getId.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                            if(documentSnapshot.exists()) {
                                Log.i("tag", "Usuario existe no firestore");
                            } else {
                                Log.i("tag", "Usuario nao existe");
                                String id = user.getUid();
                                String telefone = user.getPhoneNumber();
                                String nome = "user";
                                String foto = "";
                                int tempo = 0;//reformular var para date time
                                String titulo = "Iniciante";
                                int viagem = 0;
                                int reputacao = 0;
                                double credito = 0.0;
                                Passageiro passageiro = new Passageiro(id, nome, foto, reputacao, titulo, credito, viagem);
                                FirebaseUtils.salvaUsuario(passageiro);
                            }

                }
            }
        });
    }


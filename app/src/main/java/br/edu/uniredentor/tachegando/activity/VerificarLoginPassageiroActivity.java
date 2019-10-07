package br.edu.uniredentor.tachegando.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import br.edu.uniredentor.tachegando.R;
import br.edu.uniredentor.tachegando.model.Passageiro;
import br.edu.uniredentor.tachegando.utils.FirebaseUtils;

import static br.edu.uniredentor.tachegando.utils.FirebaseUtils.getAuth;

public class VerificarLoginPassageiroActivity extends AppCompatActivity {

    private String mVerificacaoId;
    private EditText editTextCodigo;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificar_login);

        getSupportActionBar().setTitle("Verificação");
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#6A5ACD")));

        mAuth = FirebaseAuth.getInstance();
        editTextCodigo = findViewById(R.id.editTextCodigo);

        //Recebendo o numero
        Intent intent = getIntent();
        String mobile = intent.getStringExtra("mobile");

        enviarVerificacaoCodigo(mobile);


        findViewById(R.id.buttonEntrar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codigo = editTextCodigo.getText().toString().trim();
                if (codigo.isEmpty() || codigo.length() < 6) {
                    editTextCodigo.setError("Entre com o código");
                    editTextCodigo.requestFocus();
                    return;
                }
                try {
                    verificacaoCodigoEnviado(codigo);
                } catch (Exception e) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Código expirado",
                            Toast.LENGTH_SHORT);

                    toast.show();
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, LoginPassageiroActivity.class));
                finishAffinity();
                break;
            default:
                break;
        }
        return true;
    }

    private void enviarVerificacaoCodigo(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+55" + mobile, //Código do pais
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBacks);
    }

    private void verificacaoCodigoEnviado(String codigo) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificacaoId, codigo);
        signInWithPhoneAuthCredential(credential);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String codigo = phoneAuthCredential.getSmsCode();

            if (codigo != null) {
                //Altera o campo do texto
                editTextCodigo.setText(codigo);
                verificacaoCodigoEnviado(codigo);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificacaoId = s;
        }
    };

    private void verificaUsuario() {
        FirebaseUser user = getAuth().getCurrentUser();
        if (user != null) {
            String id = user.getUid();
            String telefone = user.getPhoneNumber();
            String nome = "";
            String foto = "";
            String tempo = "";
            String tituloReputacao = "Iniciante";
            int qtdViagem = 0;
            int reputacao = 0;
            Double credito = 0.0;

            Passageiro passageiro = new Passageiro(id, telefone, nome, foto, tempo, reputacao, tituloReputacao, credito, qtdViagem);
            FirebaseUtils.salvaUsuario(passageiro);

        } else {

        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(VerificarLoginPassageiroActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                try {
                    if (task.isSuccessful()) {
                        //Verificação realizada
                        verificaUsuario();
                        Toast toast = Toast.makeText(getApplicationContext(), "Verificação realizada", Toast.LENGTH_SHORT);toast.show();
                        Intent i = new Intent(getApplicationContext(), PerfilPassageiroActivity.class);
                        startActivity(i);

                    }
                } catch (Exception e) {
                    //Verificação não foi realizada
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Verificação não realizada",
                            Toast.LENGTH_SHORT);

                    toast.show();
                }

            }
        });
    }



}

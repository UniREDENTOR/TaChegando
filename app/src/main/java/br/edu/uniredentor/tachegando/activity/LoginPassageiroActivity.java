package br.edu.uniredentor.tachegando.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;


import br.edu.uniredentor.tachegando.MapasActivity;
import br.edu.uniredentor.tachegando.R;
import br.edu.uniredentor.tachegando.model.Passageiro;
import br.edu.uniredentor.tachegando.utils.FirebaseUtils;

public class LoginPassageiroActivity extends FragmentActivity {

    private  GoogleSignInClient mGoogleSignInClient;
    private SignInButton signInButton;
    private int RC_SIGN_IN = 0;
    FirebaseAuth mAuth;
    private GoogleSignInAccount account;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_passageiro);

        signInButton = findViewById(R.id.sign_in_button);
        createToolbar();
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

       signInButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              switch (v.getId()) {
                  case R.id.sign_in_button:
                      signIn();
                      break;
              }
           }
       });
    }

    @SuppressLint("NewApi")
    private void createToolbar() {
        Toolbar toolbarLoginPassageiro = findViewById(R.id.toolbar_principal);
        toolbarLoginPassageiro.setTitle(R.string.login);
        toolbarLoginPassageiro.setElevation(0);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
           account = completedTask.getResult(ApiException.class);
           firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.

        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    verificaPassageiro();
                    Log.v("ID USUÁRIO", mAuth.getCurrentUser().getUid());
                }
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
                        Log.i("tag", "Usuário existe no firestore");
                        Intent i = new Intent(getApplicationContext(), MapasActivity.class);
                        startActivity(i);
                    } else {
                        String titulo = "Iniciante";
                        int viagem = 0;
                        int reputacao = 0;
                        String id = user.getUid();
                        double credito = 0.0;
                        String fotoPerfil = String.valueOf(account.getPhotoUrl());
                        Passageiro passageiro = new Passageiro(id, account.getDisplayName(), fotoPerfil, reputacao, titulo, credito, viagem);
                        FirebaseUtils.salvaUsuario(passageiro);
                        Intent i = new Intent(getApplicationContext(), MapasActivity.class);
                        startActivity(i);


                    }

                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

    }
}

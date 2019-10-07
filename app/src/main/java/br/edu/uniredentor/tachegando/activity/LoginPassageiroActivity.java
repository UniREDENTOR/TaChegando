package br.edu.uniredentor.tachegando.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;


import br.edu.uniredentor.tachegando.R;

public class LoginPassageiroActivity extends FragmentActivity {

    private EditText editTextNumero;

    FirebaseAuth auth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_passageiro);

        createToolbar();


        auth = FirebaseAuth.getInstance();


        editTextNumero = findViewById(R.id.editTextMobile);


        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile = editTextNumero.getText().toString().trim();

                if(mobile.isEmpty() || mobile.length() < 10){
                    editTextNumero.setError(getString(R.string.entre_com_numero_valido));
                    editTextNumero.requestFocus();
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), VerificarLoginPassageiroActivity.class);
                intent.putExtra("mobile", mobile);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("NewApi")
    private void createToolbar() {
        Toolbar toolbarLoginPassageiro = findViewById(R.id.toolbar_principal);

        toolbarLoginPassageiro.setTitle(R.string.login);
        toolbarLoginPassageiro.setElevation(0);
    }

}

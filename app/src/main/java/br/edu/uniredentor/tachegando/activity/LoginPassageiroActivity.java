package br.edu.uniredentor.tachegando.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;


import br.edu.uniredentor.tachegando.R;

public class LoginPassageiroActivity extends AppCompatActivity {

    private EditText editTextNumero;

    FirebaseAuth auth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_passageiro);

        getSupportActionBar().setTitle(getString(R.string.login));
        getSupportActionBar().setElevation(0);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#6A5ACD")));

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

}

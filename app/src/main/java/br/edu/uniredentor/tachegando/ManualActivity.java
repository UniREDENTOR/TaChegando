package br.edu.uniredentor.tachegando;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import br.edu.uniredentor.tachegando.R;
import br.edu.uniredentor.tachegando.model.Viagem;
import br.edu.uniredentor.tachegando.utils.FirebaseUtils;

public class ManualActivity extends AppCompatActivity {

    private EditText editTextRotaManual;
    private Button buttonSalvarRotaManual;
    private double latitude, longitude;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);

        editTextRotaManual = findViewById(R.id.edit_text_rota_manual);
        buttonSalvarRotaManual = findViewById(R.id.btn_salvar_rota_manual);

        latitude = getIntent().getExtras().getDouble("latitude");
        longitude = getIntent().getExtras().getDouble("longitude");

        buttonSalvarRotaManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Viagem viagem = new Viagem();
                viagem.setNome(editTextRotaManual.getText().toString());
                viagem.setLatitude(latitude);
                viagem.setLongitude(longitude);
                viagem.setIdUsuario("10");
                viagem.setId("200");
                FirebaseUtils.salva(viagem);
            }
        });
    }
}

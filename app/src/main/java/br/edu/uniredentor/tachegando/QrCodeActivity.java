package br.edu.uniredentor.tachegando;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import br.edu.uniredentor.tachegando.model.Viagem;
import br.edu.uniredentor.tachegando.utils.FirebaseUtils;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView qrCodeScanner;
    private TextView textViewResultado;
    private double latitude, longitude;
    private Button buttonSalvarRota;
    private String rota;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        qrCodeScanner= findViewById(R.id.qr_code_scan);
        textViewResultado = findViewById(R.id.text_resultado_qrcode);
        buttonSalvarRota = findViewById(R.id.btn_salvar_rota_qrcode);

        latitude = getIntent().getExtras().getDouble("latitude");
        longitude = getIntent().getExtras().getDouble("longitude");



        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        qrCodeScanner.setResultHandler(QrCodeActivity.this);
                        qrCodeScanner.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(QrCodeActivity.this, "Você precisa aceitar a permissão", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
                .check();

        buttonSalvarRota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Viagem viagem = new Viagem();
                viagem.setLongitude(longitude);
                viagem.setLatitude(latitude);
                viagem.setNome(rota);
                viagem.setIdUsuario("4");
                viagem.setId("4");
                FirebaseUtils.salva(viagem);
                finish();


            }
        });

    }

    @Override
    protected void onDestroy() {
        qrCodeScanner.stopCamera();
        super.onDestroy();
    }

    @Override
    public void handleResult(Result rawResult) {
        textViewResultado.setText(rawResult.getText());
        rota = rawResult.getText();
    }
}

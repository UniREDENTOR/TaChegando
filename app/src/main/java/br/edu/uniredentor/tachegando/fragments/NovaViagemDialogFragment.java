package br.edu.uniredentor.tachegando.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import br.edu.uniredentor.tachegando.ManualActivity;
import br.edu.uniredentor.tachegando.QrCodeActivity;

public class NovaViagemDialogFragment extends AppCompatDialogFragment {

    private double latitude;
    private double longitude;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Nova Viagem")
                .setMessage("Escolha como deseja cadastrar uma nova viagem:")
                .setPositiveButton("Manual", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent manual = new Intent(getContext(),ManualActivity.class);
                        manual.putExtra("longitude",longitude);
                        manual.putExtra("latitude", latitude);
                        startActivity(manual);




                    }
                }).setNegativeButton("QrCode", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent qrCode = new Intent(getContext(), QrCodeActivity.class);
                qrCode.putExtra("longitude", longitude);
                qrCode.putExtra("latitude", latitude);
                startActivity(qrCode);
            }
        });

        return builder.create();
    }

    public void pegarDados(double latitude, double longitute) {
        this.latitude = latitude;
        this.longitude = longitute;
    }
}

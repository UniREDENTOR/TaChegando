package br.edu.uniredentor.tachegando.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.fragment.app.FragmentActivity;

import br.edu.uniredentor.tachegando.fragments.NovoPontoManualDialogFragment;

public class CriarPontoController {

    public static void alertaDeNovoPonto(final FragmentActivity activity, final double latitude, final double longitude) {
        AlertDialog.Builder alerta = new AlertDialog.Builder(activity);
        alerta.setTitle("Cadastrar Ponto").setMessage("Deseja adicionar um novo ponto de que forma?").setPositiveButton("QrCode", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        }).setNegativeButton("Vou digitar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                NovoPontoManualDialogFragment.novaInstancia(latitude, longitude).show(activity.getSupportFragmentManager(), "NovoPonto");

            }
        }).show();
    }
}

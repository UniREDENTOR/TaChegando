package br.edu.uniredentor.tachegando.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.fragment.app.FragmentActivity;

import br.edu.uniredentor.tachegando.R;
import br.edu.uniredentor.tachegando.fragments.NovaViagemManualDialogFragment;
import br.edu.uniredentor.tachegando.fragments.NovaViagemQrCodeDialogFragment;

public class NovaViagemController {

    public static void alertaDeNovaViagem(final FragmentActivity activity, final double latitude, final double longitude){
        AlertDialog.Builder alerta = new AlertDialog.Builder(activity);
        alerta.setTitle(activity.getString(R.string.nova_viagem)).setMessage(activity.getString(R.string.deseja_adicionar_uma_nova_viagem_de_que_forma)).setPositiveButton(activity.getString(R.string.qrCode), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 NovaViagemQrCodeDialogFragment.novaInstancia(latitude, longitude).show(activity.getSupportFragmentManager(),"novaViagemCrCode");

            }
        }).setNegativeButton(activity.getString(R.string.manual), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                NovaViagemManualDialogFragment.novaInstancia(latitude, longitude).show(activity.getSupportFragmentManager(), "novaViagem");
            }
        }).show();
    }
}

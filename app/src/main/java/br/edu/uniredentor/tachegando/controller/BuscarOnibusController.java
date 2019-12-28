package br.edu.uniredentor.tachegando.controller;

import android.app.AlertDialog;

import android.text.InputType;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;

import java.util.List;

import br.edu.uniredentor.tachegando.R;
import br.edu.uniredentor.tachegando.fragments.BuscarOnibusDialogFragment;
import br.edu.uniredentor.tachegando.model.Viagem;
import br.edu.uniredentor.tachegando.utils.ConstantsUtils;


public class BuscarOnibusController {

    public static void alertaDeBusca(final FragmentActivity activity, final List<Viagem> listaLocal, final GoogleMap mapa) {
        final AlertDialog.Builder alerta = new AlertDialog.Builder(activity);

        LinearLayout container = new LinearLayout(activity.getApplicationContext());
        container.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(64, -48, 64, 0);

        final List<Viagem> listaDeViagensFiltrada = new ArrayList<>();

        final EditText editTextlocal = new EditText(activity.getApplicationContext());

        editTextlocal.setLayoutParams(lp);
        editTextlocal.setHint("Nome da Viagem");
        editTextlocal.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES|InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        container.addView(editTextlocal, lp);
        alerta.setView(container);

        alerta.setTitle(activity.getString(R.string.procurar_onibus)).setMessage(activity.getString(R.string.informe_para_onde_deseja_ir))
                .setPositiveButton(activity.getString(R.string.procurar), (dialog, which) -> {
                    String localDesejado = editTextlocal.getText().toString().toLowerCase();
                    for (Viagem viagem : listaLocal) {
                        if (viagem.getNome().toLowerCase().contains(localDesejado) && !localDesejado.isEmpty()) {
                            listaDeViagensFiltrada.add(viagem);
                        }
                    }

                    if (listaDeViagensFiltrada.size() > 0) {
                        BuscarOnibusDialogFragment.novaInstancia(listaDeViagensFiltrada).setMapa(mapa).show(activity.getSupportFragmentManager(), "buscarOnibus");
                    } else {
                        Toast.makeText(activity, ConstantsUtils.VIAGEM_NÃO_ENCONTRADA, Toast.LENGTH_LONG).show();
                        BuscarOnibusController.alertaDeBusca(activity, listaLocal, mapa);
                    }

                }).setNegativeButton(activity.getString(R.string.cancelar), null).show();
    }
}
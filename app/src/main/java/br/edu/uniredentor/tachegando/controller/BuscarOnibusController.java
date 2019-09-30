package br.edu.uniredentor.tachegando.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Toast;


import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;

import java.util.List;

import br.edu.uniredentor.tachegando.fragments.BuscarOnibusDialogFragment;
import br.edu.uniredentor.tachegando.model.Viagem;


public class BuscarOnibusController {

    public static void alertaDeBusca(final FragmentActivity activity, final List<Viagem> listaLocal, final GoogleMap mapa) {
        final AlertDialog.Builder alerta = new AlertDialog.Builder(activity);
        final  List<Viagem> listaDeViagensFiltrada = new ArrayList<>();
        final EditText editTextlocal = new EditText(activity.getApplicationContext());
        alerta.setView(editTextlocal);
        alerta.setTitle("Procurar Onibus:").setMessage("Informe para onde deseja ir:").setPositiveButton("Procurar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String localDesejado = editTextlocal.getText().toString().toLowerCase();
                for (Viagem viagem : listaLocal) {
                    if(viagem.getNome().toLowerCase().contains(localDesejado) && !localDesejado.isEmpty()){
                        listaDeViagensFiltrada.add(viagem);
                        BuscarOnibusDialogFragment.novaInstancia(listaDeViagensFiltrada).setMapa(mapa).show(activity.getSupportFragmentManager(), "buscarOnibus");
                    } else {
                        Toast.makeText(activity, "Onibus não encontrado", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }).setNegativeButton("Cancelar", null).show();
    }
}

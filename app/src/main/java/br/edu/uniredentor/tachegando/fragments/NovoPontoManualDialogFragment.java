package br.edu.uniredentor.tachegando.fragments;


import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import br.edu.uniredentor.tachegando.R;
import br.edu.uniredentor.tachegando.model.Ponto;
import br.edu.uniredentor.tachegando.utils.ConstantsUtils;
import br.edu.uniredentor.tachegando.utils.GeralUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class NovoPontoManualDialogFragment extends DialogFragment {

    private double latitude;
    private double longitude;
    private TextInputEditText editTextPontoManual;


    public static NovoPontoManualDialogFragment novaInstancia(double latitude, double longitude) {
        // Required empty public constructor
        NovoPontoManualDialogFragment fragment = new NovoPontoManualDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putDouble(ConstantsUtils.LATITUDE, latitude);
        bundle.putDouble(ConstantsUtils.LONGITUDE, longitude);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_novo_ponto_manual_dialog, container,false);
        createToolbar(view);
        latitude = getArguments().getDouble(ConstantsUtils.LATITUDE);
        longitude = getArguments().getDouble(ConstantsUtils.LONGITUDE);
        editTextPontoManual = view.findViewById(R.id.editText_ponto);
        Button buttonPontoManual = view.findViewById(R.id.button_salvar_ponto_manual);
        TextView textViewEndereco = view.findViewById(R.id.textView_endereco_atual_ponto);
        String enderecoAtual = GeralUtils.getEndereco(getContext(), latitude, longitude);
        textViewEndereco.setText(getString(R.string.sua_localizacao_atual_e) + " " + enderecoAtual);

        buttonPontoManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomePonto = editTextPontoManual.getText().toString();
                Ponto ponto = new Ponto();
                ponto.setNome(nomePonto);
                ponto.setId("1231");
                ponto.setIdUsuario("2");
                ponto.setLatitude(latitude);
                ponto.setLongitude(longitude);

                dismiss();
            }

        });

        return view;

    }



    private void createToolbar(View view) {
        Toolbar toolbarNovaViagem = view.findViewById(R.id.toolbar_principal);
        toolbarNovaViagem.setTitle(getString(R.string.novo_ponto_manual));
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}

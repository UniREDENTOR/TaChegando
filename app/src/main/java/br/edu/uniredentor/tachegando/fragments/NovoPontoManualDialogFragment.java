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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class NovoPontoManualDialogFragment extends DialogFragment {

    private double latitude;
    private double longitude;

    @BindView(R.id.editText_ponto) TextInputEditText editTextPontoManual;
    @BindView(R.id.textView_endereco_atual_ponto) TextView textViewEndereco;
    @BindView(R.id.toolbar_principal) Toolbar toolbarNovaViagem;

    public static NovoPontoManualDialogFragment novaInstancia(double latitude, double longitude) {
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
        View view = inflater.inflate(R.layout.fragment_novo_ponto_manual_dialog, container, false);
        ButterKnife.bind(this, view);
        toolbarNovaViagem.setTitle(getString(R.string.novo_ponto_manual));

        latitude = getArguments().getDouble(ConstantsUtils.LATITUDE);
        longitude = getArguments().getDouble(ConstantsUtils.LONGITUDE);

        textViewEndereco.setText(getString(R.string.sua_localizacao_atual_e) + " ");


        return view;

    }

    @OnClick(R.id.button_salvar_ponto_manual)
    public void salvarPontoManual(){
        String nomePonto = editTextPontoManual.getText().toString();
        Ponto ponto = new Ponto();
        ponto.setNome(nomePonto);
        ponto.setId("1231");
        ponto.setIdUsuario("2");
        ponto.setLatitude(latitude);
        ponto.setLongitude(longitude);

        dismiss();
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

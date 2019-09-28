package br.edu.uniredentor.tachegando.fragments;

import android.app.Dialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import br.edu.uniredentor.tachegando.R;
import br.edu.uniredentor.tachegando.model.Viagem;
import br.edu.uniredentor.tachegando.utils.ConstantsUtils;
import br.edu.uniredentor.tachegando.utils.FirebaseUtils;
import br.edu.uniredentor.tachegando.utils.GeralUtils;
import br.edu.uniredentor.tachegando.utils.Singleton;

public class NovaViagemManualDialogFragment extends DialogFragment {

    private double latitude;
    private double longitude;

    public static NovaViagemManualDialogFragment novaInstancia(double latitude, double longitude) {
        NovaViagemManualDialogFragment fragment = new NovaViagemManualDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putDouble(ConstantsUtils.LATITUDE, latitude);
        bundle.putDouble(ConstantsUtils.LONGITUDE, longitude);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nova_viagem_manual_dialog, container, false);

        final TextInputEditText editTextRotaManual = view.findViewById(R.id.editText_rota_manual);
        Button buttonSalvarRotaManual = view.findViewById(R.id.button_salvar_rota_manual);
        TextView textViewEndereco = view.findViewById(R.id.textView_endereco_atual);
        latitude = getArguments().getDouble(ConstantsUtils.LATITUDE);
        longitude = getArguments().getDouble(ConstantsUtils.LONGITUDE);
        String enderecoAtual = GeralUtils.getEndereco(getContext(), latitude, longitude);
        textViewEndereco.setText(getString(R.string.sua_localizacao_atual_e) + " " + enderecoAtual);
        buttonSalvarRotaManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Viagem viagem = new Viagem();
                viagem.setNome(editTextRotaManual.getText().toString());
                viagem.setLatitude(latitude);
                viagem.setLongitude(longitude);
                viagem.setIdUsuario("1");
                String id = FirebaseUtils.salva(viagem);
                viagem.setId(id);
                FirebaseUtils.atualizaId(viagem);
                GeralUtils.show("Id " + id);
                Singleton.getInstance().setIdViagem(viagem.getId());
                dismiss();
            }
        });

        return view;
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

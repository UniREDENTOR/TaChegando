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
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.edu.uniredentor.tachegando.R;
import br.edu.uniredentor.tachegando.model.Passageiro;
import br.edu.uniredentor.tachegando.model.Viagem;
import br.edu.uniredentor.tachegando.utils.ConstantsUtils;
import br.edu.uniredentor.tachegando.utils.FirebaseUtils;
import br.edu.uniredentor.tachegando.utils.GeralUtils;
import br.edu.uniredentor.tachegando.utils.Singleton;

public class NovaViagemManualDialogFragment extends DialogFragment {

    private double latitude;
    private double longitude;
    private TextInputEditText editTextRotaManual;
    private FirebaseUser user;

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

        createToolbar(view);
        user = FirebaseUtils.getAuth().getCurrentUser();

        editTextRotaManual = view.findViewById(R.id.editText_rota_manual);
        Button buttonSalvarRotaManual = view.findViewById(R.id.button_salvar_rota_manual);
        TextView textViewEndereco = view.findViewById(R.id.textView_endereco_atual);
        latitude = getArguments().getDouble(ConstantsUtils.LATITUDE);
        longitude = getArguments().getDouble(ConstantsUtils.LONGITUDE);
        String enderecoAtual = GeralUtils.getEndereco(getContext(), latitude, longitude);
        textViewEndereco.setText(getString(R.string.sua_localizacao_atual_e) + " " + enderecoAtual);
        buttonSalvarRotaManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nome = editTextRotaManual.getText().toString();
                if(ehValido(nome)){
                    ArrayList<String> ids = new ArrayList<>();
                    Viagem viagem = new Viagem();
                    viagem.setNome(nome);
                    viagem.setLatitude(latitude);
                    viagem.setLongitude(longitude);
                    viagem.setIdUsuario(user.getUid());
                    String id = FirebaseUtils.salvaViagem(viagem);
                    viagem.setId(id);
                    viagem.setIdPassageiros(ids);
                    FirebaseUtils.atualizaId(viagem);
                    GeralUtils.show("Id " + id);
                    Singleton.getInstance().setIdViagem(viagem.getId());

                }
                dismiss();
            }
        });

        return view;
    }

    private void createToolbar(View view) {
        Toolbar toolbarNovaViagem = view.findViewById(R.id.toolbar_principal);
        toolbarNovaViagem.setTitle(getString(R.string.nova_viagem_manual));
    }

    private boolean ehValido(String nome) {
        if(nome.isEmpty()){
            editTextRotaManual.setError(getString(R.string.campo_em_branco));
            return false;
        }

        if(latitude == 0 || longitude == 0){
            GeralUtils.mostraMensagem(getActivity(), getString(R.string.localizacao_nao_encontrada));
            return false;
        }
        return true;
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

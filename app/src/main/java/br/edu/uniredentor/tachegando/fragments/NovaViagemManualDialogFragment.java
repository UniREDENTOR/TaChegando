package br.edu.uniredentor.tachegando.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;

import br.edu.uniredentor.tachegando.R;
import br.edu.uniredentor.tachegando.model.Viagem;
import br.edu.uniredentor.tachegando.utils.ConstantsUtils;
import br.edu.uniredentor.tachegando.utils.FirebaseUtils;
import br.edu.uniredentor.tachegando.utils.GeralUtils;
import br.edu.uniredentor.tachegando.utils.SharedUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NovaViagemManualDialogFragment extends DialogFragment {

    private double latitude;
    private double longitude;

    @BindView(R.id.editText_rota_manual) TextInputEditText editTextRotaManual;
    @BindView(R.id.button_salvar_rota_manual) Button buttonSalvarRotaManual;
    @BindView(R.id.textView_endereco_atual) TextView textViewEndereco;
    @BindView(R.id.toolbar_principal) Toolbar toolbarNovaViagem;

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
        ButterKnife.bind(this, view);
        toolbarNovaViagem.setTitle(getString(R.string.nova_viagem_manual));

        latitude = getArguments().getDouble(ConstantsUtils.LATITUDE);
        longitude = getArguments().getDouble(ConstantsUtils.LONGITUDE);
        String enderecoAtual = GeralUtils.getEndereco(getContext(), latitude, longitude);
        textViewEndereco.setText(getString(R.string.sua_localizacao_atual_e) + " " + enderecoAtual);

        buttonSalvarRotaManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nome = editTextRotaManual.getText().toString();
                if (ehValido(nome)) {
                    Viagem viagem = new Viagem();
                    viagem.setNome(nome);
                    viagem.setId(GeralUtils.getIdDoUsuario());
                    viagem.setLatitude(latitude);
                    viagem.setLongitude(longitude);
                    viagem.setAtiva(true);
                    FirebaseUtils.salvaViagem(viagem);
                    SharedUtils.save(viagem.getId(), getActivity());
                }
                dismiss();
            }
        });

        return view;
    }

    private boolean ehValido(String nome) {
        if (nome.isEmpty()) {
            editTextRotaManual.setError(getString(R.string.campo_em_branco));
            return false;
        }

        if (latitude == 0 || longitude == 0) {
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

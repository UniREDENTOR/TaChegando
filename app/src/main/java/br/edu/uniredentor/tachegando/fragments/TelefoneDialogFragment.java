package br.edu.uniredentor.tachegando.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import br.edu.uniredentor.tachegando.R;
import br.edu.uniredentor.tachegando.model.Passageiro;
import br.edu.uniredentor.tachegando.utils.FirebaseUtils;

public class TelefoneDialogFragment extends AppCompatDialogFragment {

    EditText editTextNovoTelefone;
    TextView textViewExibirTelefone;

    String telefone = "";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_telefone_editar_perfil_dialog, null);

        inicializaComponente(view);
        recuperarTelefone();

        builder.setView(view).setTitle("Telefone").setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                telefone = editTextNovoTelefone.getText().toString().trim();
                Log.d("telefone", telefone);
                if (!telefone.isEmpty()) {
                    salvarNovoTelefone();
                    return;
                } else {
                    editTextNovoTelefone.setText("Digite o telefone");
                    editTextNovoTelefone.requestFocus();
                }

            }
        });
        return builder.create();
    }

    private void recuperarTelefone() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseUtils.getBanco().collection("users").document(user.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        Passageiro passageiro = documentSnapshot.toObject(Passageiro.class);
                        alteraInformacaoTelefoneDialog(passageiro);
                    } else {
                        Log.d("TAG", "Não existe");
                    }
                }
            });
        } else {

        }
    }

    private void salvarNovoTelefone() {
        FirebaseUtils.usuarioCadastrado();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Passageiro passageiro = new Passageiro(telefone);
        FirebaseUtils.getBanco().collection("users").document(user.getUid()).update(
                "telefone", "+55" + passageiro.getTelefone()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.w("TAG", telefone );
            }
        });
    }

    private void alteraInformacaoTelefoneDialog(Passageiro passageiro) {
        textViewExibirTelefone.setText(passageiro.getTelefone());
    }

    private void inicializaComponente(View view) {
        editTextNovoTelefone = view.findViewById(R.id.editText_novo_telefone_usuario);
        textViewExibirTelefone = view.findViewById(R.id.textView_telefone_dialog);
    }

}

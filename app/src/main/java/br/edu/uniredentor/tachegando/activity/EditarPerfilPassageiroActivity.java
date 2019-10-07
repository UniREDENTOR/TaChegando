package br.edu.uniredentor.tachegando.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import br.edu.uniredentor.tachegando.R;

public class EditarPerfilPassageiroActivity extends FragmentActivity {

    private ImageView imageViewEditarFotoPerfil, imageViewFotoPerfil, imageViewTelefone, imageViewNome;
    private TextView textViewNomeEditarPerfil, textViewTelefoneEditarPerfil;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil_passageiro);

        imageViewNome.findViewById(R.id.imageView_nome_editarPerfil);
        imageViewTelefone.findViewById(R.id.imageView_telefone_editar_perfil);
        textViewNomeEditarPerfil.findViewById(R.id.textView_nome_passageiro_editar_perfil);
        textViewTelefoneEditarPerfil.findViewById(R.id.textView_telefone_passageiro_editar_perfil);
        imageViewFotoPerfil.findViewById(R.id.imageView_foto_passageiro_editar_perfil);
        imageViewEditarFotoPerfil.findViewById(R.id.imageView_foto_editar_passageiro);

        Toolbar toolbarEditarPerfil = findViewById(R.id.toolbar_principal);
        toolbarEditarPerfil.setTitle(R.string.editar);
    }
}

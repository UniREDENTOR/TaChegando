package br.edu.uniredentor.tachegando.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import br.edu.uniredentor.tachegando.R;

public class EditarPerfilPassageiroActivity extends FragmentActivity {

    private ImageView imageViewEditarFotoPerfil, imageViewFotoPerfil, imageViewTelefone, imageViewNome;
    private TextView textViewNomeEditarPerfil, textViewTelefoneEditarPerfil;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil_passageiro);

        imageViewEditarFotoPerfil = findViewById(R.id.imageView_edit_foto_perfil_passageiro);

        imageViewEditarFotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        Toolbar toolbarEditarPerfil = findViewById(R.id.toolbar_principal);
        toolbarEditarPerfil.setTitle(R.string.editar);
    }
}

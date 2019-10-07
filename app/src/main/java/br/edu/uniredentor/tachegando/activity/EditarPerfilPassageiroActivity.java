package br.edu.uniredentor.tachegando.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import br.edu.uniredentor.tachegando.R;

public class EditarPerfilPassageiroActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil_passageiro);

        Toolbar toolbarEditarPerfil = findViewById(R.id.toolbar_principal);
        toolbarEditarPerfil.setTitle(R.string.editar);
    }
}

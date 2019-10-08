package br.edu.uniredentor.tachegando.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import java.io.IOException;

import br.edu.uniredentor.tachegando.R;

public class EditarPerfilPassageiroActivity extends FragmentActivity {

    private ImageView imageViewBtnEditarFotoPerfil, imageViewFotoPerfil, imageViewTelefone, imageViewNome;
    private TextView textViewNomeEditarPerfil, textViewTelefoneEditarPerfil;
    private Uri fotoSelecionada;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil_passageiro);

        imageViewFotoPerfil = findViewById(R.id.imageView_foto_passsageiro_editar_perfil);
        imageViewBtnEditarFotoPerfil = findViewById(R.id.imageView_edit_foto_perfil_passageiro);

        imageViewBtnEditarFotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecionarFoto();
            }
        });



        Toolbar toolbarEditarPerfil = findViewById(R.id.toolbar_principal);
        toolbarEditarPerfil.setTitle(R.string.editar);
    }

    private void selecionarFoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            fotoSelecionada = data.getData();

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fotoSelecionada);
                imageViewFotoPerfil.setImageDrawable(new BitmapDrawable(bitmap));
                imageViewBtnEditarFotoPerfil.setAlpha(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

package br.edu.uniredentor.tachegando.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import br.edu.uniredentor.tachegando.R;
import br.edu.uniredentor.tachegando.utils.FirebaseUtils;
import br.edu.uniredentor.tachegando.utils.GeralUtils;

public class EditarPerfilPassageiroActivity extends FragmentActivity {

    private ImageView imageViewBtnEditarFotoPerfil, imageViewFotoPerfil, imageViewTelefone, imageViewNome;
    private TextView textViewNomeEditarPerfil, textViewTelefoneEditarPerfil;
    private Uri fotoSelecionada;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil_passageiro);

        Toolbar toolbarEditarPerfil = findViewById(R.id.toolbar_principal);
        toolbarEditarPerfil.setTitle(R.string.editar);

        imageViewFotoPerfil = findViewById(R.id.imageView_foto_passsageiro_editar_perfil);
        imageViewBtnEditarFotoPerfil = findViewById(R.id.imageView_edit_foto_perfil_passageiro);

        imageViewBtnEditarFotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecionarFoto();

            }
        });

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
                salvaEditarPerfil();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void salvaEditarPerfil() {
        FirebaseUtils.usuarioCadastrado();
        String filename = UUID.randomUUID().toString();
        final StorageReference ref = FirebaseStorage.getInstance().getReference("/images" + filename);
        ref.putFile(fotoSelecionada).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.i("Teste", uri.toString());
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                    Log.e("Teste", e.getMessage(), e);
            }
        });
    }
}

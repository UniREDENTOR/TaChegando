package br.edu.uniredentor.tachegando.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import br.edu.uniredentor.tachegando.R;
import br.edu.uniredentor.tachegando.model.Passageiro;
import br.edu.uniredentor.tachegando.utils.FirebaseUtils;

public class EditarPerfilPassageiroActivity extends FragmentActivity {

    private ImageView imageViewBtnEditarFotoPerfil, imageViewFotoPerfil;
    private EditText editTextNomeEditarPerfil;
    private CardView cardViewSolicitarTrocaTelefone;
    private FloatingActionButton floatingActionButtonEditarPerfil;

    private Uri fotoSelecionada;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil_passageiro);

        Toolbar toolbarEditarPerfil = findViewById(R.id.toolbar_principal);
        toolbarEditarPerfil.setTitle(R.string.editar);

        inicializandoComponente();

        imageViewBtnEditarFotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecionarFoto();

            }
        });

        floatingActionButtonEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PerfilPassageiroActivity.class);
                startActivity(i);
            }
        });

        cardViewSolicitarTrocaTelefone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(), "Abrir dialog", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }

    private void inicializandoComponente() {
        cardViewSolicitarTrocaTelefone = findViewById(R.id.cardview_telefone_editar_perfil);
        editTextNomeEditarPerfil = findViewById(R.id.editText_editar_nome_perfil);
        floatingActionButtonEditarPerfil = findViewById(R.id.floatingActionButton_editar_perfil);
        imageViewFotoPerfil = findViewById(R.id.imageView_foto_passsageiro_editar_perfil);
        imageViewBtnEditarFotoPerfil = findViewById(R.id.imageView_edit_foto_perfil_passageiro);

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
                        String id = FirebaseAuth.getInstance().getUid();
                        String foto = uri.toString();

                        Passageiro passageiro = new Passageiro(id, foto);

                        FirebaseUtils.getBanco().collection("users").document(passageiro.getId()).update(
                                "nome", passageiro.getNome(),
                                "foto", passageiro.getFoto()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });

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

package br.edu.uniredentor.tachegando.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.IOException;

import br.edu.uniredentor.tachegando.MapasActivity;
import br.edu.uniredentor.tachegando.R;
import br.edu.uniredentor.tachegando.model.Passageiro;
import br.edu.uniredentor.tachegando.utils.FirebaseUtils;
import br.edu.uniredentor.tachegando.utils.GeralUtils;

public class EditarPerfilPassageiroActivity extends FragmentActivity {

    private ImageView imageViewBtnEditarFotoPerfil, imageViewFotoPerfil, imageViewEditNomePerfil;
    private EditText editTextNomeEditarPerfil;
    private TextView textViewTelefoneEditarPerfil;
    private CardView cardViewSolicitarTrocaTelefone;
    private FloatingActionButton floatingActionButtonEditarPerfil;

    private String fotoSelecionada = "";
    private String nome = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil_passageiro);

        Toolbar toolbarEditarPerfil = findViewById(R.id.toolbar_principal);
        toolbarEditarPerfil.setTitle(R.string.editar);

        inicializandoComponente();
        recuperaEditarPerfilPassageiro();


        imageViewEditNomePerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nome = editTextNomeEditarPerfil.getText().toString().trim();
                GeralUtils.esconderTeclado(imageViewEditNomePerfil, getApplicationContext());
                Toast toast = Toast.makeText(getApplicationContext(), nome, Toast.LENGTH_SHORT);
                toast.show();
                imageViewEditNomePerfil.setImageResource(R.drawable.ic_edit_preto);

            }
        });

        imageViewBtnEditarFotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecionarFoto();
            }
        });

        floatingActionButtonEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!nome.isEmpty() && !fotoSelecionada.isEmpty()) {
                            FirebaseUtils.salvaEditarPerfil(Uri.parse(fotoSelecionada), nome);
                            Intent i = new Intent(getApplicationContext(), MapasActivity.class);
                            startActivity(i);
                            finishAffinity();
                } else if (nome.isEmpty()) {
                        verificaCampo();
                    } else{
                        Toast toast = Toast.makeText(getApplicationContext(), "Selecione uma foto" ,Toast.LENGTH_SHORT);
                        toast.show();

                    }
                } catch (Exception e) {
                    Log.w("tag", "Excessão");
                }
            }

        });

        cardViewSolicitarTrocaTelefone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(), "Abrir dialog", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        editTextNomeEditarPerfil.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                imageViewEditNomePerfil.setImageResource(R.drawable.ic_edit_preto);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                imageViewEditNomePerfil.setImageResource(R.drawable.ic_check_preto);

            }

        });
    }



    private void verificaCampo() {
        editTextNomeEditarPerfil.setError("Digite um nome");
        editTextNomeEditarPerfil.requestFocus();
        return;
    }
    private void inicializandoComponente() {
        cardViewSolicitarTrocaTelefone = findViewById(R.id.cardview_telefone_editar_perfil);
        editTextNomeEditarPerfil = findViewById(R.id.editText_editar_nome_perfil);
        floatingActionButtonEditarPerfil = findViewById(R.id.floatingActionButton_editar_perfil);
        imageViewFotoPerfil = findViewById(R.id.imageView_foto_passsageiro_editar_perfil);
        imageViewBtnEditarFotoPerfil = findViewById(R.id.imageView_edit_foto_perfil_passageiro);
        imageViewEditNomePerfil = findViewById(R.id.imageView_edit_nome_perfil);
        textViewTelefoneEditarPerfil = findViewById(R.id.textView_telefone_editar_perfil);
    }

    private void selecionarFoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    @Override
    public void onBackPressed() {

    }


    private void recuperaEditarPerfilPassageiro() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseUtils.getBanco().collection("users").document(user.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        Passageiro passageiro = documentSnapshot.toObject(Passageiro.class);
                        alteraInformacaoEditarPerfil(passageiro);
                    } else {
                        Log.d("", "Não existe");
                    }
                }
            });
        } else {

        }
    }

    private void alteraInformacaoEditarPerfil(Passageiro passageiro){
        textViewTelefoneEditarPerfil.setText(passageiro.getTelefone());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
           fotoSelecionada = String.valueOf(data.getData());
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(fotoSelecionada));
                    imageViewFotoPerfil.setImageDrawable(new BitmapDrawable(bitmap));
                    imageViewBtnEditarFotoPerfil.setAlpha(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


}

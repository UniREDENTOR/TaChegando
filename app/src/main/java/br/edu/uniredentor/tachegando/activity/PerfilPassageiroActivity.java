package br.edu.uniredentor.tachegando.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import br.edu.uniredentor.tachegando.R;
import br.edu.uniredentor.tachegando.model.Passageiro;
import br.edu.uniredentor.tachegando.utils.FirebaseUtils;


public class PerfilPassageiroActivity extends AppCompatActivity {

    private TextView textViewNomePassageiro, textViewCorridaPassageiro;
    private TextView textViewReputacaoPassageiro;
    private ImageView imagemPassageiro, imagemCorrida, imagemEmblema;

    FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_passageiro);

        getSupportActionBar().setTitle("Perfil");
        getSupportActionBar().setElevation(0);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#6A5ACD")));

        textViewNomePassageiro = findViewById(R.id.textView_nome_perfil);
        textViewCorridaPassageiro = findViewById(R.id.textView_qtd_corrida);
        textViewReputacaoPassageiro = findViewById(R.id.textView_reputacao_passageiro);
        imagemPassageiro = findViewById(R.id.imageView_foto_perfil_passageiro);
        imagemCorrida = findViewById(R.id.imageView_qtd_corrida);
        imagemEmblema = findViewById(R.id.imageView_emblema_perfil);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseUtils.getBanco().collection("users").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Passageiro passageiro = documentSnapshot.toObject(Passageiro.class);
                    String id = passageiro.getId();
                    textViewNomePassageiro.setText(passageiro.getId());
                    Toast toast = Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }
        else {
            // No user is signed in
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_perfil_passageiro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_sair_app:
                FirebaseUtils.signOut();
                break;
            case R.id.editar_perfil:
                Intent i = new Intent(this, EditarPerfilPassageiroActivity.class);
                startActivity(i);
        }
        return true;
    }

}
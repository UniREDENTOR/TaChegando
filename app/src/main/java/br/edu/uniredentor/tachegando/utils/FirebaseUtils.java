package br.edu.uniredentor.tachegando.utils;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import br.edu.uniredentor.tachegando.model.MensagemChat;
import br.edu.uniredentor.tachegando.model.Passageiro;
import br.edu.uniredentor.tachegando.model.Viagem;


public class FirebaseUtils extends AppCompatActivity {

    private static FirebaseAuth auth;

    public static String salvaViagem(Viagem viagem) {
        DocumentReference reference = getBanco().collection("viagens")
                .document(viagem.getIdUsuario());
        if (viagem.getId().isEmpty()) {
            String id = getBanco().collection("viagens")
                    .document().getId();
            viagem.setId(id);
            reference.set(viagem.getInicialMap());
        } else {
            reference.set(viagem.getLocalizacao());
        }

        return viagem.getId();
    }

    private static void salvaHistorico(Viagem viagem) {
        getBanco().collection("historico").document(viagem.getId()).collection(viagem.getIdUsuario()).add(viagem.getLocalizacao());
    }

    public static void getViagem(String id) {
        getBanco().collection("viagens").document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (snapshot != null && snapshot.exists()) {
                    Log.d("", "Current data: " + snapshot);
                    GeralUtils.show("" + snapshot);
                } else {
                    Log.d("", "Current data: null");
                }
            }
        });
    }

    public static void getViagens() {
        getBanco().collection("viagens").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                GeralUtils.show("" + queryDocumentSnapshots.getDocuments());


            }
        });
    }

    public static void denuncia(Passageiro passageiroCriador, String idUsuario) {
        HashMap<String, Object> map = passageiroCriador.getMap();
        map.put("idDenuncia", idUsuario);
        getBanco().collection("denuncias").document().set(map);
    }

    public static void salvaLocal(ArrayList<LatLng> locais) {
        DocumentReference reference = getBanco().collection("historico").document();
        reference.set(locais);
    }

    public static void atualizaId(Viagem viagem) {
        getBanco().collection("viagens")
                .document(viagem.getIdUsuario()).update(viagem.getIdMap());
    }

    public static void atualizaLocalizacao(Viagem viagem) {
        getBanco().collection("viagens")
                .document(viagem.getIdUsuario()).update(viagem.getLocalizacao());
        salvaHistorico(viagem);
    }

    public static void salvaMensagem(MensagemChat mensagemChat) {
        getBanco().collection("chats").document(mensagemChat.getIdViagem()).collection("conversas").add(mensagemChat.getMap());
    }


    public static FirebaseFirestore getBanco() {
        return FirebaseFirestore.getInstance();
    }

    public static FirebaseAuth getAuth() {
        if (auth == null) {
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }


    public static FirebaseAuth signOut() {
        FirebaseAuth.getInstance().signOut();
        Log.w("SAIR", "Usuário saiu da Sessão");
        return auth;
    }


    public static void salvaUsuario(Passageiro passageiro) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();

        FirebaseUtils.getBanco().collection("users").document(id).set(passageiro.getUser()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }

    public static boolean usuarioCadastrado() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public static String getIdUsuario() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}

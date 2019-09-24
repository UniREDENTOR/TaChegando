package br.edu.uniredentor.tachegando.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import br.edu.uniredentor.tachegando.model.Viagem;


public class FirebaseUtils {

    public static void salva(Viagem viagem) {
        Map<String, Object> viagemMap = new HashMap<>();
        viagemMap.put("id", viagem.getId());
        viagemMap.put("idUsuario", viagem.getIdUsuario());
        viagemMap.put("nome", viagem.getNome());
        viagemMap.put("latitude", viagem.getLatitude());
        viagemMap.put("longitude", viagem.getLongitude());

        getBanco().collection("viagens")
                .document(viagem.getIdUsuario())
                .set(viagemMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }

    public static void getViagem(String id){
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

    public static FirebaseFirestore getBanco() {
        return FirebaseFirestore.getInstance();
    }

    public static void getViagens() {
        getBanco().collection("viagens").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                GeralUtils.show("" + queryDocumentSnapshots.getDocuments());
            }
        });
    }
}

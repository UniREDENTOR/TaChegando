package br.edu.uniredentor.tachegando.utils;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.edu.uniredentor.tachegando.model.Passageiro;
import br.edu.uniredentor.tachegando.model.Viagem;


public class FirebaseUtils {



    public static String salva(Viagem viagem) {
        DocumentReference reference = getBanco().collection("viagens")
                .document(viagem.getIdUsuario());
        if(viagem.getId().isEmpty()){
            String id= getBanco().collection("viagens")
                    .document().getId();
             viagem.setId(id);
             reference.set(viagem.getInicialMap());
        }else{
            reference.set(viagem.getLocalizacao());
        }

        return viagem.getId();
    }


    private static void salvaHistorico(Viagem viagem) {
        getBanco().collection("historico").document(viagem.getId()).collection(viagem.getIdUsuario()).add(viagem.getLocalizacao());
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


    public static void denuncia(Passageiro passageiroCriador, String idUsuario) {
        HashMap<String, Object> map = passageiroCriador.getMap();
        map.put("idDenuncia", idUsuario);
        getBanco().collection("denuncias").document().set(map);
    }

    public static void salva(ArrayList<LatLng> locais) {
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


}

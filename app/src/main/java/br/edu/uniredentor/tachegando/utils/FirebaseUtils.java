package br.edu.uniredentor.tachegando.utils;


import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.edu.uniredentor.tachegando.model.Denuncia;
import br.edu.uniredentor.tachegando.model.MensagemChat;
import br.edu.uniredentor.tachegando.model.Passageiro;
import br.edu.uniredentor.tachegando.model.Viagem;


public class FirebaseUtils extends AppCompatActivity {

    private static FirebaseAuth auth;

    public static void salvaViagem(final Viagem viagem) {
        final DocumentReference reference = getBanco().collection("viagens")
                .document(viagem.getId());
        getBanco().collection("users").document(viagem.getId()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    viagem.addPassageiro(documentSnapshot.toObject(Passageiro.class));
                    reference.set(viagem.getInicialMap());
                }
            }
        });
    }

    public static DocumentReference getViagem(String id) {
        return getBanco().collection("viagens")
                .document(id);
    }

    public static CollectionReference getViagemRealizadas() {
        return getBanco().collection("viagens_realizadas");
    }

    public static CollectionReference getViagens() {
        return getBanco().collection("viagens");
    }

    public static CollectionReference getUsers() {
        return getBanco().collection("users");
    }

    public static FirebaseFirestore getBanco() {
        return FirebaseFirestore.getInstance();
    }

    public static CollectionReference getConversas(String idViagem) {
        return FirebaseFirestore.getInstance().collection("viagens").document(idViagem).collection("conversas");
    }

    public static FirebaseAuth signOut() {
        FirebaseAuth.getInstance().signOut();
        return auth;
    }

    public static void salvaUsuario(final Passageiro passageiro) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();

        FirebaseUtils.getBanco().collection("users").document(id).set(passageiro.retornaUser()).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public static FirebaseUser getUser() {
        return FirebaseAuth.getInstance().getCurrentUser();

    }

    public static void removePassageiro(Viagem viagem, String passageiroId) {
        viagem.removePassageiro(passageiroId);
        List<Passageiro> passageiros = viagem.getPassageiros();

        if (passageiros.size() == 0) {
            //Apagar todos os dados da viagem
            getViagemRealizadas().add(viagem);
            getViagem(viagem.getId()).delete();
        } else {
            if(viagem.getId().equals(passageiroId)){
                String proximoId = atualizaViagemComProximoIdViagem(viagem, passageiros);
                viagem.setAtiva(true);
                viagem.setId(proximoId);
                getViagem(proximoId).set(viagem.getInicialMap());
                getViagem(viagem.getId()).delete();
            }else{
                viagem.removePassageiro(passageiroId);
                HashMap<String, Object> map = new HashMap<>();
                map.put("passageiros", passageiros);
                getViagem(viagem.getId()).update(map);
            }
        }
    }

    private static String atualizaViagemComProximoIdViagem(Viagem viagem, List<Passageiro> passageiros) {
        String proximoId = viagem.getPassageiros().get(0).getId();
        viagem.setProximoIdDaViagem(proximoId);
        viagem.setAtiva(false);
        HashMap<String, Object> map = new HashMap<>();
        map.put("passageiros", passageiros);
        map.put("proximoIdViagem", proximoId);
        map.put("ativa", false);
        getViagem(viagem.getId()).update(map);
        return proximoId;
    }


    public static void denuncia(Viagem viagem, Denuncia denuncia) {
        HashMap<String, Object> map = new HashMap<>();
        List<Denuncia> denuncias = viagem.getDenuncias();
        denuncias.add(denuncia);
        map.put("denuncias", denuncias);
        getViagem(viagem.getId()).update(map);
    }

    public static void adicionaPassageiro(String id, final Viagem viagem) {

        getBanco().collection("users").document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    HashMap<String, Object> map = new HashMap<>();
                    List<Passageiro> passageiros = viagem.getPassageiros();
                    Passageiro passageiro = documentSnapshot.toObject(Passageiro.class);
                    if(!passageiros.contains(passageiro)){
                        passageiros.add(passageiro);
                        map.put("passageiros", passageiros);
                        getViagem(viagem.getId()).update(map);
                        Singleton.getInstance().setIdViagem(viagem.getId());
                    }

                }
            }

        });
    }

    public static void atualizaLocalizacao(String id, Location location) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("latitude", location.getLatitude());
        map.put("longitude", location.getLongitude());
        getViagem(id).update(map);
        getViagem(id).collection("trajeto").add(map);
    }
}
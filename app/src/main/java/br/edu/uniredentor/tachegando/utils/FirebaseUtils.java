package br.edu.uniredentor.tachegando.utils;


import android.location.Location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import br.edu.uniredentor.tachegando.fragments.InformacaoOnibusDialogFragment;
import br.edu.uniredentor.tachegando.model.Denuncia;
import br.edu.uniredentor.tachegando.model.Passageiro;
import br.edu.uniredentor.tachegando.model.Viagem;


public class FirebaseUtils extends AppCompatActivity {

    public static void salvaViagem(final Viagem viagem) {
        removeViagem(viagem, null);

        final DocumentReference reference = getBanco().collection(ConstantsUtils.VIAGENS)
                .document(viagem.getId());
        getBanco().collection(ConstantsUtils.USERS).document(viagem.getId()).addSnapshotListener((documentSnapshot, e) -> {
            if (documentSnapshot.exists()) {
                viagem.addPassageiro(documentSnapshot.toObject(Passageiro.class));
                reference.set(viagem.getInicialMap());
            }
        });

    }

    private static void removeTrajeto(String id) {
        getTrajeto(id).get().addOnCompleteListener(task -> remove(task.getResult()));
    }

    private static void removeConversas(String id) {
        getConversas(id).get().addOnCompleteListener(task -> remove(task.getResult()));
    }

    public static DocumentReference getViagem(String id) {
        return getBanco().collection(ConstantsUtils.VIAGENS)
                .document(id);
    }

    public static CollectionReference getViagemRealizadas() {
        return getBanco().collection(ConstantsUtils.VIAGENS_REALIZADAS);
    }

    public static CollectionReference getViagens() {
        return getBanco().collection(ConstantsUtils.VIAGENS);
    }

    public static CollectionReference getUsers() {
        return getBanco().collection(ConstantsUtils.USERS);
    }

    public static FirebaseFirestore getBanco() {
        return FirebaseFirestore.getInstance();
    }

    public static CollectionReference getConversas(String idViagem) {
        return FirebaseFirestore.getInstance().collection(ConstantsUtils.VIAGENS).document(idViagem).collection(ConstantsUtils.CONVERSAS);
    }
    public static CollectionReference getTrajeto(String idViagem) {
        return FirebaseFirestore.getInstance().collection(ConstantsUtils.VIAGENS).document(idViagem).collection(ConstantsUtils.TRAJETO);
    }


    public static void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    public static void salvaUsuario(final Passageiro passageiro) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();

        FirebaseUtils.getBanco().collection(ConstantsUtils.USERS).document(id).set(passageiro.retornaUser());
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

    private static void removeViagem(Viagem viagem, InformacaoOnibusDialogFragment.RemoveMarker removeMarkerListener){
        getViagem(viagem.getId()).delete().addOnCompleteListener(task -> {
            if(removeMarkerListener != null){
                removeMarkerListener.remove(viagem);
            }

        });
        getViagem(viagem.getId()).collection(ConstantsUtils.CONVERSAS).addSnapshotListener((queryDocumentSnapshots, e) -> {
            remove(queryDocumentSnapshots);

        });

        getViagem(viagem.getId()).collection(ConstantsUtils.TRAJETO).addSnapshotListener( (queryDocumentSnapshots, e) -> {
            remove(queryDocumentSnapshots);
        });
    }

    public static void removePassageiro(Viagem viagem, String passageiroId, InformacaoOnibusDialogFragment.RemoveMarker removeMarkerListener) {
        viagem.removePassageiro(passageiroId);
        List<Passageiro> passageiros = viagem.getPassageiros();

        if (passageiros.size() == 0) {
            //Apagar todos os dados da viagem
            getViagemRealizadas().add(viagem);
           removeViagem(viagem, removeMarkerListener);
        } else {
            if(viagem.getId().equals(passageiroId)){
                String proximoId = atualizaViagemComProximoIdViagem(viagem, passageiros);
                viagem.setAtiva(true);
                viagem.setId(proximoId);
                getViagem(proximoId).set(viagem.getInicialMap());
                getViagem(passageiroId).delete();
            }else{
                viagem.removePassageiro(passageiroId);
                HashMap<String, Object> map = new HashMap<>();
                map.put(ConstantsUtils.PASSAGEIROS, passageiros);
                getViagem(viagem.getId()).update(map);
            }
        }
    }

    private static void remove(QuerySnapshot queryDocumentSnapshots) {
        try{
            for(DocumentSnapshot document: queryDocumentSnapshots.getDocuments()){
                document.getReference().delete();
            }
        }catch (Exception e1){
            e1.printStackTrace();
        }
    }

    private static String atualizaViagemComProximoIdViagem(Viagem viagem, List<Passageiro> passageiros) {
        String proximoId = viagem.getPassageiros().get(0).getId();
        viagem.setProximoIdDaViagem(proximoId);
        viagem.setAtiva(false);
        HashMap<String, Object> map = new HashMap<>();
        map.put(ConstantsUtils.PASSAGEIROS, passageiros);
        map.put(ConstantsUtils.PROXIMO_ID_VIAGEM, proximoId);
        map.put(ConstantsUtils.ATIVA, false);
        getViagem(viagem.getId()).update(map);
        return proximoId;
    }


    public static void denuncia(Viagem viagem, Denuncia denuncia) {
        HashMap<String, Object> map = new HashMap<>();
        List<Denuncia> denuncias = viagem.getDenuncias();
        denuncias.add(denuncia);
        map.put(ConstantsUtils.DENUNCIAS, denuncias);
        getViagem(viagem.getId()).update(map);
    }

    public static void adicionaPassageiro(String id, final Viagem viagem) {

        getBanco().collection(ConstantsUtils.USERS).document(id).addSnapshotListener((documentSnapshot, e) -> {
            if (documentSnapshot.exists()) {
                HashMap<String, Object> map = new HashMap<>();
                List<Passageiro> passageiros = viagem.getPassageiros();
                Passageiro passageiro = documentSnapshot.toObject(Passageiro.class);
                if(!passageiros.contains(passageiro)){
                    passageiros.add(passageiro);
                    map.put(ConstantsUtils.PASSAGEIROS, passageiros);
                    getViagem(viagem.getId()).update(map);
                }
            }
        });
    }

    public static void atualizaLocalizacao(String id, Location location) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(ConstantsUtils.LATITUDE, location.getLatitude());
        map.put(ConstantsUtils.LONGITUDE, location.getLongitude());
        getViagem(id).update(map);
        getViagem(id).collection(ConstantsUtils.TRAJETO).add(map);
    }
}
package br.edu.uniredentor.tachegando.utils;


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
                .document(GeralUtils.getIdDoUsuario());
        getBanco().collection("users").document(GeralUtils.getIdDoUsuario()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    viagem.addPassageiro(documentSnapshot.toObject(Passageiro.class));
                    reference.set(viagem.getInicialMap());
                }
            }
        });
    }

    public static DocumentReference getViagem(String id){
        return getBanco().collection("viagens")
                .document(id);
    }

    private static void salvaHistorico(Viagem viagem) {
        getBanco().collection("historico").document(viagem.getId()).collection(viagem.getId()).add(viagem.getLocalizacao());
    }


    public static CollectionReference getViagens() {
        return getBanco().collection("viagens");
    }

    public static CollectionReference getUsers() {
        return getBanco().collection("users");
    }

    public static CollectionReference getHistorico() {
        return getBanco().collection("historico");
    }

    public static CollectionReference getPontos() {
        return getBanco().collection("pontos");
    }

    public static void salvaLocal(ArrayList<LatLng> locais) {
        DocumentReference reference = getBanco().collection("historico").document();
        reference.set(locais);
    }

    public static void atualizaLocalizacao(Viagem viagem) {
        getBanco().collection("viagens")
                .document(viagem.getId()).update(viagem.getLocalizacao());
      //  salvaHistorico(viagem);
    }

    public static void salvaMensagem(MensagemChat mensagemChat) {
       // getBanco().collection("chats").document(mensagemChat.getIdViagem()).collection("conversas").add(mensagemChat.getMap());
    }


    public static FirebaseFirestore getBanco() {
        return FirebaseFirestore.getInstance();
    }

    public static CollectionReference getConversas(String idViagem) {
        return FirebaseFirestore.getInstance().collection("viagens").document(idViagem).collection("conversas");
    }

    public static FirebaseAuth getAuth() {
        if (auth == null) {
            auth = FirebaseAuth.getInstance();
        }
        return auth;
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

    public static void removePassageiro(Viagem viagem) {
        HashMap<String, Object> map = new HashMap<>();
        List<Passageiro> passageiros = viagem.getPassageiros();
        map.put("passageiros", passageiros);
        getViagem(viagem.getId()).update(map);
    }

    public static void deletaTudo() {
        final CollectionReference ref = getViagens();
        ref.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for(DocumentSnapshot d : queryDocumentSnapshots.getDocuments()){
                    ref.document(d.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }
            }
        });

        final CollectionReference refUsers = getUsers();
        refUsers.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for(DocumentSnapshot d : queryDocumentSnapshots.getDocuments()){
                    refUsers.document(d.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }
            }
        });

        final CollectionReference refHistorico = getHistorico();
        refHistorico.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for(DocumentSnapshot d : queryDocumentSnapshots.getDocuments()){
                    refHistorico.document(d.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }
            }
        });

        final CollectionReference refPontos = getPontos();
        refPontos.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for(DocumentSnapshot d : queryDocumentSnapshots.getDocuments()){
                    refPontos.document(d.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }
            }
        });
    }

    public static void denuncia(Viagem viagem, Denuncia denuncia) {
        HashMap<String, Object> map = new HashMap<>();
        List<Denuncia> denuncias = viagem.getDenuncias();
        denuncias.add(denuncia);
        map.put("denuncias", denuncias);
        getViagem(viagem.getId()).update(map);
    }
}

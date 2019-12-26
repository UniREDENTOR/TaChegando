package br.edu.uniredentor.tachegando.utils;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

import br.edu.uniredentor.tachegando.model.MensagemChat;
import br.edu.uniredentor.tachegando.model.Passageiro;
import br.edu.uniredentor.tachegando.model.Viagem;


public class FirebaseUtils extends AppCompatActivity {

    private static FirebaseAuth auth;

    public static void salvaViagem(Viagem viagem) {
        DocumentReference reference = getBanco().collection("viagens")
                .document(GeralUtils.getIdDoUsuario());
        if (!viagem.getId().isEmpty()) {
            reference.set(viagem.getInicialMap());
        } else {
            reference.set(viagem.getLocalizacao());
        }
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
        salvaHistorico(viagem);
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

    public static void salvaEditarPerfil(Uri fotoSelecionada, final String nome) {
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

                        Passageiro passageiro = new Passageiro(id, foto, nome);
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
        return;
    }

    public static FirebaseUser getUser() {
        return FirebaseAuth.getInstance().getCurrentUser();

    }

    public static void removePassageiro(Viagem viagem) {

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
                            GeralUtils.mostraLog("Deu certo " + task);
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
                            GeralUtils.mostraLog("Deu certo " + task);
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
                            GeralUtils.mostraLog("Deu certo " + task);
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
                            GeralUtils.mostraLog("Deu certo " + task);
                        }
                    });
                }
            }
        });
    }
}

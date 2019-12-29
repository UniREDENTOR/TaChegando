package br.edu.uniredentor.tachegando.livedata;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import br.edu.uniredentor.tachegando.utils.FirebaseUtils;
import br.edu.uniredentor.tachegando.utils.GeralUtils;

public class FirebaseLiveDataPassageiro extends LiveData<DocumentSnapshot> {
    private final MyValueEventListener listener = new MyValueEventListener();
    private String id = "";


    @Override
    protected void onActive(){
        GeralUtils.show("onActive");
        if(!id.isEmpty()){
            FirebaseUtils.getViagem(id).addSnapshotListener(listener);

        }
    }

    @Override
    protected void onInactive(){
        GeralUtils.show("onInactive");
    }

    public LiveData<DocumentSnapshot> setId(String id) {
        this.id = id;
        return this;
    }

    private class MyValueEventListener implements EventListener<DocumentSnapshot> {


        @Override
        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
            setValue(documentSnapshot);
        }
    }
}

package br.edu.uniredentor.tachegando.livedata;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import br.edu.uniredentor.tachegando.utils.FirebaseUtils;

public class FirebaseLiveDataPassageiro extends LiveData<DocumentSnapshot> {
    private static final String Log_tag = "FirebaseQueryLiveData";
    private final MyValueEventListener listener = new MyValueEventListener();



    @Override
    protected void onActive(){
        Log.d(Log_tag, "onActive");
        FirebaseUtils.getViagem("SMbIGq2uU6avlXBIUNz6OGTiXs43").addSnapshotListener(listener);
    }

    @Override
    protected void onInactive(){
        Log.d(Log_tag, "onInactive");
    }

    private class MyValueEventListener implements EventListener<DocumentSnapshot> {


        @Override
        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
            setValue(documentSnapshot);
        }
    }
}

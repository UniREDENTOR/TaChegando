package br.edu.uniredentor.tachegando.livedata;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import br.edu.uniredentor.tachegando.utils.FirebaseUtils;
import br.edu.uniredentor.tachegando.utils.GeralUtils;

public class FirebaseLiveDataViagens extends LiveData<QuerySnapshot> {

    private QueryListenerViagens listener = new QueryListenerViagens();

    @Override
    protected void onActive() {
        super.onActive();
        FirebaseUtils.getViagens().addSnapshotListener(listener);
    }

    private class QueryListenerViagens implements EventListener<QuerySnapshot>{

        @Override
        public void onEvent(@Nullable QuerySnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
            setValue(documentSnapshot);
        }
    }
}

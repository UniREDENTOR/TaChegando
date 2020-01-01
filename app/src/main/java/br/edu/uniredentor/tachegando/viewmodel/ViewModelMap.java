package br.edu.uniredentor.tachegando.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import br.edu.uniredentor.tachegando.livedata.FirebaseLiveDataPassageiro;
import br.edu.uniredentor.tachegando.livedata.FirebaseLiveDataViagens;

public class ViewModelMap extends ViewModel {

    private final FirebaseLiveDataPassageiro liveData = new FirebaseLiveDataPassageiro();
    private final FirebaseLiveDataViagens liveDataViagens = new FirebaseLiveDataViagens();

    @NonNull
    public LiveData<DocumentSnapshot> getDataSnapshotLiveData(String id){
        return liveData.setId(id);
    }

    public LiveData<QuerySnapshot> getQuerySnapshotLiveDataViagens(){
        return liveDataViagens;
    }

}
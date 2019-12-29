package br.edu.uniredentor.tachegando.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;

import br.edu.uniredentor.tachegando.livedata.FirebaseLiveDataPassageiro;

public class ViewModelPassageiro extends ViewModel {

    private final FirebaseLiveDataPassageiro liveData = new FirebaseLiveDataPassageiro();

    @NonNull
    public LiveData<DocumentSnapshot> getdataSnapshotLiveData(){
        return liveData;
    }

}
package com.hsappdev.ahs.cache_new;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public abstract class DataLoaderBackEnd<T> {
    private volatile boolean localLoaded = false;
    private volatile boolean isDataInLocal = false;
    private volatile boolean firebaseLoaded = false;

    private String dataID;
    public DataLoaderBackEnd(String dataID) {
        this.dataID = dataID;

    }

    protected void startDatabaseLoad() {
        loadFromLocalDatabase();
        loadFromFirebaseDatabase();
    }

    private final MutableLiveData<DataWithSource<T>> mutableLiveData = new MutableLiveData<>();
    public LiveData<DataWithSource<T>> getLiveData() {
        return mutableLiveData;
    }

    private void loadFromLocalDatabase() {
        LiveData<T> localData = getLocalData(dataID);
        localData.observeForever(new Observer<T>() {
            @Override
            public void onChanged(T t) {
                localLoaded = true;
                isDataInLocal = (t != null);
                if (t != null) {
                    if(!firebaseLoaded) {
                        mutableLiveData.setValue(new DataWithSource<T>(t, false));
                    } else {
                        DataWithSource<T> firebaseData = mutableLiveData.getValue();
                        updateFirebaseData_withLocalData(firebaseData.getData(), t);
                        mutableLiveData.setValue(firebaseData);
                    }
                }
                localData.removeObserver(this);
            }
        });
    }


    private void loadFromFirebaseDatabase() {
        getFirebaseRef(dataID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                firebaseLoaded = true;
                T data = getData_fromDataSnapshot(snapshot, dataID);
                if(!localLoaded || localLoaded && !isDataInLocal) {
                    updateFirebaseData_withDefaultLocalAttrs(data);
                }
                mutableLiveData.setValue(new DataWithSource<T>(data, true));
                updateLocalDatabase(data);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    protected abstract LiveData<T> getLocalData(String dataID);
    protected abstract DatabaseReference getFirebaseRef(String dataID);

    protected abstract T getData_fromDataSnapshot(DataSnapshot snapshot, String dataID);
    protected abstract void updateFirebaseData_withLocalData(T firebaseData, T localData);
    protected abstract void updateFirebaseData_withDefaultLocalAttrs(T firebaseData);

    protected abstract void updateLocalDatabase(T data);

    public static class DataWithSource<T> {
        private T data;
        private boolean fromFirebase;

        public DataWithSource(T data, boolean fromFirebase) {
            this.data = data;
            this.fromFirebase = fromFirebase;
        }

        public T getData() {
            return data;
        }

        public boolean isFromFirebase() {
            return fromFirebase;
        }
    }
}

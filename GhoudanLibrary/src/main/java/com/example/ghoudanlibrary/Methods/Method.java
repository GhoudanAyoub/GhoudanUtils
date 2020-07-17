package com.example.ghoudanlibrary.Methods;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ghoudanlibrary.API.FireBaseClient;
import com.google.android.gms.common.internal.Objects;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Method<T> {
    
    private static final String TAG = "OnCompleteCalled";
    private static final String TAG2 = "onCancelledCalled";
    public MutableLiveData<Objects> mutableLiveData = new MutableLiveData<>();
    public MutableLiveData<List<Objects>> listMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Objects> mutableLiveDataFireStore = new MutableLiveData<>();
    public MutableLiveData<List<Objects>> listMutableLiveDataFireStore = new MutableLiveData<>();

    /*
     *This Section is All About Adding Data To DataBases Tables
     *Using General Object And TablesNames There A Possibility To
     *Replace Object With general object T
     *@param obj The current Object To manipulate
     *@param TableName The DataBase Tables Name
     *@param DocumentId The FireBaseFireStore Document Id
     *
     * Both Method Will be triggered in the event that this listener either failed at the server
     * or Successes Or Completed
     */

    public void AddDataToRunTimeDb(Object obj, String TableName) {
        FireBaseClient.getFireBaseClient().getDatabaseReference()
                .child(TableName)
                .push()
                .setValue(obj)
                .addOnCompleteListener(task -> Log.i(TAG, "AddDemandToRunTimeDb: " + task.getException()))
                .addOnSuccessListener(Object::notifyAll)
                .addOnFailureListener(Throwable::printStackTrace);
    }

    public void AddDataToFirebaseFireStore(Object obj, String TableName, String DocumentId) {
        FireBaseClient.getFireBaseClient().getFirebaseFirestore()
                .collection(TableName)
                .document(DocumentId)
                .set(obj)
                .addOnCompleteListener(task -> Log.i(TAG, "AddDataToFirebaseFireStore: " + task.getException()))
                .addOnSuccessListener(Object::notifyAll)
                .addOnFailureListener(Throwable::printStackTrace);
    }

    /*
    This Section is All About Getting Data From DataBases Tables
    Using General Object And TablesNames There A Possibility To
    Replace Object With general object T
     */

    /*
     * This method will be called with a Objects of the data at this location. It will also be called
     * each time that data changes using The Base Method Of FirebaseRunTimeDataBase
     * It Return a Type Of Live Data Objects Stored In MutableLiveData
     *@param TableName The DataBase Tables Name
     *@param ObjectId To lookFor The specific Column in The Table
     *@param ObjectIdLookFor To identify The Specific Object LookingFor
     */
    public LiveData<Objects> GetSingleObjectFromRunTimeDb(String TableName, String ObjectId, String ObjectIdLookFor) {
        FireBaseClient.getFireBaseClient().getDatabaseReference()
                .child(TableName)
                .orderByChild(ObjectId).
                equalTo(ObjectIdLookFor)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Objects objects = null;
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                objects = dataSnapshot1.getValue(Objects.class);
                            }
                        }
                        mutableLiveData.setValue(objects);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w(TAG2, "onCancelled: " + databaseError.getMessage());
                    }
                });
        return mutableLiveData;
    }

    public LiveData<List<Objects>> GetListOfObjectFromRunTimeDb(String TableName) {
        List<Objects> objectsList = new ArrayList<>();

        FireBaseClient.getFireBaseClient().getFirebaseDatabase()
                .getReference(TableName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Objects objects = null;
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                try {
                                    objects = dataSnapshot1.getValue(Objects.class);
                                    objectsList.add(objects);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            listMutableLiveData.setValue(objectsList);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w(TAG2, "onCancelled: " + error.getMessage());
                    }
                });
        return listMutableLiveData;

    }


    /*
     * This method will be called with a Objects of the data at this location. It will also be called
     * each time that data changes using The Base Method Of FirebaseFireStore
     * It Return a Type Of  Live Data List Of Objects Stored In MutableLiveData
     *@param obj The current Object To manipulate
     *@param TableName The DataBase Tables Name
     */
    public LiveData<Objects> GetSingleObjectFromFirebaseFireStore(Objects obj, String TableName) {
        GetListOfObjectFromFirebaseFireStore(TableName).observe((LifecycleOwner) this, objects -> {
            for (Objects objects1 : objects) {
                if (objects1.equals(obj)) {
                    mutableLiveDataFireStore.setValue(objects1);
                }
            }
        });
        return mutableLiveDataFireStore;
    }

    public LiveData<List<Objects>> GetListOfObjectFromFirebaseFireStore(String TableName) {
        List<Objects> objectsList = new ArrayList<Objects>();

        FireBaseClient.getFireBaseClient()
                .getFirebaseFirestore()
                .collection(TableName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            try {
                                objectsList.add(document.toObject(Objects.class));
                            } catch (Throwable t) {
                                t.printStackTrace();
                            }
                        }
                        listMutableLiveDataFireStore.setValue(objectsList);
                    }
                });
        return listMutableLiveDataFireStore;
    }
}

package com.group1.swepproject.user.nochange.models;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group1.swepproject.user.nochange.data.Constant;

import java.util.ArrayList;

public class AppViewModel extends AndroidViewModel {

    public AppViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<ArrayList<Payment>> getPayment(String type, String searchWord) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        MutableLiveData<ArrayList<Payment>> response = new MutableLiveData<>();

        firestore.collection(Constant.USER_COLLECTION).document(FirebaseAuth.getInstance()
                .getCurrentUser().getUid()).collection(Constant.PAYMENT_COLLECTION)
                .orderBy("timestamp")
                .whereEqualTo("type", type)
                .whereGreaterThanOrEqualTo("name", searchWord).get()
                .addOnSuccessListener(res -> {
                    response.postValue((ArrayList<Payment>) res.toObjects(Payment.class));
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    Log.i("Could not fetch data", "Could not fetch data");
                    response.postValue(null);

                });
        return response;
    }

    public LiveData<ArrayList<Payment>> getAllPayment(String type, String searchWord) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        MutableLiveData<ArrayList<Payment>> response = new MutableLiveData<>();

        firestore.collection(Constant.USER_COLLECTION).document(FirebaseAuth.getInstance()
                .getCurrentUser().getUid()).collection(Constant.PAYMENT_COLLECTION)
                .orderBy("timestamp")
                .whereEqualTo("type", type)
                .whereGreaterThanOrEqualTo("name", searchWord).get()
                .addOnSuccessListener(res -> {
                    response.postValue((ArrayList<Payment>) res.toObjects(Payment.class));
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    Log.i("Could not fetch data", "Could not fetch data");
                    response.postValue(null);

                });
        return response;


    }
}

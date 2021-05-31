package com.example.parkinglotapp;

import android.app.Application;
import android.app.Person;

import com.example.parkinglotapp.singleton.SingleTon;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;

public class MyApplication extends Application {
    public static MyApplication instance;
    private FirebaseAuth mAuth;
    private SingleTon singleTon;
    @Override
    public void onCreate() {
        super.onCreate();
        mAuth=FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null){
            singleTon=SingleTon.getInstance();
        }
        instance = this;
    }

    public static MyApplication getInstance() {
        return instance;
    }

}

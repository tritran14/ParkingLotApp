package com.example.parkinglotapp.singleton;

import androidx.annotation.NonNull;

import com.example.parkinglotapp.model.Person;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class SingleTon {
    private static SingleTon instance=null;
    private FirebaseFirestore firebaseFirestore;
    private Person person;
    private PublishSubject<Boolean> isReady = PublishSubject.create();
    private PublishSubject<Boolean> needUpdate=PublishSubject.create();
    public Observable<Boolean> getFragmentEventObservable() {
        return isReady;
    }
    public Observable<Boolean> getUpdateObservable() {
        return needUpdate;
    }
    private SingleTon(){
        isReady.onNext(false);
        needUpdate.onNext(false);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Users")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot doc=task.getResult();
                            if(doc.exists()){
                                person=doc.toObject(Person.class);
                                isReady.onNext(true);
                            }
                        }
                    }
                });
    }
    public void clear(){
        instance=null;
        firebaseFirestore=null;
        person=null;
        isReady=null;
    }
    public void setState(boolean val){
        this.isReady.onNext(val);
    }
    public void setUpdate(boolean val){
        this.needUpdate.onNext(val);
    }
    public static SingleTon getInstance(){
        if(instance==null){
            instance=new SingleTon();
        }
        return instance;
    }
    public Person getPerson(){
        return this.person;
    }
}

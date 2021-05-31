package com.example.parkinglotapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkinglotapp.R;
import com.example.parkinglotapp.adapters.RecyclerViewAdapter;
import com.example.parkinglotapp.callbacks.callbackInterface;
import com.example.parkinglotapp.model.Person;
import com.example.parkinglotapp.model.TimeIO;
import com.example.parkinglotapp.model.TimeInOutOfFireStore;
import com.example.parkinglotapp.singleton.SingleTon;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class HistoryFragment extends Fragment implements callbackInterface {
    private Context context;
    private View view;
    private RecyclerView recyclerView;
    private List<TimeIO> list=new ArrayList<>();
    private RecyclerViewAdapter adapter;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private String id;
    private SingleTon singleTon;
    private CardView cardViewSearch;
    private EditText edtSearch;
    private boolean isLoaded=false;
    private boolean isAdmin=false;
    private TextView emptyView;
    SwipeRefreshLayout swipeLayout;
    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
//        id=mAuth.getUid();
        view=inflater.inflate(R.layout.fragment_history, container, false);
        emptyView= view.findViewById(R.id.empty_view);
        context=getContext();
        cardViewSearch=view.findViewById(R.id.cardview_search);
        edtSearch=view.findViewById(R.id.edt_search);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text=s.toString().trim();
                if(text.isEmpty()){
                    update(isLoaded,isAdmin);
                }
                else{
                    update(isLoaded,isAdmin,text);
                    Log.d("AAA1",text);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        singleTon=SingleTon.getInstance();
        singleTon.getUpdateObservable().subscribeOn(Schedulers.io())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull Boolean aBoolean) {
                        if(aBoolean){
                            update(isLoaded,isAdmin);
                            singleTon.setUpdate(false);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        swipeLayout=view.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                edtSearch.setText("");
                update(isLoaded,isAdmin);
                swipeLayout.setRefreshing(false);
            }
        });
        firebaseFirestore.collection("Users").document(mAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot cur=task.getResult();
                            Person per=cur.toObject(Person.class);
                            isAdmin=per.isAdmin();
                            isLoaded=true;
                            adapter.setAdmin(isAdmin);
                            update(isLoaded,isAdmin);
                            if(isAdmin){
                                cardViewSearch.setVisibility(View.VISIBLE);
                            }
                        }
                        else{
                            Toast.makeText(context, "error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        recyclerView=view.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter=new RecyclerViewAdapter(list,getContext());
        recyclerView.setAdapter(adapter);
        update(isLoaded,isAdmin);
        solveEmpty();
        return view;
    }

    public void solveEmpty(){
        if(list.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else{
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    public void update(boolean isLoaded,boolean isAdmin){
        if(isLoaded){
            if(isAdmin){
                list.clear();
                adapter.notifyDataSetChanged();
                firebaseFirestore
                        .collection("TimeInOut")
                        .orderBy("time",Query.Direction.DESCENDING)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
//                                    Toast.makeText(context, "size : "+task.getResult().getDocumentChanges(), Toast.LENGTH_SHORT).show();
                                    for(DocumentChange doc:task.getResult().getDocumentChanges()){
                                        if(doc.getType()==DocumentChange.Type.ADDED){
//                                    TimeInOutOfFireStore cur=doc.getDocument().toObject(TimeInOutOfFireStore.class);
//                                    list.add(cur.getTimeIO());
                                            TimeIO cur=doc.getDocument().toObject(TimeIO.class);
                                            list.add(cur);
                                            adapter.notifyDataSetChanged();
                                            solveEmpty();
                                        }
                                    }
                                }
                                else{
                                    Log.d("AAA1","error : "+task.getException().getMessage());
                                }
                            }
                        });
            }
            else{
                list.clear();
                adapter.notifyDataSetChanged();
                firebaseFirestore
                        .collection("TimeInOut")
                        .whereEqualTo("id",mAuth.getUid())
                        .orderBy("time",Query.Direction.DESCENDING)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
//                                    Toast.makeText(context, "size : "+task.getResult().getDocumentChanges(), Toast.LENGTH_SHORT).show();
                                    for(DocumentChange doc:task.getResult().getDocumentChanges()){
                                        if(doc.getType()==DocumentChange.Type.ADDED){
//                                    TimeInOutOfFireStore cur=doc.getDocument().toObject(TimeInOutOfFireStore.class);
//                                    list.add(cur.getTimeIO());
                                            TimeIO cur=doc.getDocument().toObject(TimeIO.class);
                                            list.add(cur);
                                            adapter.notifyDataSetChanged();
                                            solveEmpty();
                                        }
                                    }
                                }
                                else{
                                    Log.d("AAA1","error : "+task.getException().getMessage());
                                }
                            }
                        });
            }
        }
    }
    public void update(boolean isLoaded,boolean isAdmin,String text){
        if(isLoaded){
            if(isAdmin){
                list.clear();
                adapter.notifyDataSetChanged();
                firebaseFirestore
                        .collection("TimeInOut")
                        .orderBy("time",Query.Direction.DESCENDING)
                        .whereEqualTo("name",text)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
//                                    Toast.makeText(context, "size : "+task.getResult().getDocumentChanges(), Toast.LENGTH_SHORT).show();
                                    for(DocumentChange doc:task.getResult().getDocumentChanges()){
                                        if(doc.getType()==DocumentChange.Type.ADDED){
//                                    TimeInOutOfFireStore cur=doc.getDocument().toObject(TimeInOutOfFireStore.class);
//                                    list.add(cur.getTimeIO());
                                            TimeIO cur=doc.getDocument().toObject(TimeIO.class);
                                            list.add(cur);
                                            adapter.notifyDataSetChanged();
                                            solveEmpty();
                                        }
                                    }
                                }
                                else{
                                    Log.d("AAA1","error : "+task.getException().getMessage());
                                }
                            }
                        });
            }
        }
    }

    @Override
    public void passData(String data) {
        Log.d("AAA1","History : "+data);
    }
}
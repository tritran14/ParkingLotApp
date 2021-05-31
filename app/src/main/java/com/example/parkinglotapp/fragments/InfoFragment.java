package com.example.parkinglotapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkinglotapp.R;
import com.example.parkinglotapp.callbacks.callbackInterface;
import com.example.parkinglotapp.singleton.SingleTon;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class InfoFragment extends Fragment {

    private SingleTon singleTon;
    private Context context;
    private View v;
    private TextView tvName,tvLicenseNumber,tvLicensePlateNumber;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_info, container, false);
        context=v.getContext();
        tvName=v.findViewById(R.id.tv_name);
        tvLicenseNumber=v.findViewById(R.id.tv_license_number);
        tvLicensePlateNumber=v.findViewById(R.id.tv_license_plate_number);
        SingleTon.getInstance().getFragmentEventObservable()
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        
                    }

                    @Override
                    public void onNext(@NonNull Boolean aBoolean) {
                        if(aBoolean==true){
//                            Toast.makeText(context, "something :v ", Toast.LENGTH_SHORT).show();
                            Log.d("AAA1","name : "+SingleTon.getInstance().getPerson().getName());
                            tvName.setText(SingleTon.getInstance().getPerson().getName());
                            tvLicenseNumber.setText(SingleTon.getInstance().getPerson().getLicenseNumber());
                            tvLicensePlateNumber.setText(SingleTon.getInstance().getPerson().getLicensePlateNumber());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return v;
    }
}
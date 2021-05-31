package com.example.parkinglotapp.callbacks;

public class CallBackListener {
    callbackInterface callbackItf;

    public CallBackListener(callbackInterface callbackItf) {
        this.callbackItf = callbackItf;
    }
    public void setData(String data){
        callbackItf.passData(data);
    }
}

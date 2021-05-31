package com.example.parkinglotapp.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parkinglotapp.Activity.RootActivity;
import com.example.parkinglotapp.MainActivity;
import com.example.parkinglotapp.R;
import com.example.parkinglotapp.blur.BlurBuilder;
import com.example.parkinglotapp.model.LicensePlateNumber;
import com.example.parkinglotapp.model.Person;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class InfoSetupActivity extends RootActivity {
    private FirebaseAuth mAuth;
    private EditText edtName,edtLicenseNumber,edtLicensePlateNumber;
    private Button btnRegister;
    private String email,password;
    private ConstraintLayout mContainerView;
    private FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_setup);
        String Color="#6C448D";
        getWindow().setNavigationBarColor(android.graphics.Color.parseColor(Color));
        getWindow().setStatusBarColor(android.graphics.Color.parseColor(Color));
        mContainerView=findViewById(R.id.container);
        mAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        init();
        Intent intent=getIntent();
        email=intent.getStringExtra(RegisterActivity.EMAIL);
        password=intent.getStringExtra(RegisterActivity.PASS);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name=edtName.getText().toString().trim();
                final String licenseNumber=edtLicenseNumber.getText().toString().trim();
                final String licensePlateNumber=edtLicensePlateNumber.getText().toString();
                if(name.isEmpty()||licenseNumber.isEmpty()||licensePlateNumber.isEmpty()){
                    Toast.makeText(InfoSetupActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    final boolean[] step1 = {false};
                    final boolean[] step2 = {false};
                    mAuth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        String id=mAuth.getUid();
                                        Person person=new Person(id,name,licensePlateNumber,licenseNumber);
                                        firebaseFirestore.collection("Users").document(id).set(person)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            step1[0] =true;
                                                            if(step2[0]&&step1[0]){
                                                                mAuth.signOut();
                                                                goToLogin();
                                                            }
                                                        }
                                                        else{
                                                            Toast.makeText(InfoSetupActivity.this, "error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                        firebaseFirestore.collection("LicensePlateNumber").document(licensePlateNumber)
                                                .set(new LicensePlateNumber(id)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    step2[0] =true;
                                                    if(step2[0]&&step1[0]){
                                                        mAuth.signOut();
                                                        goToLogin();
                                                    }
                                                }
                                                else{
                                                    Toast.makeText(InfoSetupActivity.this, "error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                    else{
                                        Toast.makeText(InfoSetupActivity.this, "error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

    }
    private void goToLogin(){
        Intent intent=new Intent(InfoSetupActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    public void init(){
        edtName=findViewById(R.id.edt_name);
        edtLicenseNumber=findViewById(R.id.edt_license_number);
        edtLicensePlateNumber=findViewById(R.id.edt_license_plate_number);
        btnRegister=findViewById(R.id.btn_register);
    }
}
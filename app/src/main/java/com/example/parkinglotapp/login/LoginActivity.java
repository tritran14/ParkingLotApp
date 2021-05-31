package com.example.parkinglotapp.login;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkinglotapp.Activity.RootActivity;
import com.example.parkinglotapp.MainActivity;
import com.example.parkinglotapp.R;
import com.example.parkinglotapp.blur.BlurBuilder;
import com.example.parkinglotapp.firebase.FirebaseIDService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends RootActivity {
    private EditText edtName;
    private TextInputEditText edtPass;
    public static final String LOGIN_CODE="login code";
    private Button btnLogin;
    private TextView tvRegister;
    private FirebaseAuth mAuth;
    private ConstraintLayout mContainerView;
    private FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        String Color="#6C448D";
        getWindow().setNavigationBarColor(android.graphics.Color.parseColor(Color));
        getWindow().setStatusBarColor(android.graphics.Color.parseColor(Color));
        mContainerView=findViewById(R.id.container);
//        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background_login);
//        Bitmap blurredBitmap = BlurBuilder.blur( this, originalBitmap );
//        mContainerView.setBackground(new BitmapDrawable(getResources(), blurredBitmap));
        edtName=findViewById(R.id.edt_name);
        edtPass=findViewById(R.id.edt_pass);
        btnLogin=findViewById(R.id.btn_login);
        tvRegister=findViewById(R.id.btn_register);
        firebaseFirestore=FirebaseFirestore.getInstance();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=edtName.getText().toString().trim();
                String pass=edtPass.getText().toString().trim();
                if(name.isEmpty()||pass.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Invalid", Toast.LENGTH_SHORT).show();
                }
                else{
                    mAuth=FirebaseAuth.getInstance();
                    mAuth.signInWithEmailAndPassword(name,pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        firebaseFirestore.collection("Users").document(mAuth.getUid())
                                                .update("token", FirebaseInstanceId.getInstance().getToken());
                                        goToMain();
                                    }
                                    else{
                                        Toast.makeText(LoginActivity.this, "error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoReg();
            }
        });
    }
    private void goToMain(){
        Intent intent=new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra(LOGIN_CODE,1);
        startActivity(intent);
        overridePendingTransition(R.anim.abc_fade_in,R.anim.abc_fade_out);
        finish();
    }
    private void gotoReg(){
        Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }
}
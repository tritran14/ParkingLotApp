package com.example.parkinglotapp.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parkinglotapp.Activity.RootActivity;
import com.example.parkinglotapp.MainActivity;
import com.example.parkinglotapp.R;
import com.example.parkinglotapp.blur.BlurBuilder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class RegisterActivity extends RootActivity {
    private EditText edtEmail,edtPass,edtPass1;
    private Button btn_next;
    private FirebaseAuth mAuth;
    private ConstraintLayout mContainerView;
    public static final String EMAIL="EMAIL";
    public static final String PASS="PASS";
    private View background;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.do_not_move, R.anim.do_not_move);
        setContentView(R.layout.activity_register);
        mAuth=FirebaseAuth.getInstance();
        String Color="#6C448D";
        getWindow().setNavigationBarColor(android.graphics.Color.parseColor(Color));
        getWindow().setStatusBarColor(android.graphics.Color.parseColor(Color));
        background = findViewById(R.id.container);
        if (savedInstanceState == null) {
            background.setVisibility(View.INVISIBLE);

            final ViewTreeObserver viewTreeObserver = background.getViewTreeObserver();

            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        circularRevealActivity();
                        background.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }

                });
            }

        }
        mContainerView=findViewById(R.id.container);
        mAuth=FirebaseAuth.getInstance();
        edtEmail=findViewById(R.id.edt_email);
        edtPass=findViewById(R.id.edt_password);
        edtPass1=findViewById(R.id.edt_password_confirm);
        btn_next=findViewById(R.id.btn_next);
//        btn_next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String email=edtEmail.getText().toString().trim();
//                String pass=edtPass.getText().toString().trim();
//                String pass1=edtPass1.getText().toString().trim();
//                boolean isEmpty=false;
//                for(String x: new String[]{email, pass, pass1}){
//                    if(x.isEmpty()){
//                        isEmpty=true;
//                        break;
//                    }
//                }
//                if(isEmpty){
//                    Toast.makeText(RegisterActivity.this, "Dien day du cac truong", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    if(!pass.equals(pass1)){
//                        Toast.makeText(RegisterActivity.this, "password cua ban khong trung", Toast.LENGTH_SHORT).show();
//                    }
//                    else{
//                        mAuth.createUserWithEmailAndPassword(email,pass)
//                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<AuthResult> task) {
//                                        if(task.isSuccessful()){
//                                            Toast.makeText(RegisterActivity.this, "Successful", Toast.LENGTH_SHORT).show();
//                                            goToMain();
//                                        }
//                                        else{
//                                            Toast.makeText(RegisterActivity.this, "error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                });
//                    }
//                }
//            }
//        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=edtEmail.getText().toString().trim();
                String pass=edtPass.getText().toString().trim();
                String pass1=edtPass1.getText().toString().trim();
                boolean isEmpty=false;
                for(String x: new String[]{email, pass, pass1}){
                    if(x.isEmpty()){
                        isEmpty=true;
                        break;
                    }
                }
                if(isEmpty){
                    Toast.makeText(RegisterActivity.this, "Điền đầy đủ các trường", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(!pass.equals(pass1)){
                        Toast.makeText(RegisterActivity.this, "Password của bạn không trùng", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        mAuth.fetchSignInMethodsForEmail(email)
                                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                        boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
                                        if(isNewUser){
                                            Intent intent=new Intent(RegisterActivity.this,InfoSetupActivity.class);
                                            intent.putExtra(EMAIL,email);
                                            intent.putExtra(PASS,pass);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else{
                                            Toast.makeText(RegisterActivity.this, "Đã có người sử dụng email này", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }
                }
            }
        });
    }
    private void goToMain(){
        Intent intent=new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
    }
    private void circularRevealActivity() {
        int cx = background.getRight() - getDips(44);
        int cy = background.getBottom() - getDips(44);

        float finalRadius = Math.max(background.getWidth(), background.getHeight());

        Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                background,
                cx,
                cy,
                0,
                finalRadius);

        circularReveal.setDuration(3000);
        background.setVisibility(View.VISIBLE);
        circularReveal.start();

    }

    private int getDips(int dps) {
        Resources resources = getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dps,
                resources.getDisplayMetrics());
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int cx = background.getWidth() - getDips(44);
            int cy = background.getBottom() - getDips(44);

            float finalRadius = Math.max(background.getWidth(), background.getHeight());
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(background, cx, cy, finalRadius, 0);

            circularReveal.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    background.setVisibility(View.INVISIBLE);
                    finish();
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            circularReveal.setDuration(3000);
            circularReveal.start();
        }
        else {
            super.onBackPressed();
        }
    }
}
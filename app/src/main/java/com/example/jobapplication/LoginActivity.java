package com.example.jobapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText loginEmail,loginPassword;
    Button loginButton,registerButton,newPassButton;
    private SignInButton signInButton;
    FirebaseAuth firebaseAuth;
    GoogleApiClient mGoogleApiClient;
    String emailStr, passwordStr;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail = (EditText) findViewById(R.id.girisEmail);
        loginPassword = (EditText) findViewById(R.id.girisParola);
        loginButton = (Button) findViewById(R.id.girisButton);
        registerButton = (Button) findViewById(R.id.uyeOlButton);
        newPassButton = (Button) findViewById(R.id.yeniSifreButton);

        firebaseAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emailStr = loginEmail.getText().toString();
                passwordStr = loginPassword.getText().toString();
                if(emailStr.isEmpty() || passwordStr.isEmpty()){

                    Toast.makeText(getApplicationContext(),"Lütfen gerekli alanları doldurunuz!",Toast.LENGTH_SHORT).show();

                }else{

                    loginFunc();
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });


    }
    private void loginFunc() {

        firebaseAuth.signInWithEmailAndPassword(emailStr,passwordStr).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            Intent i = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(i);
                            finish();

                        }
                        else{
                            // hata
                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }

}

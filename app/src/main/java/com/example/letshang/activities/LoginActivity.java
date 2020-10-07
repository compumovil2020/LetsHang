package com.example.letshang.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.letshang.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


    private Button buttonIniciar, buttonForgotPassword;
    private TextView registarText, loginTextUser, loginTextPass;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();


        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Login");

        loginTextUser = findViewById(R.id.email);
        loginTextPass = findViewById(R.id.password);
        buttonIniciar = findViewById(R.id.signinBtn);
        buttonForgotPassword = findViewById(R.id.forgotBtn);

        registarText = findViewById(R.id.registerLogin);

        registarText.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        buttonIniciar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(!isValidEmail(loginTextUser.getText() )){
                    Toast.makeText(getApplicationContext(), "El correo no es valido" ,
                            Toast.LENGTH_SHORT).show();
                }
                else if(loginTextPass.getText().toString().length() < 6){
                    Toast.makeText(getApplicationContext(),
                            "La contraseña debe ser de mínimo 6 caracteres" ,
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    attemptSignIn();
                }
            }
        });

        buttonForgotPassword.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Toast t = Toast.makeText(getApplicationContext() , "Forgot password", Toast.LENGTH_SHORT);
                t.show();
            }
        });

        registarText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RegistroActivity.class);
                startActivity(intent);
            }
        });

    }

    private void attemptSignIn() {
        mAuth.signInWithEmailAndPassword(loginTextUser.getText().toString(), loginTextPass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("login", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("login", loginTextUser.getText().toString());
                            Log.w("login", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext() , PrincipalActivity.class);
            startActivity(intent);
        }
        else{
            loginTextUser.setText("");
            loginTextPass.setText("");
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
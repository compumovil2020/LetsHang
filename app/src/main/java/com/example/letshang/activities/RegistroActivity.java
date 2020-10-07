package com.example.letshang.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class RegistroActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Button btnSiguiente;
    private TextView textName, textEmail, textPhone, textPass, textVerifyPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        mAuth = FirebaseAuth.getInstance();
        user = null;

        btnSiguiente = findViewById(R.id.signupBtn);
        textName = findViewById(R.id.nameSignup);
        textEmail = findViewById(R.id.emailSignup);
        textPhone = findViewById(R.id.phoneSignup);
        textPass = findViewById(R.id.passwordSignup);
        textVerifyPass = findViewById(R.id.verifyPswd);

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(textPass.getText().toString().equals(textVerifyPass.getText().toString())) {
                    attemptSignUp();
                }
                else{
                    Toast.makeText(getApplicationContext() , "Las contraseñas no coinciden :("
                            , Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void attemptSignUp(){
        if(!validateInput()){
            return;
        }



        mAuth.createUserWithEmailAndPassword(textEmail.getText().toString(),
                textPass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("signup", "createUserWithEmail:success");
                            user = mAuth.getCurrentUser();

                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("signup", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });
    }

    private boolean validateInput(){

        if(textName.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "El nombre es requerido.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if(textPhone.getText().toString().length() < 7){
            Toast.makeText(getApplicationContext(), "El numero de telefono no es válido.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!isValidEmail(textEmail.getText())){
            Toast.makeText(getApplicationContext(), "El correo no es válido.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!textVerifyPass.getText().toString().equals(textPass.getText().toString())){
            Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if(textPass.getText().toString().length() < 6){
            Toast.makeText(getApplicationContext(),
                    "La contraseña debe tener al menos 6 caracteres.",
                    Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private void updateUI(FirebaseUser u) {
        if(u == null){
            textPass.setText("");
            textVerifyPass.setText("");
            Toast.makeText(getApplicationContext() ,
                    "no se pudo crear el usuario :(",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            Intent intent = new Intent(getApplicationContext(),  RegistroAdicional.class);
            startActivity(intent);
        }
    }


}
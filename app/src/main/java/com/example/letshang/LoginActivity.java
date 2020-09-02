package com.example.letshang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {


    Button buttonIniciar, buttonForgotPassword, buttonAtras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonIniciar = findViewById(R.id.signinBtn);
        buttonForgotPassword = findViewById(R.id.forgotBtn);
        buttonAtras = findViewById(R.id.backBtnLogin);


        buttonIniciar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext() , PrincipalActivity.class);
                startActivity(intent);
            }
        });

        buttonForgotPassword.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Toast t = Toast.makeText(getApplicationContext() , "Forgot password", Toast.LENGTH_SHORT);
                t.show();
            }
        });

        buttonAtras.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                finish();
            }
        });





    }
}
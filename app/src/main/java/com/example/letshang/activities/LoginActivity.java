package com.example.letshang.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.letshang.R;

public class LoginActivity extends AppCompatActivity {


    private Button btnIngresar, btnOlvidar, btnFacebook, btnGmail, btnYahoo;
    private EditText etCorreo, etPassword;
    private CheckBox cbRecordar;
    private TextView tvRegistrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Login");

        btnIngresar = findViewById(R.id.btnIngresarLogin);
        btnOlvidar = findViewById(R.id.btnOlvidarLogin);
        btnFacebook = findViewById(R.id.btnFacebookLogin);
        btnGmail = findViewById(R.id.btnGmailLogin);
        btnYahoo = findViewById(R.id.btnYahooLogin);

        etCorreo = findViewById(R.id.etCorreoLogin);
        etPassword = findViewById(R.id.etPasswordLogin);

        cbRecordar = findViewById(R.id.cbRememberLogin);

        tvRegistrar = findViewById(R.id.tvRegistrarLogin);
        tvRegistrar.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        btnIngresar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext() , PrincipalActivity.class);
                startActivity(intent);
            }
        });

        btnOlvidar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Toast t = Toast.makeText(getApplicationContext() , "Forgot password", Toast.LENGTH_SHORT);
                t.show();
            }
        });

        tvRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RegistroActivity.class);
                startActivity(intent);
            }
        });

    }
}
package com.example.letshang.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.letshang.R;
import com.example.letshang.providers.EventProvider;

public class StartActivity extends AppCompatActivity {

    private Button btnIniciar, btnRegistro;
    private EventProvider ep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        // esto es para que el provider vaya llamando a firebase y no se demore tanto en cargar los eventos
        //ep.getInsatance();

        btnIniciar = findViewById(R.id.btnIniciarSesionStart);
        btnRegistro = findViewById(R.id.btnRegistrarStart);

        btnIniciar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext() , LoginActivity.class);
                startActivity(intent);
            }
        });

        btnRegistro.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext() , RegistroActivity.class);
                startActivity(intent);
            }
        });
    }


}
package com.example.letshang.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.letshang.R;

public class DescripcionEventoActivity extends AppCompatActivity {

    private TextView tvHost;
    private Button btnCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion_evento);

        getSupportActionBar().setTitle("Evento");

        tvHost = findViewById(R.id.tvHostDescripcionEvento);

        tvHost.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        btnCancelar = findViewById(R.id.btnCancelarDescripcionEvento);

        btnCancelar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EventosInscritos.class);
                startActivity(intent);
            }
        });

        tvHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PerfilHostActivity.class);
                startActivity(intent);
            }
        });


    }
}

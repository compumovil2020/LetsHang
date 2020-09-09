package com.example.letshang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InformacionPerfilActivity extends AppCompatActivity {

    private TextView deportesTag, conciertosTag, conferenciasTag;
    private Button eventBtn, editPerfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_perfil);

        //Inflates
        deportesTag = findViewById(R.id.deportesInfoPerfil);
        conciertosTag = findViewById(R.id.conciertosInfoPerfil);
        conferenciasTag = findViewById(R.id.conferenciasInfoPerfil);
        eventBtn = findViewById(R.id.eventOneInfoPerfil);
        editPerfil = findViewById(R.id.editarInfoPerfil);
        //Set underline
        deportesTag.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        conciertosTag.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        conferenciasTag.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        //Listeners
        eventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ModificarEvento.class);
                startActivity(intent);
            }
        });

        editPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AdministrarPerfil.class);
                startActivity(intent);
            }
        });

    }
}
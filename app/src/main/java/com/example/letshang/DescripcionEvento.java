package com.example.letshang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DescripcionEvento extends AppCompatActivity {

    Button enrollButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion_evento);

        getSupportActionBar().setTitle("Evento");

        enrollButton = findViewById(R.id.descriptionEnrollButton);

        enrollButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext() , InscribirEvento.class);
                startActivity(intent);
            }
        });


    }
}

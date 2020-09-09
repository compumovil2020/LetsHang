package com.example.letshang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DescripcionEvento extends AppCompatActivity {

    Button enrollButton;
    TextView hostName;
    private Button cancellButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion_evento);

        getSupportActionBar().setTitle("Evento");

        enrollButton = findViewById(R.id.btnCancellEvent);
        hostName = findViewById(R.id.textViewHost);

        hostName.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        cancellButton = findViewById(R.id.btnCancellEvent);

        cancellButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EventosInscritos.class);
                startActivity(intent);
            }
        });

        hostName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PerfilHostActivity.class);
                startActivity(intent);
            }
        });


    }
}

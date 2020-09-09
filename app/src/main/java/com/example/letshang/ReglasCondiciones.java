package com.example.letshang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ReglasCondiciones extends AppCompatActivity {


    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reglas_condiciones);

        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PrincipalActivity.class);
                startActivity(intent);
            }
        });
    }
}

package com.example.letshang.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.letshang.R;

public class RegistroAdicionalActivity extends AppCompatActivity {


    private EditText etCiudad, etFecha, etFacebook, etInstagram, etLinkedin;
    private Button btnRegistrar, btnOmitir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_adicional);

        etCiudad = findViewById(R.id.etLocationRegistroAdicional);
        etFecha = findViewById(R.id.etFechaRegistroAdicional);
        etFacebook = findViewById(R.id.etFacebookRegistroAdicional);
        etInstagram = findViewById(R.id.etInstagramRegistroAdicional);
        etLinkedin = findViewById(R.id.etLinkedinRegistroAdicional);

        btnRegistrar = findViewById(R.id.btnRegistrarRegistroAdicional);
        btnOmitir = findViewById(R.id.btnOmitirRegistroAdicional);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PrincipalActivity.class);
                startActivity(intent);
            }
        });

        btnOmitir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PrincipalActivity.class);
                startActivity(intent);
            }
        });
    }
}
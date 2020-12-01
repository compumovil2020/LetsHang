package com.example.letshang.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.letshang.R;
import com.example.letshang.providers.EventProvider;

public class RegistroAdicionalActivity extends AppCompatActivity {


    private EditText etCiudad, etFecha, etFacebook, etInstagram, etLinkedin;
    private Button btnRegistrar, btnOmitir;
    private AwesomeValidation validator;
    private EventProvider ep;

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

        validator = new AwesomeValidation(ValidationStyle.BASIC);

        validator.addValidation(this, R.id.etLocationRegistroAdicional, RegexTemplate.NOT_EMPTY, R.string.requirederror);
        validator.addValidation(this, R.id.etFechaRegistroAdicional, RegexTemplate.NOT_EMPTY, R.string.requirederror);


        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validator.validate()) {
                    ep = EventProvider.getInsatance();
                    Intent intent = new Intent(view.getContext(), PrincipalActivity.class);
                    startActivity(intent);
                }

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
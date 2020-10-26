package com.example.letshang.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.example.letshang.R;

public class CreateGameEventActivity extends AppCompatActivity {

    private TextView titulo, mayorEdad, nivel;
    private EditText tipo, premio, nombre;
    private Spinner respuestaMayorEdad, respuestaNivel;
    private AwesomeValidation validation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game_event);
    }
}
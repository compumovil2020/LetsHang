package com.example.letshang.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.EditText;
import android.widget.ToggleButton;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.letshang.R;
import com.example.letshang.model.Event;
import com.example.letshang.model.EventsEnum;
import com.example.letshang.model.Participant;
import com.example.letshang.model.Preference;
import com.example.letshang.model.User;
import com.example.letshang.providers.UserProvider;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.GregorianCalendar;
import java.util.List;

public class RegistroActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Button btnSiguiente;
    private EditText etNombre, etCorreo, etTelefono, etPassword, etVerify, etNuevo;
    private ToggleButton tbDeportes, tbJuegosMesa, tbConciertos, tbFiesta, tbCharlar, tbConferencias;
    private Button btnAgregar;
    private AwesomeValidation validator;
    private UserProvider userProvider;
    private ChipGroup chipGroup;
    private List<String> tags;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        mAuth = FirebaseAuth.getInstance();
        user = null;
        userProvider = userProvider.getInstance();

        etNombre = findViewById(R.id.etNombreRegistro);
        etCorreo = findViewById(R.id.etCorreoRegistro);
        etTelefono = findViewById(R.id.etTelefonoRegistro);
        etPassword = findViewById(R.id.etPasswordRegistro);
        etVerify = findViewById(R.id.etVerifyRegistro);
        etNuevo = findViewById(R.id.etNuevoRegistro);

        tbDeportes = findViewById(R.id.tbDeportesRegistro);
        tbJuegosMesa = findViewById(R.id.tbJuegosMesaRegistro);
        tbConciertos = findViewById(R.id.tbConciertosRegistro);
        tbFiesta = findViewById(R.id.tbFiestasRegistro);
        tbCharlar = findViewById(R.id.tbCharlarRegistro);
        tbConferencias = findViewById(R.id.tbConferenciasRegistro);
        chipGroup = (ChipGroup) findViewById(R.id.cgTagsCrearEvento);


        btnSiguiente = findViewById(R.id.btnSiguienteRegistro);
        btnAgregar = findViewById(R.id.btnAgregarRegistro);

        validator = new AwesomeValidation(ValidationStyle.BASIC);
        tags = new ArrayList<>();

        validator.addValidation(this, R.id.etNombreRegistro, RegexTemplate.NOT_EMPTY, R.string.requirederror);
        validator.addValidation(this, R.id.etCorreoRegistro, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        validator.addValidation(this, R.id.etTelefonoRegistro, "^[+]?[0-9]{10,13}$", R.string.requirederror);
        validator.addValidation(this, R.id.etPasswordRegistro, ".{6,}", R.string.passworderror);

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etPassword.getText().toString().equals(etVerify.getText().toString())) {
                    attemptSignUp();
                    UserProvider userProvider = UserProvider.getInstance();
                    userProvider.getCurrentUser();
                }
                else{
                    Toast.makeText(getApplicationContext() , "Las contraseñas no coinciden :("
                            , Toast.LENGTH_SHORT).show();
                }
            }
        });

        etNuevo.setOnEditorActionListener(new EditText.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
                if(textView.getText().toString().isEmpty()) {
                    return false;
                }
                else{
                    addTag(textView.getText().toString());
                    textView.setText("");
                    return true;
                }
            }
        });

    }

    private void addTag(String tag){
        for(String s: tags){
            if(s.equals(tag)){
                return;
            }
        }
        tags.add(tag);
        Chip chip = new Chip(this);
        chip.setText(tag);
        chip.setOnCloseIconClickListener(new Chip.OnClickListener(){

            @Override
            public void onClick(View view) {
                Chip currentChip = (Chip) view;
                chipGroup.removeView(view);
                tags.remove(currentChip.getText().toString());
            }
        });
        chip.setCloseIconVisible(true);
        chipGroup.addView(chip);
    }

    private void attemptSignUp(){
        if(!validateInput()){
            return;
        }

        mAuth.createUserWithEmailAndPassword(etCorreo.getText().toString(),
                etPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("signup", "createUserWithEmail:success");
                            user = mAuth.getCurrentUser();

                            Participant dbUser = crearUsuario();
                            userProvider.setUser(dbUser, mAuth.getUid());
                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("signup", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });
    }


    /**
     * crea un objeto usuario usando los campos que se llenaron
     * @return
     */
    private Participant crearUsuario(){

        Participant p = new Participant(etNombre.getText().toString(),
                etCorreo.getText().toString(), new GregorianCalendar(),
                etTelefono.getText().toString(),"", "", "", "",
                "" , new Preference( new EnumMap<EventsEnum, Double>(EventsEnum.class), tags), new ArrayList<Event>());

        // aca le pomgo cualquier valor porque igual en la proxima pantalla se va a actualizar
        p.setLocation(new LatLng(4,-72));

        return p;

    }



    private boolean validateInput(){



        if(!etVerify.getText().toString().equals(etPassword.getText().toString())){
            Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if(chipGroup.getChildCount() == 0){
            Toast.makeText(getApplicationContext(), "Escribe al menos un interes!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return validator.validate();

    }

    private void updateUI(FirebaseUser u) {
        if(u == null){
            etPassword.setText("");
            etVerify.setText("");
            Toast.makeText(getApplicationContext() ,
                    "no se pudo crear el usuario :(",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            Intent intent = new Intent(getApplicationContext(),  RegistroAdicionalActivity.class);
            startActivity(intent);
        }
    }


}
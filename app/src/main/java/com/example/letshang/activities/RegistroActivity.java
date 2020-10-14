package com.example.letshang.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.EditText;
import android.widget.ToggleButton;

import com.example.letshang.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistroActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Button btnSiguiente;
    private EditText etNombre, etCorreo, etTelefono, etPassword, etVerify, etNuevo;
    private ToggleButton tbDeportes, tbJuegosMesa, tbConciertos, tbFiesta, tbCharlar, tbConferencias;
    private Button btnAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        mAuth = FirebaseAuth.getInstance();
        user = null;

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

        btnSiguiente = findViewById(R.id.btnSiguienteRegistro);
        btnAgregar = findViewById(R.id.btnAgregarRegistro);

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etPassword.getText().toString().equals(etVerify.getText().toString())) {
                    attemptSignUp();
                }
                else{
                    Toast.makeText(getApplicationContext() , "Las contraseñas no coinciden :("
                            , Toast.LENGTH_SHORT).show();
                }
            }
        });

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

    private boolean validateInput(){

        if(etNombre.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "El nombre es requerido.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if(etTelefono.getText().toString().length() < 7){
            Toast.makeText(getApplicationContext(), "El numero de telefono no es válido.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!isValidEmail(etCorreo.getText())){
            Toast.makeText(getApplicationContext(), "El correo no es válido.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!etVerify.getText().toString().equals(etPassword.getText().toString())){
            Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if(etPassword.getText().toString().length() < 6){
            Toast.makeText(getApplicationContext(),
                    "La contraseña debe tener al menos 6 caracteres.",
                    Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
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
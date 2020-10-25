package com.example.letshang.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.letshang.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


    private Button btnIngresar, btnOlvidar;
    private ImageButton btnFacebook, btnGmail, btnYahoo;
    private EditText etCorreo, etPassword;
    private CheckBox cbRecordar;
    private TextView tvRegistrar;
    private FirebaseAuth mAuth;
    private AwesomeValidation validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();


        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Login");


        btnIngresar = findViewById(R.id.btnIngresarLogin);
        btnOlvidar = findViewById(R.id.btnOlvidarLogin);
        btnFacebook = findViewById(R.id.btnFacebookLogin);
        btnGmail = findViewById(R.id.btnGmailLogin);
        btnYahoo = findViewById(R.id.btnYahooLogin);

        etCorreo = findViewById(R.id.etCorreoLogin);
        etPassword = findViewById(R.id.etPasswordLogin);

        cbRecordar = findViewById(R.id.cbRememberLogin);

        tvRegistrar = findViewById(R.id.tvRegistrarLogin);
        tvRegistrar.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        validator = new AwesomeValidation(ValidationStyle.BASIC);

        validator.addValidation(this, R.id.etCorreoLogin , Patterns.EMAIL_ADDRESS , R.string.emailerror);
        validator.addValidation(this, R.id.etPasswordLogin , ".{6,}" , R.string.passworderror);

        btnIngresar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(!validator.validate()){
                    return;
                }
                attemptSignIn();

            }
        });

        btnOlvidar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Toast t = Toast.makeText(getApplicationContext() , "Forgot password", Toast.LENGTH_SHORT);
                t.show();
            }
        });

        tvRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RegistroActivity.class);
                startActivity(intent);
            }
        });

    }

    private void attemptSignIn() {
        mAuth.signInWithEmailAndPassword(etCorreo.getText().toString(), etPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("login", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("login", etCorreo.getText().toString());
                            Log.w("login", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext() , PrincipalActivity.class);
            startActivity(intent);
        }
        else{
            etCorreo.setText("");
            etCorreo.setText("");
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
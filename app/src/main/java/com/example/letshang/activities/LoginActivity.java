package com.example.letshang.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.net.wifi.hotspot2.pps.Credential;
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
import com.example.letshang.providers.EventProvider;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;
    private Button btnIngresar, btnOlvidar;
    private ImageButton btnFacebook, btnGmail, btnYahoo;
    private EditText etCorreo, etPassword;
    private CheckBox cbRecordar;
    private TextView tvRegistrar;
    private FirebaseAuth mAuth;
    private AwesomeValidation validator;
    private GoogleSignInClient mGoogleSignInClient;
    private EventProvider ep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createRequest();
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
        ep.getInsatance();

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

        btnGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
            }
        });

    }
    private void createRequest(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().
                        build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

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
            ep = EventProvider.getInsatance();
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

    public void signInGoogle(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Result return from launching the Intent from GoogleSignInApi
        if(requestCode==RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("SignIn Google", "firebaseAuthWithGoogle:" + account.getId());
                fireBaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w("SignIn Google","Google sign in failed",e);
            }
        }
    }
    private void fireBaseAuthWithGoogle(GoogleSignInAccount account){

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d("Login", "signInWithCredential:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                }else{
                    Log.w("Login", "signInWithCredential:failure", task.getException());
                   Toast.makeText(getApplicationContext(), "Signin con Google falló",Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

}
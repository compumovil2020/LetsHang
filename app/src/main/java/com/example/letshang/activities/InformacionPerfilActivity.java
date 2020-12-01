package com.example.letshang.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.letshang.R;
import com.example.letshang.model.Event;
import com.example.letshang.model.Participant;
import com.example.letshang.model.Preference;
import com.example.letshang.model.User;
import com.example.letshang.providers.UserProvider;
import com.example.letshang.ui.adapter.EventsAdapter;
import com.example.letshang.ui.dialog.BottomSheetDialog;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.List;

import static com.example.letshang.utils.PermissionHandler.requestPermission;

public class InformacionPerfilActivity extends AppCompatActivity implements BottomSheetDialog.BottomSheetListener {

    private static final int IMAGE_PICKER_REQUEST = 201;
    private static final int IMAGE_PICKER_PERMISSION = 211;
    private static final int CAMERA_PERMISSION = 66;
    private static final int REQUEST_IMAGE_CAPTURE = 55;
    private final static String TAG = "Informacion perfil";
    private boolean existe = false;

    private TextView tvDeportes, tvConciertos, tvConferencias;
    private Button btnEvento, btnEditar;
    private ActionBarDrawerToggle menuToggle;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private TextView tvNombre, tvCorreo;
    private Button btnFacebook, btnInstagram, btnlinkedin;
    private ImageView ivFotoInformacionPerfil;

    private UserProvider usProv = UserProvider.getInstance();
    private LinearLayout linearLayoutContenedor;
    private LinearLayout linearLayoutTextos;
    private ListView listViewEvents;
    private ChipGroup chipGroup;
    EventsAdapter eventsAdapter;
    private GoogleSignInClient mGoogleSignInClient;

    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        setContentView(R.layout.activity_informacion_perfil);
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        btnEditar = findViewById(R.id.btnEditarInformacionPerfil);

        drawerLayout = findViewById(R.id.informacion_perfil_drawer_layout);
        navView = findViewById(R.id.informacion_perfil_nav_view);

        tvNombre = findViewById(R.id.tvNombreInformacionPerfil);
        tvCorreo = findViewById(R.id.tvCorreoInformacionPerfil);
        chipGroup = findViewById(R.id.cgTagsInformacionPerfil);

        chipGroup = findViewById(R.id.cgTagsInformacionPerfil);

        listViewEvents = findViewById(R.id.listEventosInformacionPerfil);

        btnFacebook = findViewById(R.id.btnFacebookInformacionPerfil);
        btnInstagram = findViewById(R.id.btnInstagramInformacionPerfil);
        btnlinkedin = findViewById(R.id.btnLinkedinInformacionPerfil);

        ivFotoInformacionPerfil = findViewById(R.id.ivFotoInformacionPerfil);

        Participant parti = (Participant)usProv.getCurrentUser();

        tvNombre.setText(parti.getName());
        tvCorreo.setText(parti.getEmail());


        Preference pr = parti.getPreferences();

        List<String> tags = pr.getInterests();

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        int contadorTags = 0;
        for(String tag : tags){
            Chip chip = new Chip(this);
            chip.setText(tag);
            chip.setCloseIconVisible(false);
            chipGroup.addView(chip);
        }

        Participant participant = (Participant)usProv.getCurrentUser();

        List<Event> listEvents = participant.getPastEvents();

        eventsAdapter = new EventsAdapter(this,listEvents);

        listViewEvents.setAdapter(eventsAdapter);

        setupMenu();


        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AdministrarPerfilActivity.class);
                startActivity(intent);
            }
        });

        ivFotoInformacionPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BottomSheetDialog bSheetDialogPermissions = new BottomSheetDialog();
                bSheetDialogPermissions.BottomSheetListener(InformacionPerfilActivity.this);
                bSheetDialogPermissions.show(getSupportFragmentManager(),"exampleBottomSheet");
            }
        });
        try{
            setProfilePic();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void askForImage(){
        Intent pickImage = new Intent(Intent.ACTION_PICK);
        pickImage.setType("image/*");
        startActivityForResult(pickImage,IMAGE_PICKER_REQUEST);
    }


    private void setupMenu(){

        menuToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                R.string.menu_open_reader,
                R.string.menu_close_reader);

        drawerLayout.setDrawerListener(menuToggle);
        menuToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() ==  R.id.item_menu_administrar_perfil){
                    //does nothing
                }
                if(item.getItemId() ==  R.id.item_menu_sugerir){
                    Intent intent = new Intent(getApplicationContext() , CrearSugerenciaActivity.class);
                    startActivity(intent);
                }
                if(item.getItemId() ==  R.id.item_menu_eventos){
                    Intent intent = new Intent(getApplicationContext() , PrincipalActivity.class);
                    startActivity(intent);
                }
                if(item.getItemId() ==  R.id.item_menu_eventos_inscritos){
                    Intent intent = new Intent(getApplicationContext() , EventosInscritosActivity.class);
                    startActivity(intent);
                }
                if(item.getItemId() ==  R.id.item_menu_logout){

                    mAuth.signOut();
                    mGoogleSignInClient.signOut();
                    Intent intent = new Intent(getApplicationContext() , StartActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                if(item.getItemId() ==  R.id.item_menu_pago){
                    Toast.makeText(getApplicationContext(), item.getTitle() ,Toast.LENGTH_SHORT ).show();

                }
                if(item.getItemId() ==  R.id.item_menu_crear_evento){
                    Intent intent = new Intent(getApplicationContext() , CrearEventoActivity.class);
                    startActivity(intent);
                }
                if(item.getItemId() == R.id.item_menu_chat_admin){
                    Intent intent = new Intent(getApplicationContext(), ChatAdminActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }


    // needed to setup menu toggle button
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (menuToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case IMAGE_PICKER_REQUEST:
                if(resultCode==RESULT_OK){

                    Uri imageUri = data.getData();

                    FirebaseUser currentUser = mAuth.getCurrentUser();

                    StorageReference imageRef = mStorageRef.child("images/profile/"+currentUser.getUid()+"/profilePic.jpg");

                    imageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(InformacionPerfilActivity.this, "Imagen Guardada",Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(InformacionPerfilActivity.this, "Falló la subida de la foto",Toast.LENGTH_SHORT).show();
                        }
                    });

                    try{
                        existe = true;
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        ivFotoInformacionPerfil.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
                break;
            case REQUEST_IMAGE_CAPTURE:
                if(resultCode == RESULT_OK){
                    Bundle imageUri = data.getExtras();
                    Bitmap imagensota = (Bitmap) imageUri.get("data");

                    String path = MediaStore.Images.Media.insertImage(this.getContentResolver(),imagensota,"FotoPerfil",null);

                    FirebaseUser currentUser = mAuth.getCurrentUser();

                    StorageReference imageRef = mStorageRef.child("images/profile/"+currentUser.getUid()+"/profilePic.jpg");

                    imageRef.putFile(Uri.parse(path)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(InformacionPerfilActivity.this, "Imagen Guardada",Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(InformacionPerfilActivity.this, "Falló la subida de la foto",Toast.LENGTH_SHORT).show();
                        }
                    });

                    try{
                        existe = true;
                        final InputStream imageStream = getContentResolver().openInputStream(Uri.parse(path));
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        ivFotoInformacionPerfil.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private void setProfilePic() throws IOException {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        final File localFile = File.createTempFile("images","jpg");
        StorageReference imageRef = mStorageRef.child("images/profile/"+currentUser.getUid()+"/profilePic.jpg");
        imageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Bitmap selectedImage = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                ivFotoInformacionPerfil.setImageBitmap(selectedImage);
                existe = true;
            }
        });
    }

    private void takePicture(){
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePicture.resolveActivity(getPackageManager())!= null){
            startActivityForResult(takePicture,REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case IMAGE_PICKER_PERMISSION:
                if(ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    askForImage();
                }
                return;

            case CAMERA_PERMISSION:
                if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    takePicture();
                }
                return;
        }
    }

    @Override
    public void onButtonClicked(String imagen) {
        if(imagen.equals("Eliminar") && existe){
            existe = false;
            ivFotoInformacionPerfil.setImageBitmap(null);
            StorageReference imageRefDelete = mStorageRef.child("images/profile/"+mAuth.getUid()+"/profilePic.jpg");
            imageRefDelete.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // File deleted successfully
                    Toast.makeText(InformacionPerfilActivity.this, "Foto borrada correctamente",Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Uh-oh, an error occurred!
                    Toast.makeText(InformacionPerfilActivity.this, "Falló para borrar foto",Toast.LENGTH_SHORT).show();
                }
            });

            ivFotoInformacionPerfil.setImageResource(R.drawable.icn_profile);
        } else {
            Toast.makeText(InformacionPerfilActivity.this, "No tiene foto para borrar :)",Toast.LENGTH_SHORT).show();
        }
    }
}
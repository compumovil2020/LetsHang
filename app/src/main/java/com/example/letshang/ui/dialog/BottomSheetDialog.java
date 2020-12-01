package com.example.letshang.ui.dialog;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.letshang.R;
import com.example.letshang.activities.InformacionPerfilActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import static com.example.letshang.utils.PermissionHandler.requestPermission;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    private BottomSheetListener mListener;

    private static final int IMAGE_PICKER_REQUEST = 201;
    private static final int IMAGE_PICKER_PERMISSION = 211;
    private static final int CAMERA_PERMISSION = 66;
    private static final int REQUEST_IMAGE_CAPTURE = 55;
    private Activity contextAnt;

    public void BottomSheetListener(Activity c){
        this.contextAnt = c;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        Button btEliminar = v.findViewById(R.id.btEliminarFotoSheet);
        Button btGaleria = v.findViewById(R.id.btFotoGaleriaSheet);
        Button btCamara = v.findViewById(R.id.btFotoCamaraSheet);


        //Listener para eliminar
        btEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("SHEETBOTTOM","Eliminar");
                mListener.onButtonClicked("Eliminar");
                dismiss();
            }
        });

        //Listener para galeria
        btGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission(contextAnt, Manifest.permission.READ_EXTERNAL_STORAGE,"El permiso es para poder poner tu foto de perfil",IMAGE_PICKER_PERMISSION );
                Log.i("SHEETBOTTOM","Galeria");
                //mListener.onButtonClicked(null);
                dismiss();
            }
        });

        //Listener para camara
        btCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission( contextAnt, Manifest.permission.CAMERA,"El permiso es para poder poner tu foto de perfil",CAMERA_PERMISSION);
                Log.i("SHEETBOTTOM","Camara");
                //mListener.onButtonClicked(null);
                dismiss();
            }
        });

        return v;
    }

    public interface BottomSheetListener{
        void onButtonClicked(String imagen);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try{
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e){
            throw  new ClassCastException(context.toString()
            + " must implement BottomSheetListener");
        }

    }

}

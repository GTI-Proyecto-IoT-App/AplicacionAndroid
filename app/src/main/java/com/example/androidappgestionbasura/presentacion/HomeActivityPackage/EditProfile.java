package com.example.androidappgestionbasura.presentacion.HomeActivityPackage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidappgestionbasura.R;
import com.example.androidappgestionbasura.casos_uso.CasosUsoUsuario;
import com.example.androidappgestionbasura.datos.firebase.callback.CallBack;
import com.example.androidappgestionbasura.utility.AppConf;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import static android.content.ContentValues.TAG;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    private CasosUsoUsuario casosUsoUsuario;
    private EditText profileName;
    private Button saveButton;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private Button cancelButton;

    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        casosUsoUsuario = new CasosUsoUsuario(EditProfile.this);
        fAuth= FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

        Intent data = getIntent();
        reference=FirebaseDatabase.getInstance().getReference("usuarios");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final String name =data.getStringExtra("name");

        profileName = findViewById(R.id.profileName);
        profileName.setText(casosUsoUsuario.getUsuario().getName());
        saveButton=findViewById(R.id.buttonGuardar);
        cancelButton=findViewById(R.id.buttonCancelar);






        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile (v);

            }

        });


    }


    public void updateProfile(View view){
        view.setEnabled(false);
        String newName = profileName.getText().toString();
        casosUsoUsuario.getUsuario().setName(newName);
        casosUsoUsuario.updateUsuario(casosUsoUsuario.getUsuario(), new CallBack() {
            @Override
            public void onSuccess(Object object) {
                setResult(Activity.RESULT_OK);
                finish();
            }

            @Override
            public void onError(Object object) {

            }
        });
    }



}
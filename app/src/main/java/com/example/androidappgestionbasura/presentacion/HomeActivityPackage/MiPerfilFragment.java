package com.example.androidappgestionbasura.presentacion.HomeActivityPackage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.androidappgestionbasura.R;
import com.example.androidappgestionbasura.casos_uso.CasosUsoUsuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;


public class MiPerfilFragment extends Fragment {


    private CasosUsoUsuario casosUsoUsuario;
    private TextView editTextNombre;
    private TextView editTextCorreo;
    private static final int GALLERY_REQUEST_CODE = 123;
    private static final int REQUEST_EDIT_USER = 789;
    final static int RESULTADO_GALERIA = 2;
    private CircleImageView circleImageView;
    private ImageButton imageButton;
    public Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    int TAKE_IMAGE_CODE = 10;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_mi_perfil, container, false);
        casosUsoUsuario = new CasosUsoUsuario(getActivity());
        circleImageView = root.findViewById(R.id.fotoPerfil);
        imageButton = root.findViewById(R.id.btnFoto);
        editTextNombre = root.findViewById(R.id.textViewNombreUsuario);
        editTextCorreo = root.findViewById(R.id.textViewCorreoUsuario);
        setHasOptionsMenu(true);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.getPhotoUrl() != null) {
           Glide.with(this).load(user.getPhotoUrl()).into(circleImageView);
        }


        setUpVista(root);
        setUpAcciones(root);


        return root;
    }


    public void handleImageClick(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getActivity().getPackageManager()) != null){

           startActivityForResult(intent,TAKE_IMAGE_CODE);
        }
    }


   /* public void ponerDeGaleria(int codidoSolicitud) {
        String action;
        if (android.os.Build.VERSION.SDK_INT >= 19) { // API 19 - Kitkat
            action = Intent.ACTION_OPEN_DOCUMENT;
        } else {
            action = Intent.ACTION_PICK;
        }
        Intent intent = new Intent(action,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, codidoSolicitud);
    }
    public void ponerDeGaleria(View view) {
        ponerDeGaleria(RESULTADO_GALERIA);
    }
*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_IMAGE_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    circleImageView.setImageBitmap(bitmap);
                    handleUpload(bitmap);
            }
        }else if(requestCode == REQUEST_EDIT_USER){
            switch (resultCode) {
                case Activity.RESULT_OK:
                   setUpVista(null);
                    Toast.makeText(getContext(), "Actualizado correctamente", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void handleUpload(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final StorageReference reference = FirebaseStorage.getInstance().getReference().child("profileImages").child(uid + ".jpeg");

        reference.putBytes(baos.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.e(TAG, "Subida");
                getDownloadUrl(reference);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure:", e.getCause());
            }
        });
    }

    private void getDownloadUrl(StorageReference reference) {
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d(TAG, "onSuccess:" + uri);
                setUserProfileUrl(uri);
            }
        });
    }

    private void setUserProfileUrl(Uri uri) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setPhotoUri(uri).build();
        user.updateProfile(request).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "Actualizado correctamente", Toast.LENGTH_SHORT).show();

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();


                    }
                });
    }

    private void setUpVista(View root) {

        editTextNombre.setText(casosUsoUsuario.getUsuario().getName());

        editTextCorreo.setText(casosUsoUsuario.getUsuario().getEmail());

    }


    private void setUpAcciones(View root) {
        Button btnCerrarSesion = root.findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrar_sesion(v);
            }

        });
        Button btnInactivar = root.findViewById(R.id.buttonInactivar);
        btnInactivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inactivarCuenta(v);
            }

        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleImageClick(v);
            }

        });

    }


    public void inactivarCuenta(View view) {
        new AlertDialog.Builder(getContext())
                .setTitle("Inactivar su cuenta")
                .setMessage("¿Desea inactivar su cuenta?")
                .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        casosUsoUsuario.cerrarSesion();
                    }
                })
                .setNegativeButton(getString(android.R.string.cancel), null)
                .show();
    }


    public void cerrar_sesion(View v) {
        new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.cerrar_sesion))
                .setMessage(R.string.cerrar_sesion_mensaje)
                .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        casosUsoUsuario.cerrarSesion();
                    }
                })
                .setNegativeButton(getString(android.R.string.cancel), null)
                .show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_perfil_usuario, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.accion_editar_usuario) {
            Intent myIntent = new Intent(MiPerfilFragment.this.getActivity(), EditProfile.class);
            startActivityForResult(myIntent, REQUEST_EDIT_USER);
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

}
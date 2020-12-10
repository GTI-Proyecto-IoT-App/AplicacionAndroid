package com.example.androidappgestionbasura.presentacion.HomeActivityPackage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
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
    private void handleImageClick(View view) {
        final CharSequence[] options = { "Saca una foto", "Selecciona desde la galería","Cancelar" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Selecciona tu foto de perfil");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Saca una foto")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Selecciona desde la galería")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);

                } else if (options[item].equals("Cancelar")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        circleImageView.setImageBitmap(bitmap);
                        handleUpload(bitmap);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage =  data.getData();
                        String[] filePath = {MediaStore.Images.Media.DATA};
                        Cursor c = getActivity().getContentResolver().query(selectedImage,filePath, null, null, null);
                        c.moveToFirst();
                        int columnIndex = c.getColumnIndex(filePath[0]);
                        String picturePath = c.getString(columnIndex);
                        c.close();
                        Bitmap bitmap = (BitmapFactory.decodeFile(picturePath));

                        circleImageView.setImageBitmap(bitmap);
                        handleUpload(bitmap);

                    }
                    break;



            }
        }else if(requestCode == REQUEST_EDIT_USER){
            switch (resultCode) {
                case RESULT_OK:
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
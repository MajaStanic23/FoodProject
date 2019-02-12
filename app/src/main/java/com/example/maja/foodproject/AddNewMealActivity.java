package com.example.maja.foodproject;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class AddNewMealActivity extends AppCompatActivity {
    Button buttonDone;
    ImageButton imageButtonAddPhoto;
    EditText editText1;
    EditText editText2;
    Spinner spinner;
    String info1, info2;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Firebase firebaseReference;
    StorageReference storageReference;
    ImageView imageView;
    private Uri imageUri;
    private Button btn, mSubmitBtn;
    private ImageView imageview;
    private static final int GALLERY = 1, CAMERA = 1;

    ArrayAdapter<CharSequence> arrayAdapter;
    StorageReference mStorageRef;
    private DatabaseReference mDatabase;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_meal);
        spinner = findViewById(R.id.spinner);
        buttonDone = findViewById(R.id.done);
        imageButtonAddPhoto = findViewById(R.id.addPhoto);
        editText1 = findViewById(R.id.mealName);
        editText2 = findViewById(R.id.description);
        arrayAdapter = ArrayAdapter.createFromResource(this, R.array.spinner, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        Firebase.setAndroidContext(this);


        storageReference = FirebaseStorage.getInstance().getReference();


        imageButtonAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startPosting();
                createNewMeal();
            }
        });

    }



    private void createNewMeal() {
        Spinner spinner = findViewById(R.id.spinner);
        String text = spinner.getSelectedItem().toString();
        info1 = editText1.getText().toString();
        info2 = editText2.getText().toString();
        String firebaseString="https://foodproject-8d192.firebaseio.com/";
        String link = firebaseString + text;
        firebaseReference = new Firebase(link);

        if (info1 != null && info2 != null && !info1.equals("") && !info2.equals("")) {
            firebaseReference.child(info1).child("title").setValue(info1);
            firebaseReference.child(info1).child("description").setValue(info2);
            //firebaseReference = FirebaseDatabase.getInstance().getReference().child("All Categories").child(text);

            firebaseReference.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
                @Override
                public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                    Toast.makeText(getApplicationContext(), "Podaci uspješno uneseni", Toast.LENGTH_LONG).show();
                    finish();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                    Toast.makeText(getApplicationContext(), "Došlo je do pogreške mreže", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Molim unesite sva polja", Toast.LENGTH_LONG).show();
        }

    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);

    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==GALLERY){
            if(data!=null){
                imageUri=data.getData();
                imageButtonAddPhoto.setImageURI(imageUri);
                uploadImageToFirebase();

            }
        }

    }
    private void uploadImageToFirebase(){
        final StorageReference pictureReference= FirebaseStorage.getInstance().getReference("images"+"123");
        if(imageUri != null){
            pictureReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AddNewMealActivity.this,"slika je uploadana", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddNewMealActivity.this,"greska, nije uploadano", Toast.LENGTH_LONG).show();

                }
            });

        }

    }
}
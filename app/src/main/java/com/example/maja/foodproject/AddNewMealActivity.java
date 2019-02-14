package com.example.maja.foodproject;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment; import com.squareup.picasso.Picasso;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Random;

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

    private Uri mImageUri;



    ArrayAdapter<CharSequence> arrayAdapter;
    StorageReference mStorageRef;



    private static final int CAMERA_REQUEST_CODE=1;
    private int GALLERY = 1;
    private StorageReference mStorage;
    static String pictures = "";


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


        mStorage = FirebaseStorage.getInstance().getReference();


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
                generateRandomNumber();
                createNewMeal();
                startPosting();
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
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String mealName = prefs.getString("randomID", "meal"); //no id: default value

            firebaseReference.child(mealName).child("title").setValue(info1);
            firebaseReference.child(mealName).child("description").setValue(info2);
            //firebaseReference.child(mealName).child("pictureUrl").setValue(pictures);
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
                                choosePhotoFromGallery();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory()
                + "/imgDirectory", "imgName"+ ".png");

       mImageUri= Uri.fromFile(file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);*/


    }

    ////////////////////////////
    private void startPosting(){

        if (mImageUri !=null){

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String mealName = prefs.getString("randomID", "meal"); //no id: default value

            final StorageReference filepath = mStorageRef.child("images").child(mealName);


            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();
                    //DatabaseReference newPost = databaseReference.push();//push kreira uniq random id
                    //newPost.child("images").setValue(downloadUrl.toString());
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Toast.makeText(getApplicationContext(), uri.toString(), Toast.LENGTH_LONG).show();
                            Log.d("URIURI", uri.toString());
                            pictures = uri.toString();
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            String mealName = prefs.getString("randomID", "meal"); //no id: default value
                            firebaseReference.child(mealName).child("pictureUrl").setValue(pictures);

                        }
                    });
                    Toast.makeText(getApplicationContext(), databaseReference.push().toString(), Toast.LENGTH_LONG).show();
                    Log.d("link", databaseReference.push().toString());

                }
            });
        }
    }


    ////////////////////////
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                mImageUri = data.getData();
                imageButtonAddPhoto.setImageURI(mImageUri);
                Log.d("imageURI", mImageUri.toString());
                Toast.makeText(getApplicationContext(), mImageUri.toString(), Toast.LENGTH_LONG).show();
            }

        } else if (requestCode == CAMERA_REQUEST_CODE && requestCode==RESULT_OK) {
/*
            if (data != null) {
            mImageUri = data.getData();
            imageButtonAddPhoto.setImageURI(mImageUri);
            StorageReference filePath=mStorage.child("photos").child(mImageUri.getLastPathSegment());
            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AddNewMealActivity.this, "Uspjesno",Toast.LENGTH_LONG).show();
                }
            });
        }*/
        }
    }


    public String generateRandomNumber(){

        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();

        Log.d("generatedString", generatedString);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("randomID", generatedString);
        editor.apply();

        return generatedString;
    }
}
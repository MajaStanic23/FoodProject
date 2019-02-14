package com.example.maja.foodproject;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.lang.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodListActivity extends AppCompatActivity {

    private Firebase mRef;
    private ListView mListView;
    private FoodAdapter customAdapter;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String foodType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        Firebase.setAndroidContext(this);

        Intent intent = getIntent();
        foodType = intent.getStringExtra("foodType");
        String firebaseString="https://foodproject-8d192.firebaseio.com/";
        String link = firebaseString + foodType;
        mRef = new Firebase(link);
        Log.d("databaseString",link);

        mListView = findViewById(R.id.ListView);

        // Popuniti ovu listu food itemima

        //List<FoodItem> foodItems = new ArrayList<FoodItem>();

        //foodItems.add(new FoodItem("Burger", "super krabby patty", "https://www.designindaba.com/sites/default/files/styles/scaledlarge/public/node/news/23566/sonic-burger.jpg?itok=zGk5pjcI"));

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().getRoot();
        Log.d("root", databaseReference.toString());

        populateString();


    }


    private void populateString() {

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<FoodItem> foodItems = new ArrayList<FoodItem>();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                    Log.d("key", childDataSnapshot.getKey());
                    String description = String.valueOf(childDataSnapshot.child("description").getValue());
                    String title = String.valueOf(childDataSnapshot.child("title").getValue());
                    String pictureUrl = String.valueOf(childDataSnapshot.child("pictureUrl").getValue());
                    Log.d("value", String.valueOf(childDataSnapshot.child("description").getValue()));
                    Log.d("value", String.valueOf(childDataSnapshot.child("title").getValue()));

                    String uri = null;
                    foodItems.add(new FoodItem(title, description, pictureUrl));
                    customAdapter = new FoodAdapter(foodItems);
                    mListView.setAdapter(customAdapter);

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }}
package com.example.maja.foodproject;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    DatabaseHelperSQL databaseHelperSQL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        Firebase.setAndroidContext(this);

        databaseHelperSQL=new DatabaseHelperSQL(this);

        Intent intent = getIntent();
        foodType = intent.getStringExtra("foodType");
        String firebaseString="https://foodproject-8d192.firebaseio.com/";
        String link = firebaseString + foodType;
        mRef = new Firebase(link);
        Log.d("databaseString",link);

        mListView = findViewById(R.id.ListView);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().getRoot();
        Log.d("root", databaseReference.toString());

        populateString();


        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(FoodListActivity.this, "lajkic", Toast.LENGTH_LONG).show();
                mListView.getItemAtPosition(position).toString();
                //Toast.makeText(FoodListActivity.this, mListView.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();

                TextView textView1= view.findViewById(R.id.title);
                TextView textView2= view.findViewById(R.id.desc);

                String title= textView1.getText().toString();
                String description= textView2.getText().toString();
                AddData(title,description);

                return true;
            }
        });
        populateListviewSQL();
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


    }
    public  void  AddData(String title,String description){
        boolean insertData= databaseHelperSQL.addData(title, description);
        if(insertData){
            Toast.makeText(FoodListActivity.this, "Data inserted correctly", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(FoodListActivity.this, "Database error", Toast.LENGTH_LONG).show();
        }
    }
    private void populateListviewSQL(){
        Cursor data=databaseHelperSQL.getData();
        List<FoodItem> foodItems = new ArrayList<FoodItem>();
        while (data.moveToNext()){

            String pictureUrl ="https://i.dietdoctor.com/wp-content/uploads/2018/06/DD_keto-meals_feat.jpg?auto=compress%2Cformat&w=1500&h=844&fit=crop";
            foodItems.add(new FoodItem(data.getString(1), data.getString(2), pictureUrl));
            customAdapter = new FoodAdapter(foodItems);
            mListView.setAdapter(customAdapter);
        }
    }
}
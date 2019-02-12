package com.example.maja.foodproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import static android.R.id.message;

import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

public class SelectActivity extends AppCompatActivity {
   private TextView mCategory;
   private ImageButton mProfile, mAdd;
   private  Button btnAppetizer, btnMainCourse, btnDessert;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView textView;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        mCategory = findViewById(R.id.category);
        mProfile=(ImageButton) findViewById(R.id.profile);
        mAdd=(ImageButton) findViewById(R.id.add);
        btnAppetizer=(Button) findViewById(R.id.appetizer);
        btnMainCourse=(Button) findViewById(R.id.mainCourse);
        btnDessert=(Button) findViewById(R.id.dessert);
        textView =  findViewById(R.id.email_sign_out_button);
        fab=findViewById(R.id.fab);
        mAuth = FirebaseAuth.getInstance();
        textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(SelectActivity.this, MainActivity.class);
                startActivity(intent);

            }});

        btnAppetizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String foodType = "apetizer";
                Intent intent= new Intent ( SelectActivity.this, FoodListActivity.class);
                intent.putExtra("foodType", foodType);
                startActivity(intent);
            }
        });
        btnMainCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodType = "main";
                Intent intent= new Intent ( SelectActivity.this, FoodListActivity.class);
                intent.putExtra("foodType", foodType);
                startActivity(intent);
            }
        });
        btnDessert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodType = "dessert";
                Intent intent= new Intent ( SelectActivity.this, FoodListActivity.class);
                intent.putExtra("foodType", foodType);
                startActivity(intent);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent( SelectActivity.this, AddNewMealActivity.class);
                startActivity(intent);
            }
        });


    }
}
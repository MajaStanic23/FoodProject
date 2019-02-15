package com.example.maja.foodproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelperSQL extends SQLiteOpenHelper {
    private static  final  String TABLE_NAME="foodTable";
    private static final String column="id";
    private static final String column2="title";
    private static final String column3="description";



    public DatabaseHelperSQL(Context context) {
        super(context,TABLE_NAME,null,1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable="CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " + column2 +" TEXT, " + column3 +" TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public boolean addData(String title, String description){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(column2,title);
        contentValues.put(column3,description);
        long result= db.insert(TABLE_NAME,null,contentValues);
        if(result== -1){
            return false;

        } else {
            return  true;
        }

    }
    public Cursor getData(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor data= db.rawQuery("SELECT * FROM " + TABLE_NAME,null);
        return data;

    }
}

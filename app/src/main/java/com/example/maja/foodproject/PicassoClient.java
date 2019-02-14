package com.example.maja.foodproject;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PicassoClient {
    public static void download(String url, ImageView imageview){

            Picasso.get().load(url).into(imageview);


    }
}


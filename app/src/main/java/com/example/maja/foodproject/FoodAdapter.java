package com.example.maja.foodproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FoodAdapter extends BaseAdapter {

    List<FoodItem> foodItems;

    public FoodAdapter(List<FoodItem> foodItems) {
        this.foodItems = foodItems;
    }

    @Override
    public int getCount() {
        return foodItems.size();
    }

    @Override
    public Object getItem(int position) {
        return foodItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        FoodViewHolder holder = null;

        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_food, parent, false);
            holder = new FoodViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (FoodViewHolder) convertView.getTag();
        }

        FoodItem food = foodItems.get(position);

        holder.title.setText(food.getTitle());
        holder.description.setText(food.getDescription());

        return convertView;
    }

    class FoodViewHolder {

        TextView title;
        ImageView icon;
        TextView description;

        public FoodViewHolder(View view) {
            title = view.findViewById(R.id.title);
            icon = view.findViewById(R.id.icon);
            description = view.findViewById(R.id.desc);
        }
    }
}

//public class CustomAdapter {
//    private  String title;
//    private String description;
//
//    public CustomAdapter() {
//
//    }
//
//    public CustomAdapter(String title, String description) {
//        this.title = title;
//        this.description = description;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//}

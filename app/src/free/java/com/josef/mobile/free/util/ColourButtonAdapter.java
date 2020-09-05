package com.josef.mobile.free.util;

import android.content.Context;

import androidx.annotation.NonNull;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.josef.josefmobile.R;
import com.josef.mobile.free.Data;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class ColourButtonAdapter extends RecyclerView.Adapter<ColourButtonAdapter.FavoriteHolder> {

    private ArrayList<Data> arrayList;
    private static final String TAG = "FavouriteAdapter";
    private Context context;


    public ColourButtonAdapter(Context conext, ArrayList<Data> arrayList){
        this.arrayList=arrayList;
        this.context= conext;
    }

    @NonNull
    @Override
    public FavoriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.colobuttoncard;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new FavoriteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteHolder noteHolder, int i) {
        Data currentNote = arrayList.get(i);
        String color = currentNote.getColor();
        noteHolder.imageButton.setBackgroundColor(Color.parseColor(color));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void setNotes(ArrayList<Data> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    class FavoriteHolder extends RecyclerView.ViewHolder  {

       public MaterialButton imageButton;

        public FavoriteHolder(View itemView) {
            super(itemView);
            imageButton = itemView.findViewById(R.id.gridcolorbutton);
        }
        public MaterialButton getImageButton() {
            return imageButton;
        }

    }

}


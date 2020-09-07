package com.josef.mobile.free.ui.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.josef.josefmobile.R;
import com.josef.mobile.free.ui.body.Data;
import java.util.ArrayList;

public class ColourButtonAdapter extends RecyclerView.Adapter<ColourButtonAdapter.FavoriteHolder> {

    private ArrayList<Data> arrayList;
    private static final String TAG = "FavouriteAdapter";
    private Context context;
    private ColourButtonAdapterOnClickHander onClickHander;

    public interface ColourButtonAdapterOnClickHander {
        void onClick(String string);
    }

    public ColourButtonAdapter(Context conext,ColourButtonAdapterOnClickHander onClickHander, ArrayList<Data> arrayList) {
        this.arrayList = arrayList;
        this.context = conext;
        this.onClickHander = onClickHander;
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

    class FavoriteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public MaterialButton imageButton;

        public FavoriteHolder(View itemView) {
            super(itemView);
            imageButton = itemView.findViewById(R.id.gridcolorbutton);
            itemView.setOnClickListener(this);
        }

        public MaterialButton getImageButton() {
            return imageButton;
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            onClickHander.onClick(Integer.toString(adapterPosition));
        }
    }

}


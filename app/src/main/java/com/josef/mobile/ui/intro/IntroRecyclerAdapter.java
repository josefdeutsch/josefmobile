package com.josef.mobile.ui.intro;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.josef.mobile.R;
import com.josef.mobile.models.ContainerOnBoard;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class IntroRecyclerAdapter extends RecyclerView.Adapter<IntroRecyclerAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<ContainerOnBoard> list = new ArrayList<>();

    public IntroRecyclerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_intro_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setIntro(ArrayList<ContainerOnBoard> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image1);
        }

        public void bind(final ContainerOnBoard container) {
            Picasso.get().load(container.getPng()).config(Bitmap.Config.ARGB_8888)
                    .fit().centerCrop().into(imageView);
        }
    }
}

package com.josef.mobile.components;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.josef.josefmobile.R;
import com.josef.mobile.model.MainActivityAdapterBody;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.ViewHolder> {

    private List<MainActivityAdapterBody> mValues;

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        }
    };

    public MainActivityAdapter(ArrayList<MainActivityAdapterBody> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mTextView.setText(mValues.get(position).getName());
        holder.itemView.setTag(mValues.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);

       // Picasso.get().load(R.drawable.ic_folder_black_24dp).into(holder.imageButton);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void setRecipes(List<MainActivityAdapterBody> arrayList) {
        this.mValues = arrayList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTextView;
        public final ImageView imageButton;

        ViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.main_text);
            imageButton = itemView.findViewById(R.id.imageButton);
        }
    }
}




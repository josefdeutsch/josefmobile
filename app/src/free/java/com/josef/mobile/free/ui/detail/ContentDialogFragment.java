package com.josef.mobile.free.ui.detail;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.josef.josefmobile.R;
import com.josef.mobile.data.Favourite;
import com.josef.mobile.data.FavouriteViewModel;
import com.josef.mobile.free.Data;
import com.josef.mobile.free.util.ColourButtonAdapter;

import java.util.ArrayList;
import java.util.List;


public class ContentDialogFragment extends ContentComponentFragment implements ColourButtonAdapter.ColourButtonAdapterOnClickHander {

    private static final String TAG = "ContentDialogFragment";
    private RecyclerView mRecyclerView;
    private FavouriteViewModel favouriteViewModel;
    private String mDialogIndex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mRecyclerView = new RecyclerView(getContext());
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        favouriteViewModel = ViewModelProviders.of(this).get(FavouriteViewModel.class);
        favouriteViewModel.getAllNotes().observe(this, new Observer<List<Favourite>>() {
            @Override
            public void onChanged(@Nullable List<Favourite> notes) {

                ArrayList<Data> datalist = new ArrayList<>();
                datalist.add(new Data("#C8C8C8"));
                datalist.add(new Data("#180000"));
                datalist.add(new Data("#ffff00"));
                datalist.add(new Data("#C8C8C8"));
                datalist.add(new Data("#180000"));
                datalist.add(new Data("#ffff00"));
                datalist.add(new Data("#C8C8C8"));
                datalist.add(new Data("#180000"));
                datalist.add(new Data("#ffff00"));
                mRecyclerView.setAdapter(new ColourButtonAdapter(getContext(),ContentDialogFragment.this, datalist));
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setTitle("uschi")
                .setView(mRecyclerView)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
                            }
                        }
                ).create();
    }

    @Override
    public void onClick(String string) {
        favouriteViewModel.setString(string);
    }
    /**  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_detail_dialog, container, false);
    mRecyclerView = v.findViewById(R.id.dialogrecycler_view);
    GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
    mRecyclerView.setLayoutManager(layoutManager);
    mRecyclerView.setHasFixedSize(true);
    favouriteViewModel = ViewModelProviders.of(this).get(FavouriteViewModel.class);
    favouriteViewModel.getAllNotes().observe(this, new Observer<List<Favourite>>() {
    @Override public void onChanged(@Nullable List<Favourite> notes) {

    ArrayList<Data> datalist = new ArrayList<>();
    datalist.add(new Data("#C8C8C8"));
    datalist.add(new Data("#180000"));
    datalist.add(new Data("#ffff00"));
    datalist.add(new Data("#C8C8C8"));
    datalist.add(new Data("#180000"));
    datalist.add(new Data("#ffff00"));
    datalist.add(new Data("#C8C8C8"));
    datalist.add(new Data("#180000"));
    datalist.add(new Data("#ffff00"));

    mRecyclerView.setAdapter(new ColourButtonAdapter(getContext(),datalist));
    }
    });
    return v;
    }**/

}

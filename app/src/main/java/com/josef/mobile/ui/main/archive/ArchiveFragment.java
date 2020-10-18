package com.josef.mobile.ui.main.archive;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.josef.mobile.R;
import com.josef.mobile.data.Favourite;
import com.josef.mobile.data.FavouriteViewModel;
import com.josef.mobile.viewmodels.ViewModelProviderFactory;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class ArchiveFragment extends DaggerFragment {

    @Inject
    ViewModelProviderFactory providerFactory;

    private ArchiveViewModel viewModel;


    private FavouriteViewModel favouriteViewModel;

    private final ArchiveRecyclerViewAdapter.OnDeleteCallBack onDeleteCallBack = new ArchiveRecyclerViewAdapter.OnDeleteCallBack() {
        @Override
        public void delete(final Favourite note) {

            new AlertDialog.Builder(getActivity())
                    .setTitle("Messenger:")
                    .setMessage("delete item..?")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            favouriteViewModel.delete(note);
                        }
                    }).show();
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_archive, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // For the scrolling content, you can use RecyclerView, NestedScrollView or any other
        // View that inherits NestedScrollingChild
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        final Context context = recyclerView.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        viewModel = new ViewModelProvider(this, providerFactory).get(ArchiveViewModel.class);
        favouriteViewModel = new ViewModelProvider(this).get(FavouriteViewModel.class);
        favouriteViewModel.getAllNotes().observe(this, new Observer<List<Favourite>>() {
            @Override
            public void onChanged(@Nullable List<Favourite> favourites) {
                recyclerView.setAdapter(new ArchiveRecyclerViewAdapter(context, favourites, onDeleteCallBack));
            }
        });

    }
}























package com.josef.mobile.free.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.josef.josefmobile.R;
import com.josef.mobile.data.Favourite;
import com.josef.mobile.data.FavouriteViewModel;
import com.josef.mobile.free.util.ArchiveActivityAdapter;

import java.util.List;

public class ModalFragment extends BottomSheetDialogFragment {
    private FavouriteViewModel favouriteViewModel;


    private final ArchiveActivityAdapter.OnDeleteCallBack onDeleteCallBack = new ArchiveActivityAdapter.OnDeleteCallBack() {
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
        return inflater.inflate(R.layout.fragment_modal, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // For the scrolling content, you can use RecyclerView, NestedScrollView or any other
        // View that inherits NestedScrollingChild
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        final Context context = recyclerView.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        favouriteViewModel = ViewModelProviders.of(this).get(FavouriteViewModel.class);
        favouriteViewModel.getAllNotes().observe(this, new Observer<List<Favourite>>() {
            @Override
            public void onChanged(@Nullable List<Favourite> favourites) {
                recyclerView.setAdapter(new ArchiveActivityAdapter(context, favourites, onDeleteCallBack));
            }
        });

    }

}

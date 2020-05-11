package com.josef.mobile.free.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.josef.josefmobile.R;
import com.josef.mobile.data.Favourite;
import com.josef.mobile.data.FavouriteViewModel;
import com.josef.mobile.free.util.ArchiveActivityAdapter;

import java.util.List;

public class ArchiveFragment extends Fragment{


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View mLayoutInflater;
    private String mParam1;
    private String mParam2;

    public ArchiveFragment() {
    }

    public static ArchiveFragment newInstance(String param1, String param2) {
        ArchiveFragment fragment = new ArchiveFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private FavouriteViewModel favouriteViewModel;
    private ArchiveActivityAdapter simpleAdapter;
    private RecyclerView mRecyclerView;

    private ArchiveActivityAdapter.OnDeleteCallBack onDeleteCallBack = new ArchiveActivityAdapter.OnDeleteCallBack() {
        @Override
        public void delete(final Favourite note) {
            final Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.main_content), "delete item..?", Snackbar.LENGTH_LONG)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            favouriteViewModel.delete(note);
                        }
                    }).setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
            snackbar.setAnchorView(R.id.fab);
            snackbar.show();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mLayoutInflater = inflater.inflate(R.layout.fragment_archive, container, false);
        setupRecyclerView();
        favouriteViewModel = ViewModelProviders.of(this).get(FavouriteViewModel.class);
        favouriteViewModel.getAllNotes().observe(this, new Observer<List<Favourite>>() {
            @Override
            public void onChanged(@Nullable List<Favourite> favourites) {
                simpleAdapter.setListItems(favourites);
            }
        });
        return mLayoutInflater;
    }

    private void setupRecyclerView() {
        mRecyclerView = (RecyclerView) mLayoutInflater.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        simpleAdapter = new ArchiveActivityAdapter(getContext(), null,onDeleteCallBack);
        mRecyclerView.setAdapter(simpleAdapter);
    }
}

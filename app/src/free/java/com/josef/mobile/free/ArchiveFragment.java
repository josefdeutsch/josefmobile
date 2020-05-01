package com.josef.mobile.free;

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
import com.josef.mobile.free.components.ArchiveActivityAdapter;

import java.util.List;


public class ArchiveFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View mLayoutInflater;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ArchiveFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArchiveFragment.
     */
    // TODO: Rename and change types and number of parameters
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
            final Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.bottom_app_bar_coord), "delete item..?", Snackbar.LENGTH_LONG)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            favouriteViewModel.delete(note);
                        }
                    }).setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
            View view = snackbar.getView();
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
            params.gravity = Gravity.TOP;
            view.setLayoutParams(params);
            view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
            TextView snackBarText =  snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
            snackBarText.setTextColor(ContextCompat.getColor(getContext(), R.color.colorBlack));
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

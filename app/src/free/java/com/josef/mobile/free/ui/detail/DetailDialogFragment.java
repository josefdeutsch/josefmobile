package com.josef.mobile.free.ui.detail;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.josef.josefmobile.R;
import com.josef.mobile.data.Favourite;
import com.josef.mobile.data.FavouriteViewModel;
import com.josef.mobile.free.util.ArchiveActivityAdapter;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailDialogFragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private View mView;

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
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetailDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailDialogFragment newInstance(String param1, String param2) {
        DetailDialogFragment fragment = new DetailDialogFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_detail_dialog, container, false);
        // Inflate the layout for this fragment

        View tv = mView.findViewById(R.id.text);
        ((TextView)tv).setText("This is an instance of MyDialogFragment");

     /**   final RecyclerView recyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
        final Context context = getActivity();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        favouriteViewModel = ViewModelProviders.of(this).get(FavouriteViewModel.class);
        favouriteViewModel.getAllNotes().observe(this, new Observer<List<Favourite>>() {
            @Override
            public void onChanged(@Nullable List<Favourite> favourites) {
                recyclerView.setAdapter(new ArchiveActivityAdapter(context, favourites, onDeleteCallBack));
            }
        });**/


        return mView;
    }
}
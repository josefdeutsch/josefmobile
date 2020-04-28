package com.josef.mobile.free;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.josef.josefmobile.R;
import com.josef.mobile.free.components.ArchiveActivityAdapter;
import com.josef.mobile.free.components.DeleteCallBack;

import java.util.ArrayList;


public class ArchiveFragment extends Fragment {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mLayoutInflater= inflater.inflate(R.layout.fragment_archive, container, false);
        setupRecyclerView();
        return mLayoutInflater;

    }

    private void setupRecyclerView() {
        ArrayList arrayList = getLists(new ArrayList<String>());

        RecyclerView mRecyclerView = (RecyclerView) mLayoutInflater.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        final ArchiveActivityAdapter simpleAdapter = new ArchiveActivityAdapter(getContext(), arrayList);
        mRecyclerView.setAdapter(simpleAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new DeleteCallBack(simpleAdapter,
                new DeleteCallBack.SnackBarListener() {
                    @Override
                    public void listenToAction(final int position) {
                        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) mLayoutInflater.findViewById(R.id.bottom_app_bar_coord);
                        final Snackbar snackbar = Snackbar.make(coordinatorLayout, "delete item..?", Snackbar.LENGTH_LONG)
                                .setAction("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        simpleAdapter.deleteTask(position);
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
                }));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private ArrayList<String> getLists(ArrayList<String> arrayList) {

        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/0001.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/0002.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/0003.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00010621.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00020621.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00030621.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00010622.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00020622.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00030622.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00030622.png");

        return arrayList;
    }

}

package com.josef.mobile.ui.main.archive;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.josef.mobile.R;
import com.josef.mobile.data.local.db.model.Archive;
import com.josef.mobile.ui.main.Resource;
import com.josef.mobile.viewmodels.ViewModelProviderFactory;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class ArchiveFragment extends DaggerFragment implements View.OnClickListener {

    @Inject
    ViewModelProviderFactory providerFactory;

    private ArchiveViewModel viewModel;

    private static final String TAG = "ArchiveFragment";
    private final ArchiveRecyclerViewAdapter.OnDeleteCallBack onDeleteCallBack
            = new ArchiveRecyclerViewAdapter.OnDeleteCallBack() {

        @Override
        public void delete(final Archive archive) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Messenger:")
                    .setMessage("delete item..?")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            viewModel.deleteArchives(archive);
                        }
                    }).show();
        }
    };

    private Button sync;
    private ArchiveRecyclerViewAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_archive, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        sync = view.findViewById(R.id.purchasebutton);
        sync.setOnClickListener(this);
        final RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        final Context context = recyclerView.getContext();
        adapter = new ArchiveRecyclerViewAdapter(getActivity(), onDeleteCallBack);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        viewModel = new ViewModelProvider(this, providerFactory).get(ArchiveViewModel.class);

        viewModel.observeArchive().observe(this, new Observer<Resource<List<Archive>>>() {
            @Override
            public void onChanged(Resource<List<Archive>> listResource) {
                if (listResource != null) {
                    switch (listResource.status) {
                        case LOADING: {
                            Log.d(TAG, "onChanged: ArchiveFragment: LOADING...");
                            break;
                        }

                        case SUCCESS: {
                            Log.d(TAG, "onChanged: ArchiveFragment: SUCCESS :" + listResource.data.isEmpty());
                            adapter.setListItems(listResource.data);
                            break;
                        }
                        case ERROR: {
                            Log.d(TAG, "onChanged: PostsFragment: ERROR : " + listResource.data.isEmpty());
                            break;
                        }
                    }
                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setMessage(R.string.sync);
        alert.setCancelable(false);
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alert.show();
    }
}






















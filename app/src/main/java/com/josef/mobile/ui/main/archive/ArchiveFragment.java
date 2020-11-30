package com.josef.mobile.ui.main.archive;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.josef.mobile.R;
import com.josef.mobile.data.local.db.model.Archive;
import com.josef.mobile.ui.base.BaseFragment;
import com.josef.mobile.ui.main.MainActivity;
import com.josef.mobile.utils.UtilManager;
import com.josef.mobile.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

public class ArchiveFragment extends BaseFragment implements View.OnClickListener {

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    UtilManager utilManager;

    private ArchiveViewModel viewModel;
    private RecyclerView recyclerView;
    private FloatingActionButton sync;
    private ArchiveRecyclerViewAdapter adapter;

    private final ArchiveRecyclerViewAdapter.OnDeleteCallBack onDeleteCallBack
            = new ArchiveRecyclerViewAdapter.OnDeleteCallBack() {

        @Override
        public void delete(final Archive archive) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Messenger:")
                    .setMessage("delete item..?")
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, which) -> {
                        viewModel.deleteArchives(archive);
                        viewModel.updateEndpoints(archive);
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
        sync = view.findViewById(R.id.purchasebutton);
        sync.setOnClickListener(this);
        recyclerView = view.findViewById(R.id.recycler_view);
        final Context context = recyclerView.getContext();
        adapter = new ArchiveRecyclerViewAdapter(getActivity(), onDeleteCallBack);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setAdapter(adapter);
        viewModel = new ViewModelProvider(this, providerFactory).get(ArchiveViewModel.class);

        subscribeObservers();
    }

    private void subscribeObservers() {

        viewModel.observeArchive().removeObservers(getViewLifecycleOwner());
        viewModel.observeArchive().observe(getViewLifecycleOwner(), listResource -> {
            if (listResource != null) {
                switch (listResource.status) {
                    case LOADING: {
                        utilManager.showProgressbar(getActivity());
                        break;
                    }
                    case SUCCESS: {
                        if (listResource.data != null && listResource.data.isEmpty()) {
                            archiveDatabaseRemainder();
                            utilManager.hideProgressbar();
                            return;
                        }
                        if (listResource.data != null) {
                            adapter.setListItems(listResource.data);
                        }
                        utilManager.hideProgressbar();
                        break;
                    }
                    case ERROR: {
                        utilManager.hideProgressbar();
                        break;
                    }
                }
            }
        });
    }

    private void archiveDatabaseRemainder() {
        Toast.makeText(getContext(), "Please add items to archive..", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setMessage(R.string.syncs);
        alert.setCancelable(false);
        alert.setPositiveButton(android.R.string.yes, (dialogInterface, i) -> viewModel.synchronize((MainActivity) getActivity()));
        alert.setNegativeButton(android.R.string.no, (dialogInterface, i) -> dialogInterface.dismiss());
        alert.show();
    }
}























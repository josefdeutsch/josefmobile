package com.josef.mobile.ui.main.archive;

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

import com.josef.mobile.R;
import com.josef.mobile.data.local.db.model.Archive;
import com.josef.mobile.ui.base.BaseFragment;
import com.josef.mobile.ui.main.MainActivity;
import com.josef.mobile.utils.UtilManager;
import com.josef.mobile.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ArchiveFragment extends BaseFragment implements View.OnClickListener, ArchiveRecyclerViewAdapter.OnDeleteCallBack {

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    UtilManager utilManager;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Inject
    ArchiveRecyclerViewAdapter adapter;

    ArchiveViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_archive, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        adapter.setmDeleteCallBack(this);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
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
        Toast.makeText(getContext(),
                getContext().getResources().
                        getResourceName(R.string.archive_database_remainder),
                Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.purchasebutton)
    public void onClick(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setMessage(R.string.archive_fab_alert_syncs);
        alert.setCancelable(false);
        alert.setPositiveButton(android.R.string.yes, (dialogInterface, i) -> viewModel.synchronize((MainActivity) getActivity()));
        alert.setNegativeButton(android.R.string.no, (dialogInterface, i) -> dialogInterface.dismiss());
        alert.show();
    }

    @Override
    public void delete(Archive archive) {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.archive_alert_title)
                .setMessage(R.string.archive_alert_message)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    viewModel.deleteArchives(archive);
                    viewModel.updateEndpoints(archive);
                }).show();
    }
}























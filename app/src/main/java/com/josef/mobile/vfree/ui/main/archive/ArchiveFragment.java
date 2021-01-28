package com.josef.mobile.vfree.ui.main.archive;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.josef.mobile.vfree.data.local.db.model.Archive;
import com.josef.mobile.vfree.ui.base.BaseFragment;
import com.josef.mobile.vfree.ui.err.ErrorActivity;
import com.josef.mobile.vfree.ui.main.MainActivity;
import com.josef.mobile.vfree.utils.UtilManager;
import com.josef.mobile.vfree.viewmodels.ViewModelProviderFactory;
import com.josef.mobile.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.josef.mobile.vfree.ui.err.ErrorActivity.ACTIVITY_KEYS;

public class ArchiveFragment extends BaseFragment
        implements View.OnClickListener, ArchiveRecyclerViewAdapter.OnDeleteCallBack {

    @NonNull
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    UtilManager utilManager;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.adView)
    AdView mAdView;

    @Inject
    ArchiveRecyclerViewAdapter adapter;

    ArchiveViewModel viewModel;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_archive, container, false);
        ButterKnife.bind(this, view);

        loadBanner();
       // mHandler.postDelayed(() -> hideBanner(),
           //     3000);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        adapter.setmDeleteCallBack(this);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapter);
        viewModel = new ViewModelProvider(this, providerFactory)
                .get(ArchiveViewModel.class);
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
                        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(getActivity(),
                                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();

                        Intent intent = new Intent(getActivity(), ErrorActivity.class);
                        intent.putExtra(ACTIVITY_KEYS, getActivity().getComponentName().getClassName());

                        startActivity(intent, bundle);

                        getActivity().finishAfterTransition();
                        break;
                    }
                }
            }
        });
    }

    private void archiveDatabaseRemainder() {
        Toast.makeText(getContext(),
                getContext().getResources().
                        getString(R.string.archive_database_remainder),
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

    private void loadBanner() {
        AdRequest adRequest = new AdRequest.Builder().build();

        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }


}























package com.josef.mobile.vfree.ui.main.post;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.josef.mobile.vfree.ui.err.ErrorActivity;
import com.josef.mobile.vfree.ui.main.archive.model.Archive;
import com.josef.mobile.vfree.ui.main.post.model.LocalCache;
import com.josef.mobile.vfree.data.local.prefs.PreferencesHelper;
import com.josef.mobile.vfree.ui.base.BaseFragment;
import com.josef.mobile.vfree.ui.player.PlayerActivity;
import com.josef.mobile.vfree.utils.AppConstants;
import com.josef.mobile.R;

import javax.inject.Inject;

import static android.content.ContentValues.TAG;
import static android.widget.NumberPicker.OnScrollListener.SCROLL_STATE_IDLE;
import static com.josef.mobile.vfree.ui.base.Base.REQUEST_ENDPOINT;
import static com.josef.mobile.vfree.ui.base.Base.REQUEST_INDEX;


public final class PostsFragment extends BaseFragment
        implements PostRecyclerAdapter.PostRecyclerViewOnClickListener {


    @Inject
    PostRecyclerAdapter adapter;

    @NonNull
    private PostsViewModel viewModel;
    @NonNull
    private RecyclerView recyclerView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_view);
        adapter.setPostRecyclerViewOnClickListener(this);
        viewModel = new ViewModelProvider(this, providerFactory).get(PostsViewModel.class);

        viewModel.initiateInsterstitialAds(getActivity());

        initRecyclerView();
        subscribeObservers();

    }


    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

    }

    private void subscribeObservers() {
        viewModel.observeEndpoints().removeObservers(getViewLifecycleOwner());
        viewModel.observeEndpoints().observe(getViewLifecycleOwner(), listResource -> {
            if (listResource != null) {
                switch (listResource.status) {
                    case LOADING: {
                        showProgressbar(getActivity());
                        break;
                    }
                    case SUCCESS: {
                        if(listResource.data != null && !listResource.data.isEmpty()){
                            adapter.setPosts(listResource.data);
                        }
                        hideProgessbar();
                        break;
                    }
                    case ERROR: {
                        hideProgessbar();
                        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(getActivity(),
                                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();

                        Intent intent = new Intent(getActivity(), ErrorActivity.class);
                        intent.putExtra(ErrorActivity.ACTIVITY_KEYS, getActivity().getComponentName().getClassName());

                        startActivity(intent, bundle);

                        getActivity().finishAfterTransition();
                        break;
                    }
                }
            }
        });
    }


    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }


    @Override
    public void onClick(@NonNull int position) {
        Intent intent = new Intent(getActivity(), PlayerActivity.class);
        intent.putExtra(REQUEST_INDEX, position);
        intent.putExtra(REQUEST_ENDPOINT, AppConstants.ENDPOINT_1);
        startActivity(intent);
    }

    @Override
    public void onBuy(@NonNull LocalCache favourite) {
        if (favourite.getBuy() != null && !favourite.getBuy().isEmpty()) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(favourite.getBuy()));
            startActivity(i);
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setMessage(R.string.post_no_auction_available);
            alert.setCancelable(false);
            alert.setNegativeButton(android.R.string.no, (dialogInterface, i) -> dialogInterface.dismiss());
            alert.show();
        }

    }

    @Override
    public void onChecked(@NonNull Boolean isChecked,
                          @NonNull LocalCache favourite) {

        if (dataManager.getHashMapArchiveIndicator().equals(PreferencesHelper.ARCHIVE_EMPTY)) {
            Toast.makeText(getContext(), getContext().getResources()
                    .getString(R.string.fragment_posts_added_sculpture_remainder), Toast.LENGTH_SHORT)
                    .show();
            dataManager.setHashMapArchiveIndicator(PreferencesHelper.ARCHIVE_NOT_EMPTY);
        }

        Archive archive = new Archive(
                favourite.getId(),
                favourite.isFlag(),
                favourite.getName(),
                favourite.getPng(),
                favourite.getUrl(),
                favourite.getTag()
        );

        if (isChecked) {
            viewModel.insertArchives(archive);
        }
        if (!isChecked) {
            viewModel.deleteArchives(archive);
        }

    }

    @Override
    public void isFlagged(@NonNull Boolean isChecked,
                          @NonNull LocalCache favourite) {
        if (isChecked) {
            favourite.flag = true;
            viewModel.updateEndpoints(favourite);
            if (!recyclerView.isComputingLayout() && RecyclerView.SCROLL_STATE_IDLE == SCROLL_STATE_IDLE) {
                recyclerView.post(() -> adapter.notifyDataSetChanged());
            }
        }
        if (!isChecked) {
            favourite.flag = false;
            viewModel.updateEndpoints(favourite);
            if (!recyclerView.isComputingLayout() && RecyclerView.SCROLL_STATE_IDLE == SCROLL_STATE_IDLE) {
                recyclerView.post(() -> adapter.notifyDataSetChanged());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.onDetachedFromRecyclerView(recyclerView);
        recyclerView.clearOnScrollListeners();
    }
}
























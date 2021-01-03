package com.josef.josefmobile.ui.main.post;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.josef.josefmobile.data.local.db.model.Archive;
import com.josef.josefmobile.data.local.db.model.LocalCache;
import com.josef.josefmobile.data.local.prefs.PreferencesHelper;
import com.josef.josefmobile.ui.base.BaseFragment;
import com.josef.josefmobile.ui.err.ErrorActivity;
import com.josef.josefmobile.ui.player.PlayerActivity;
import com.josef.josefmobile.utils.AppConstants;
import com.josef.mobile.R;

import javax.inject.Inject;

import static android.widget.NumberPicker.OnScrollListener.SCROLL_STATE_IDLE;


public class PostsFragment extends BaseFragment
        implements PostRecyclerAdapter.PostRecyclerViewOnClickListener {


    @Inject
    PostRecyclerAdapter adapter;

    private PostsViewModel viewModel;
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
                        adapter.setPosts(listResource.data);
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
    public void onClick(int position) {
        Intent intent = new Intent(getActivity(), PlayerActivity.class);
        intent.putExtra(AppConstants.REQUEST_INDEX, position);
        startActivityForResult(intent, AppConstants.PLAYERACTIVIY);
    }

    @Override
    public void onChecked(Boolean isChecked, LocalCache favourite) {

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
    public void isFlagged(Boolean isChecked, LocalCache favourite) {
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
























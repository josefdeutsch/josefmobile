package com.josef.mobile.ui.main.post;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.josef.mobile.R;
import com.josef.mobile.data.local.db.model.Archive;
import com.josef.mobile.ui.main.Resource;
import com.josef.mobile.ui.main.post.model.Container;
import com.josef.mobile.viewmodels.ViewModelProviderFactory;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


public class PostsFragment extends DaggerFragment implements PostRecyclerAdapter.PostRecyclerViewOnClickListener {

    private static final String TAG = "PostsFragment";

    PostRecyclerAdapter adapter;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    RequestManager requestManager;

    private PostsViewModel viewModel;

    private RecyclerView recyclerView;

    private int index = 0;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            index = savedInstanceState.getInt("index");
        }
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
        adapter = new PostRecyclerAdapter(getActivity(), requestManager, this);
        viewModel = new ViewModelProvider(this, providerFactory).get(PostsViewModel.class);
        initRecyclerView();
        subscribeObservers();
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("index", index);
    }

    private void subscribeObservers() {
        viewModel.observeResource().removeObservers(getViewLifecycleOwner());
        viewModel.observeResource().observe(getViewLifecycleOwner(), new Observer<Resource<List<Container>>>() {
            @Override
            public void onChanged(Resource<List<Container>> listResource) {
                if (listResource != null) {
                    switch (listResource.status) {
                        case LOADING: {
                            Log.d(TAG, "onChanged: PostsFragment: LOADING...");
                            break;
                        }
                        case SUCCESS: {
                            adapter.setPosts(listResource.data);
                            break;
                        }
                        case ERROR: {
                            Log.d(TAG, "onChanged: PostsFragment: ERROR... " + listResource.message);
                            break;
                        }
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
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    }


    @Override
    public void onClick(Container container) {

    }

    @Override
    public void onChecked(Boolean isChecked, Container favourite) {
        if (isChecked) {
            Log.d(TAG, "onChecked: ");
            viewModel.insertArchives(new Archive("uschi", favourite.getPng(), favourite.getUrl()));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recyclerView.clearOnScrollListeners();
    }

    /**  @Override public void onChecked(Boolean isChecked, Container container) {
    if (isChecked) {
    } else {
    }
    }**/
}























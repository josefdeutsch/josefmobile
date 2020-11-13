package com.josef.mobile.ui.main.post.content.second;

import android.content.Intent;
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
import com.josef.mobile.data.DataManager;
import com.josef.mobile.ui.base.BaseFragment;
import com.josef.mobile.ui.main.Resource;
import com.josef.mobile.ui.main.archive.model.Archive;
import com.josef.mobile.ui.main.post.PostRecyclerAdapter;
import com.josef.mobile.ui.main.post.model.Container;
import com.josef.mobile.ui.player.PlayerActivity;
import com.josef.mobile.utils.UtilManager;
import com.josef.mobile.viewmodels.ViewModelProviderFactory;

import java.util.List;

import javax.inject.Inject;

import static com.josef.mobile.utils.AppConstants.PLAYERACTIVIY;
import static com.josef.mobile.utils.AppConstants.REQUEST_INDEX;


public class SecondPostFragment extends BaseFragment
        implements PostRecyclerAdapter.PostRecyclerViewOnClickListener {

    private static final String TAG = "PostsFragment";
    @Inject
    RequestManager requestManager;

    @Inject
    DataManager dataManager;

    @Inject
    UtilManager utilManager;


    PostRecyclerAdapter adapter;

    @Inject
    ViewModelProviderFactory providerFactory;

    private SecondPostFragmentViewModel viewModel;

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
        adapter = new PostRecyclerAdapter(requestManager, dataManager, utilManager);
        adapter.setPostRecyclerViewOnClickListener(this);
        viewModel = new ViewModelProvider(this, providerFactory).get(SecondPostFragmentViewModel.class);

        initRecyclerView();
        subscribeObservers();

    }


    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

    }

    private void subscribeObservers() {
        viewModel.observeEndpoints().removeObservers(getViewLifecycleOwner());
        viewModel.observeEndpoints().observe(getViewLifecycleOwner(), new Observer<Resource<List<Container>>>() {
            @Override
            public void onChanged(Resource<List<Container>> listResource) {
                if (listResource != null) {
                    switch (listResource.status) {
                        case LOADING: {
                            Log.d(TAG, "onChanged: PostsFragment: LOADING...");
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
                            getActivity().finish();
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
    public void onClick(int position) {
        Intent intent = new Intent(getActivity(), PlayerActivity.class);
        intent.putExtra(REQUEST_INDEX, position);
        startActivityForResult(intent, PLAYERACTIVIY);
    }

    @Override
    public void onChecked(int position, Boolean isChecked, Container favourite) {
        long id = favourite.getId();
        Archive archive = new Archive(id, "default", favourite.getPng(), favourite.getUrl());
        if (isChecked) {
            viewModel.insertArchives(archive);
        }
        if (!isChecked) {
            viewModel.deleteArchives(archive);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.onDetachedFromRecyclerView(recyclerView);
    }
}






















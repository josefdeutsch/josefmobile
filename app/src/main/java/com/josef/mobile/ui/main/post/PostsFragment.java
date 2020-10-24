package com.josef.mobile.ui.main.post;

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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.josef.mobile.R;
import com.josef.mobile.data.local.db.dao.Archive;
import com.josef.mobile.models.Change;
import com.josef.mobile.models.Container;
import com.josef.mobile.ui.main.Resource;
import com.josef.mobile.ui.player.PlayerActivity;
import com.josef.mobile.viewmodels.ViewModelProviderFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

import static com.josef.mobile.util.Constants.PLAYERACTIVIY;
import static com.josef.mobile.util.Constants.REQUEST_PNG;
import static com.josef.mobile.util.Constants.REQUEST_URL;

public class PostsFragment extends DaggerFragment implements PostRecyclerAdapter.PostRecyclerViewOnClickListener {

    private static final String TAG = "PostsFragment";


    PostRecyclerAdapter adapter;

    @Inject
    ViewModelProviderFactory providerFactory;

    private PostsViewModel viewModel;

    private RecyclerView recyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new PostRecyclerAdapter(getActivity(), this);

        viewModel = new ViewModelProvider(this, providerFactory).get(PostsViewModel.class);


        initRecyclerView();
        subscribeObservers();

    }

    private void subscribeObservers() {
        viewModel.observePosts(1).removeObservers(getViewLifecycleOwner());
        viewModel.observePosts(1).observe(getViewLifecycleOwner(), new Observer<Resource<Change>>() {
            @Override
            public void onChanged(Resource<Change> listResource) {
                if (listResource != null) {
                    switch (listResource.status) {
                        case LOADING: {
                            Log.d(TAG, "onChanged: PostsFragment: LOADING...");
                            break;
                        }

                        case SUCCESS: {
                            Log.d(TAG, "onChanged: " + listResource.data.message);
                            Gson gson = new Gson();
                            Type userListType = new TypeToken<ArrayList<Container>>() {
                            }.getType();
                            ArrayList<Container> userArray = gson.fromJson(listResource.data.message, userListType);
                           // Collections.shuffle(userArray);
                            adapter.setPosts(userArray);
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
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onClick(Container container) {
        Intent intent = new Intent(getActivity(), PlayerActivity.class);
        intent.putExtra(REQUEST_PNG, container.getPng());
        intent.putExtra(REQUEST_URL, container.getUrl());
        startActivityForResult(intent, PLAYERACTIVIY);
    }

    @Override
    public void onChecked(Boolean isChecked, Container favourite) {
        if (isChecked) {
            Log.d(TAG, "onChecked: ");
            viewModel.insertArchives(new Archive("uschi", favourite.getPng(), favourite.getUrl()));
        }
    }

    /**  @Override public void onChecked(Boolean isChecked, Container container) {
    if (isChecked) {
    } else {
    }
    }**/
}























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

import com.josef.mobile.R;
import com.josef.mobile.data.local.db.model.Archive;
import com.josef.mobile.data.local.db.model.LocalCache;
import com.josef.mobile.ui.base.BaseFragment;
import com.josef.mobile.ui.main.Resource;
import com.josef.mobile.ui.player.PlayerActivity;
import com.josef.mobile.viewmodels.ViewModelProviderFactory;

import java.util.List;

import javax.inject.Inject;

import static android.widget.NumberPicker.OnScrollListener.SCROLL_STATE_IDLE;
import static com.josef.mobile.utils.AppConstants.PLAYERACTIVIY;
import static com.josef.mobile.utils.AppConstants.REQUEST_INDEX;


public class PostsFragment extends BaseFragment
        implements PostRecyclerAdapter.PostRecyclerViewOnClickListener {

    private static final String TAG = "PostsFragment";

    @Inject
    PostRecyclerAdapter adapter;

    @Inject
    ViewModelProviderFactory providerFactory;

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
        viewModel.observeEndpoints().observe(getViewLifecycleOwner(), new Observer<Resource<List<LocalCache>>>() {
            @Override
            public void onChanged(Resource<List<LocalCache>> listResource) {
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
        // recyclerView.addItemDecoration(new ItemOffsetDecoration(0));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);


    }


    @Override
    public void onClick(int position) {
        Intent intent = new Intent(getActivity(), PlayerActivity.class);
        intent.putExtra(REQUEST_INDEX, position);
        startActivityForResult(intent, PLAYERACTIVIY);
    }

    //  public Archive(long id, ,boolean flag, String name, String png, String url, String tag) {
    @Override
    public void onChecked(Boolean isChecked, LocalCache favourite) {

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
























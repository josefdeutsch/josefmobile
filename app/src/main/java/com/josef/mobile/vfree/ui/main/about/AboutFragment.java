package com.josef.mobile.vfree.ui.main.about;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.josef.mobile.R;
import com.josef.mobile.vfree.ui.base.BaseFragment;
import com.josef.mobile.vfree.ui.err.ErrorActivity;
import com.josef.mobile.vfree.ui.main.about.adapters.AboutRecyclerViewAdapter;
import com.josef.mobile.vfree.utils.AppConstants;
import javax.inject.Inject;

import static android.content.ContentValues.TAG;

public class AboutFragment extends BaseFragment {

    @Inject
    AboutRecyclerViewAdapter adapter;

    private AboutViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, providerFactory).get(AboutViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView(view);
        // 2x times - refresh
        subscribeObservers();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void initRecyclerView(@NonNull View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    private void subscribeObservers() {
        viewModel.observeEndpoints(AppConstants.ENDPOINT_2).removeObservers(getViewLifecycleOwner());
        viewModel.observeEndpoints(AppConstants.ENDPOINT_2).observe(getViewLifecycleOwner(), listResource -> {
            if (listResource != null) {
                switch (listResource.status) {
                    case LOADING: {
                        showProgressbar(getActivity());
                        break;
                    }
                    case SUCCESS: {
                        adapter.setAbouts(listResource.data);
                        hideProgessbar();
                        break;
                    }
                    case ERROR: {
                        Log.d(TAG, "subscribeObservers: "+listResource.message);
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

}
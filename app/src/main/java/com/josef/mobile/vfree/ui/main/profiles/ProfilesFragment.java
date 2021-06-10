package com.josef.mobile.vfree.ui.main.profiles;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.josef.mobile.R;
import com.josef.mobile.vfree.ui.base.BaseFragment;
import com.josef.mobile.vfree.ui.err.ErrorActivity;
import com.josef.mobile.vfree.ui.main.post.PostsViewModel;
import com.josef.mobile.vfree.ui.main.profiles.adapters.ProfileRecyclerViewAdapter;
import com.josef.mobile.vfree.ui.main.profiles.model.Profile;
import com.josef.mobile.vfree.utils.AppConstants;

import javax.inject.Inject;

public final class ProfilesFragment extends BaseFragment implements ProfileRecyclerViewAdapter.ProfileRecyclerViewOnClickListener {

    private static final String TAG = "ProfilesFragment";

    @Inject
    ProfileRecyclerViewAdapter adapter;

    private ProfilesViewModel viewModel;

    @NonNull
    private RecyclerView recyclerView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, providerFactory).get(ProfilesViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profiles, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_view);
        adapter.setProfileRecyclerViewOnClickListener(this);
        initRecyclerView();
        subscribeObservers();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    private void subscribeObservers() {
        viewModel.observeEndpoints(AppConstants.ENDPOINT_4).removeObservers(getViewLifecycleOwner());
        viewModel.observeEndpoints(AppConstants.ENDPOINT_4).observe(getViewLifecycleOwner(), listResource -> {
            if (listResource != null) {
                switch (listResource.status) {
                    case LOADING: {
                        showProgressbar(getActivity());
                        break;
                    }
                    case SUCCESS: {
                        if(listResource.data != null && !listResource.data.isEmpty()){
                            adapter.setAbouts(listResource.data);
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

    @Override
    public void showProgressbar(@NonNull Activity activity) {
        super.showProgressbar(activity);
    }

    @Override
    public void onClick(@NonNull Profile profile) {
        Log.d(TAG, "onClick: "+profile.getUrl_content());
        if (profile.getUrl_content() != null && !profile.getUrl_content().isEmpty()) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(profile.getUrl_content()));
            startActivity(i);
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setMessage(R.string.profile_no_action_available);
            alert.setCancelable(false);
            alert.setNegativeButton(android.R.string.no, (dialogInterface, i) -> dialogInterface.dismiss());
            alert.show();
        }

    }
}
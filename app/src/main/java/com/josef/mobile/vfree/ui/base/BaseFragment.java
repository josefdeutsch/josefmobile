package com.josef.mobile.vfree.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.josef.mobile.vfree.data.DataManager;
import com.josef.mobile.vfree.utils.UtilManager;
import com.josef.mobile.vfree.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public abstract class BaseFragment extends DaggerFragment {

    @NonNull
    @Inject
    public UtilManager utilManager;

    @NonNull
    @Inject
    public DataManager dataManager;

    @NonNull
    @Inject
    public ViewModelProviderFactory providerFactory;

    public void showProgressbar(@NonNull Activity activity) {
        utilManager.showProgressbar(activity);
    }

    public void hideProgessbar() {
        utilManager.hideProgressbar();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);

    }

}

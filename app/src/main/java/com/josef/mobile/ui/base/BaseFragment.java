package com.josef.mobile.ui.base;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.josef.mobile.R;
import com.josef.mobile.utils.UtilManager;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public abstract class BaseFragment extends DaggerFragment {

    public Activity activity;

    @Inject
    UtilManager utilManager;

    Dialog dialog;

    public void showProgressbar(Activity activity) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.progress_dialog, null);
        dialog = utilManager.getDialog(activity);
        dialog.addContentView(view.findViewById(R.id.fullprogressbar), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        dialog.show();
    }

    public void hideProgessbar() {
        dialog.dismiss();
    }


}

package com.josef.mobile.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.google.firebase.database.annotations.NotNull;
import com.josef.mobile.SessionManager;
import com.josef.mobile.data.DataManager;
import com.josef.mobile.ui.auth.AuthActivity;
import com.josef.mobile.utils.UtilManager;
import com.josef.mobile.utils.work.CallBackWorker;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

import static com.josef.mobile.ui.auth.AuthActivity.RC_SIGN_OUT;
import static com.josef.mobile.utils.work.Worker.WORKREQUEST_INDICATOR;
import static com.josef.mobile.utils.work.Worker.WORKREQUEST_KEYTASK_ERROR;

public abstract class BaseActivity extends DaggerAppCompatActivity {

    Activity activity;

    @Inject
    public SessionManager sessionManager;

    @Inject
    public DataManager dataManager;

    @Inject
    public UtilManager utilManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        verifyNetworkResponse();
        subscribeToSessionManager();
    }

    private void verifyNetworkResponse() {
        String activity = getActivityName();
        Constraints constraints = buildNetworkConstraints();
        Data data = buildData(activity);
        OneTimeWorkRequest onetimeJob = buildOneTimeRequest(constraints, data);
        ping(onetimeJob);
    }

    @org.jetbrains.annotations.NotNull
    private Constraints buildNetworkConstraints() {
        return new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
    }

    @org.jetbrains.annotations.NotNull
    private Data buildData(String activity) {
        return new Data.Builder()
                .putString(WORKREQUEST_INDICATOR, activity)
                .build();
    }

    @org.jetbrains.annotations.NotNull
    private OneTimeWorkRequest buildOneTimeRequest(Constraints constraints, Data data) {
        return new OneTimeWorkRequest.Builder(CallBackWorker.class)
                .setConstraints(constraints)
                .setInputData(data)
                .build();
    }

    private void ping(OneTimeWorkRequest onetimeJob) {
        WorkManager.getInstance(this).enqueue(onetimeJob);
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(onetimeJob.getId())
                .observe(this, workInfo -> {
                    if (workInfo != null) {
                        if (workInfo.getState().isFinished()) {
                            if (getCallBackResult(workInfo) != null) finish();
                        }
                    }
                });
    }

    private String getActivityName() {
        activity = this;
        return activity.getComponentName().getClassName();
    }

    protected String getCallBackResult(@NotNull WorkInfo workInfo) throws IllegalArgumentException {
        Data data = workInfo.getOutputData();
        return data.getString(WORKREQUEST_KEYTASK_ERROR);
    }

    public void subscribeToSessionManager() {
        if (sessionManager.getAuthUser() == null) return;
        sessionManager.getAuthUser().observe(this, userAuthResource -> {
            if (userAuthResource != null) {
                switch (userAuthResource.status) {
                    case NOT_AUTHENTICATED: {
                        navLoginScreen();
                        break;
                    }
                }
            }
        });
    }

    private void navLoginScreen() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivityForResult(intent, RC_SIGN_OUT);
        finish();
    }

}

















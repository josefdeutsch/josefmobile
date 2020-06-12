package com.josef.mobile.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.test.espresso.IdlingResource;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.Operation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.josef.josefmobile.R;
import com.josef.mobile.free.ContentActivity;
import com.josef.mobile.free.GoogleSignInActivity;
import com.josef.mobile.idlingres.EspressoIdlingResource;
import com.josef.mobile.util.CallBackWorker;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.josef.mobile.util.Config.VIEWPAGER_AMOUNT;
import static com.josef.mobile.util.Config.WORKREQUEST_KEYTAST_OUTPUT;
import static com.josef.mobile.util.Config.SHAREDPREFERENCES_LOCK_INDEX;
import static com.josef.mobile.util.Config.SHAREDPREFERENCES_EDITOR;
import static com.josef.mobile.util.Config.WORKREQUEST_AMOUNT;
import static com.josef.mobile.util.Config.WORKREQUEST_LIST;
import static com.josef.mobile.util.Config.WORKREQUET_CONTENTACTIVITY;
import static com.josef.mobile.util.Config.WORKREQUET_SPLASHACTIVITY;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    public Data mData;
    public Constraints mConstraints;
    public OneTimeWorkRequest mDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setTransparentStatusBarLollipop();

       // supplySharedPreferences();
      //  onUpdateAppWidgetProvider();


        mData = buildData();
        mConstraints = buildConstraints();
        mDownload = buildOneTimeWorkRequest(CallBackWorker.class,
                mConstraints, mData);

        executeWorkRequest();

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(UUID.fromString(String.valueOf(mDownload.getId())))
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable final WorkInfo workInfo) {
                        if (workInfo != null) {
                            if (workInfo.getState().isFinished()) {

                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        String amount = getAmountofViewpager(workInfo);
                                        List<OneTimeWorkRequest> list = new ArrayList<>();
                                        ArrayList<String> downloadId = new ArrayList<>();
                                        Log.d(TAG, "run: "+amount);
                                        for (int index = 1; index <= Integer.parseInt(amount); index++) {
                                            Data data = buildData(index);
                                            Constraints constraints = buildConstraints();
                                            OneTimeWorkRequest request = buildOneTimeWorkRequest(CallBackWorker.class,
                                                    constraints, data);
                                            list.add(request);
                                            downloadId.add(request.getStringId());
                                        }
                                        WorkManager.getInstance(SplashActivity.this).beginUniqueWork(WORKREQUET_CONTENTACTIVITY,
                                                ExistingWorkPolicy.KEEP, list).enqueue();
                                        Intent intent = new Intent(getApplicationContext(), ContentActivity.class);
                                        intent.putExtra(VIEWPAGER_AMOUNT, Integer.parseInt(amount));
                                        intent.putStringArrayListExtra(WORKREQUEST_LIST, downloadId);
                                        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                                        getApplicationContext().startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                        }
                    }
                });

    }

    private void supplySharedPreferences() {
        SharedPreferences.Editor editor = this.getSharedPreferences(SHAREDPREFERENCES_EDITOR, MODE_PRIVATE).edit();
        editor.remove(SHAREDPREFERENCES_LOCK_INDEX);
        editor.putInt(SHAREDPREFERENCES_LOCK_INDEX, 1);
        editor.apply();
    }

    private void onUpdateAppWidgetProvider() {
        Intent intent = new Intent(this, AppWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(this).getAppWidgetIds(new ComponentName(this, AppWidgetProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        this.sendBroadcast(intent);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setTransparentStatusBarLollipop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            setTransparentStatusBarMarshmallow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.transparent_statusbar));
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void setTransparentStatusBarMarshmallow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        } else {
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    private void executeWorkRequest() {

        WorkManager.getInstance(getApplicationContext()).beginUniqueWork(WORKREQUET_SPLASHACTIVITY,
                ExistingWorkPolicy.KEEP, mDownload).enqueue().getState().observe(this, new Observer<Operation.State>() {
            @Override
            public void onChanged(Operation.State state) {
              //  Toast.makeText(getApplicationContext(), state.toString(),
                //        Toast.LENGTH_LONG).show();
            }
        });

    }

    private OneTimeWorkRequest buildOneTimeWorkRequest(final Class clazz, final Constraints constraints, final Data data) {
        return new OneTimeWorkRequest.Builder(clazz)
                .setConstraints(constraints)
                .setInputData(data)
                .build();
    }

    private Constraints buildConstraints() {
        return new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
    }

    private Data buildData() {
        return new Data.Builder()
                .build();
    }

    private Data buildData(int index) {
        return new Data.Builder()
                .putInt(WORKREQUEST_AMOUNT, index)
                .build();
    }

    @Nullable
    private String getAmountofViewpager(WorkInfo workInfo) {
        Data data = workInfo.getOutputData();
        String output = data.getString(WORKREQUEST_KEYTAST_OUTPUT);
        return output;
    }

    @Nullable
    private IdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = EspressoIdlingResource.getIdlingResource();
        }
        return mIdlingResource;
    }
}

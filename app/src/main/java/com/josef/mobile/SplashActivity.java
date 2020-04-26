package com.josef.mobile;

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
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.josef.josefmobile.R;
import com.josef.mobile.idlingres.EspressoIdlingResource;
import com.josef.mobile.net.CallBackWorkerSplashActivity;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static com.josef.mobile.Config.KEY_TASK_OUTPUT;
import static com.josef.mobile.Config.VIEWPAGER_AMOUNT;
import static com.josef.mobile.Config.WORKREQUET_MAINACTIVITY;

public class SplashActivity extends AppCompatActivity {

    public Data mData;
    public Constraints mConstraints;
    public OneTimeWorkRequest mDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setTransparentStatusBarLollipop();
        mData = buildData();
        mConstraints = buildConstraints();
        mDownload = buildOneTimeWorkRequest();

        executeWorkRequest();
    }

    public void performMainActivity(View view) {
        //Intent intent = new Intent(this, MainActivity.class);
        //startActivity(intent);
        EspressoIdlingResource.increment();
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(UUID.fromString(String.valueOf(mDownload.getId())))
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {
                        if (workInfo != null) {
                            if (workInfo.getState().isFinished()) {
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                String amount = getAmountofViewpager(workInfo);
                                intent.putExtra(VIEWPAGER_AMOUNT,Integer.parseInt(amount));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                                    EspressoIdlingResource.decrement();
                                }
                                getApplicationContext().startActivity(intent);
                            }
                        }
                    }
                });
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
        WorkManager.getInstance(getApplicationContext()).beginUniqueWork(WORKREQUET_MAINACTIVITY,
                ExistingWorkPolicy.KEEP, mDownload).enqueue().getState().observe(this, new Observer<Operation.State>() {
            @Override
            public void onChanged(Operation.State state) {
                //Toast.makeText(getApplicationContext(), state.toString(),
                //     Toast.LENGTH_LONG).show();
            }
        });
    }
    private OneTimeWorkRequest buildOneTimeWorkRequest() {
        return new OneTimeWorkRequest.Builder(CallBackWorkerSplashActivity.class)
                .setConstraints(mConstraints)
                .setInputData(mData)
                .build();
    }

    @NotNull
    private Constraints buildConstraints() {
        return new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
    }

    private Data buildData() {
        return new Data.Builder()
                .build();
    }

    @Nullable
    private String getAmountofViewpager(@NotNull WorkInfo workInfo) {
        Data data = workInfo.getOutputData();
        String output = data.getString(KEY_TASK_OUTPUT);
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

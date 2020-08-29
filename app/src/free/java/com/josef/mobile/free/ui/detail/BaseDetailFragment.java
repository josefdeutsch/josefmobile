package com.josef.mobile.free.ui.detail;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.test.espresso.IdlingResource;
import androidx.work.Data;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.josef.mobile.idlingres.EspressoIdlingResource;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static com.josef.mobile.util.Config.WORKREQUEST_KEYTAST_OUTPUT;

public class BaseDetailFragment extends Fragment {

    protected ProgressBar mProgressBar;

    public void doWork(final String downloadId, final int index, final Worker worker) {
        if (downloadId != null) {

            mProgressBar.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);

            WorkManager.getInstance(getActivity()).getWorkInfoByIdLiveData(UUID.fromString(downloadId))
                    .observe(getViewLifecycleOwner(), new Observer<WorkInfo>() {
                        @Override
                        public void onChanged(@Nullable WorkInfo workInfo) {
                            if (workInfo != null) {
                                if (workInfo.getState().isFinished()) {
                                    final String output = getViewPagerContent(workInfo);
                                    if (worker != null) {
                                        worker.execute(output, index);
                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                mProgressBar.setVisibility(View.INVISIBLE);
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    });
            }

    }

    @Nullable
    protected String getViewPagerContent(@NotNull WorkInfo workInfo) {
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

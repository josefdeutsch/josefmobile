package com.josef.mobile.free.ui.detail;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.DialogFragment;
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

public class ContentBaseFragment extends Fragment {

    protected ViewModelDetail mViewModelDetail;
    protected ProgressBar mProgressBar;
    protected String mDownloadId;
    protected int index;
    protected volatile Object lock;


    protected void doWork(final Worker worker) {
        if (mDownloadId == null || worker == null) return;

        mProgressBar.setVisibility(View.VISIBLE);

        WorkManager.getInstance(getActivity()).getWorkInfoByIdLiveData(UUID.fromString(mDownloadId))
                .observe(getViewLifecycleOwner(), new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {
                        if (workInfo == null) return;
                        if (workInfo.getState().isFinished()){
                            final String output = getViewPagerContent(workInfo);
                            worker.execute(output, index);
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    }
                });


    }

    protected String getViewPagerContent(@NotNull WorkInfo workInfo) throws IllegalArgumentException {
        Data data = workInfo.getOutputData();
        String output = data.getString(WORKREQUEST_KEYTAST_OUTPUT);
        if(data == null || output.isEmpty()) throw new IllegalArgumentException("workinfo is empty");
        return output;
    }

    void showDialog() {
        DialogFragment newFragment = DetailDialogFragment.newInstance(null, null);
        newFragment.show(getFragmentManager(), "dialog");
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

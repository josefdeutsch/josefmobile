package com.josef.mobile.free.ui.detail;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.google.android.material.snackbar.Snackbar;
import com.josef.josefmobile.R;
import com.josef.mobile.data.Favourite;
import com.josef.mobile.data.FavouriteViewModel;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.util.UUID;

import static android.os.Looper.getMainLooper;
import static com.josef.mobile.ui.ErrorActivity.TAG;
import static com.josef.mobile.util.Config.WORKREQUEST_KEYTAST_OUTPUT;

public class ContentBaseFragment extends Fragment {


    private static final String TAG = "ContentBaseFragment";

    protected ViewModelDetail mViewModelDetail;
    protected FavouriteViewModel mFavouriteViewModel;
    protected ProgressBar mProgressBar;
    protected String mDownloadId;
    protected int index;
    protected volatile Object lock;


    protected void doWork(final Worker worker) {
        if (mDownloadId == null || worker == null) return;

        mProgressBar.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);

        WorkManager.getInstance(getActivity()).getWorkInfoByIdLiveData(UUID.fromString(mDownloadId))
                .observe(getViewLifecycleOwner(), new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {
                        if (workInfo == null) return;
                        if (workInfo.getState().isFinished()) {
                            final String output = getViewPagerContent(workInfo);

                            buildHandler(getMainLooper(), new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setVisibility(View.GONE);
                                }
                            },0l);

                            try {
                                worker.execute(output, index);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    protected String getViewPagerContent(@NotNull WorkInfo workInfo) throws IllegalArgumentException {
        Data data = workInfo.getOutputData();
        String output = data.getString(WORKREQUEST_KEYTAST_OUTPUT);
        if (data == null || output.isEmpty())
            throw new IllegalArgumentException("workinfo is empty");
        return output;
    }

    protected Snackbar buildSnackBar() {
        return Snackbar.make(
                getActivity().findViewById(R.id.main_content), "save item..?!", Snackbar.LENGTH_LONG);
    }
    protected void buildDialog(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DialogFragment dialogFragment = new ContentDialogFragment();
        dialogFragment.show(ft, "dialog");
    }

    protected void buildHandler(Looper looper, Runnable runnable, Long delayMillis) {
        if (looper == null) new Handler().postDelayed(runnable, delayMillis);
        new Handler(looper).post(runnable);
    }
}


    /* @Nullable
    private IdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = EspressoIdlingResource.getIdlingResource();
        }
        return mIdlingResource;
    }*/

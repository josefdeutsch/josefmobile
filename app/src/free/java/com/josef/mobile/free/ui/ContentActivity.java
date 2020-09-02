package com.josef.mobile.free.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.ToolbarWidgetWrapper;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ShareCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentTransaction;
import androidx.test.espresso.IdlingResource;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.snackbar.Snackbar;
import com.josef.josefmobile.R;
import com.josef.mobile.util.AppPreferences;
import com.josef.mobile.util.InterstitialAdsRequest;
import com.josef.mobile.idlingres.EspressoIdlingResource;

import java.util.ArrayList;

import static com.josef.mobile.util.Config.JOSEPHOPENINGSTATEMENT;
import static com.josef.mobile.util.Config.VIEWPAGER_AMOUNT;
import static com.josef.mobile.util.Config.WORKREQUEST_LIST;

public class ContentActivity extends AppCompatActivity {

    private BottomAppBar bar;
    private int amount;
    private ArrayList<String> downloadId;
    private int mScrollY;
    public static final String SCROLLVIEWYPOSITION = "com.josef.mobile.free.ui.ContentActivity.scroll_view_y_position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        CoordinatorLayout mainLayout = findViewById(R.id.main_content);
        mainLayout.setVisibility(LinearLayout.GONE);
        if (savedInstanceState == null) {
            downloadId = getIntent().getStringArrayListExtra(WORKREQUEST_LIST);
            amount = getIntent().getIntExtra(VIEWPAGER_AMOUNT, 0);

            FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
            for (int index = 0; index <= amount - 1; index++) {

               // ContentContainerFragment contentContainerFragment = ContentContainerFragment.newInstance(downloadId.get(index));
                        fm.add(R.id.container, ContentContainerFragment.newInstance(downloadId.get(index)));

            }
            fm.commit();
        }
        if (savedInstanceState != null)
            mScrollY = savedInstanceState.getInt(SCROLLVIEWYPOSITION, 0);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        bar = (BottomAppBar) findViewById(R.id.bottom_app_bar);


        // setSupportActionBar(bar);

        bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
        bar.replaceMenu(R.menu.toolbarmenu);
        bar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_settings:

                        break;
                    case R.id.app_bar_archieve:

                }
                return true;
            }
        });

        setupNestedScrollView();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SCROLLVIEWYPOSITION, mScrollY);
    }

    private void setupNestedScrollView() {
        final NestedScrollView scrollView = findViewById(R.id.nested_scrollview);
        scrollView.smoothScrollTo(0, mScrollY);
        scrollView.getViewTreeObserver()
                .addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        if (scrollView.getScrollY() == 0) {
                            bar.performShow();
                        } else if (scrollView.getChildAt(0).getBottom()
                                <= (scrollView.getHeight() + scrollView.getScrollY())) {
                            bar.performHide();
                        } else {
                        }
                        mScrollY = scrollView.getScrollY();
                    }
                });
        scrollView.setFillViewport(true);
    }

    // private InterstitialAd mInterstitialAd;

    private AlertDialog mDialog;

    private void setupProgressBar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.progressdialog);
        mDialog = builder.create();
        mDialog.show();
    }

    public void loadIntersitialAds(InterstitialAdsRequest request) {
        request.execute();
    }

    private void startActivity(Context conext, Class clazz) {
        Intent intent = new Intent(conext, clazz);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbarmenu, menu);
        return true;
    }

    private static final String TAG = "ContentActivity";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
        } else if (item.getItemId() == R.id.action_settings) {
            //    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            //   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //   startActivity(intent);
            return true;

        } else if (item.getItemId() == R.id.app_bar_archieve) {
            // setupProgressBar();
            //loadIntersitialAds(mArchiveActivity);
        }
        return super.onOptionsItemSelected(item);
    }

    public void performFloatingAction(View view) {

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        final Snackbar snackbar = Snackbar.make(coordinatorLayout, "share items..?! ", Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ArrayList<String> metadata = new ArrayList<>(AppPreferences.getName(getApplicationContext()));
                        String data = metadata.toString();
                        String mimeType = "text/plain";
                        Intent shareIntent = ShareCompat.IntentBuilder.from(ContentActivity.this)
                                .setType(mimeType)
                                .setText(data)
                                .getIntent();

                        if (shareIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(shareIntent);
                        }

                    }
                }).setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
        snackbar.setAnchorView(R.id.fab);
        snackbar.show();
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

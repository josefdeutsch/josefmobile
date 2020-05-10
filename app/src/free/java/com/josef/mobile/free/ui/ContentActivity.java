package com.josef.mobile.free.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.test.espresso.IdlingResource;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.snackbar.Snackbar;
import com.josef.josefmobile.R;
import com.josef.mobile.AppPreferences;
import com.josef.mobile.InterstitialAdsRequest;
import com.josef.mobile.data.Favourite;
import com.josef.mobile.data.FavouriteViewModel;
import com.josef.mobile.free.ArchiveActivity;
import com.josef.mobile.free.PresenterActivity;
import com.josef.mobile.idlingres.EspressoIdlingResource;

import java.util.ArrayList;
import java.util.List;

import static com.josef.mobile.Config.JOSEPHOPENINGSTATEMENT;
import static com.josef.mobile.Config.SCROLLVIEWYPOSITION;
import static com.josef.mobile.Config.VIEWPAGERDETAILKEY;
import static com.josef.mobile.Config.VIEWPAGER_AMOUNT;

public class ContentActivity extends AppCompatActivity {

    private BottomAppBar bar;
    private int amount;
    private int scrollY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        if (savedInstanceState == null) {
            amount = getIntent().getIntExtra(VIEWPAGER_AMOUNT, 0);

            FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
            for (int index = 1; index <= amount; index++) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, ContentContainerFragment.newInstance(index))
                        .commit();
            }
            fm.commit();
        }
        if (savedInstanceState != null)
            scrollY = savedInstanceState.getInt(SCROLLVIEWYPOSITION, 0);
        bar = (BottomAppBar) findViewById(R.id.bottom_app_bar);
        setSupportActionBar(bar);
        bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
        setupNestedScrollView();

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SCROLLVIEWYPOSITION, scrollY);
    }

    private void setupNestedScrollView() {
        final NestedScrollView scrollView = findViewById(R.id.nested_scrollview);
        scrollView.smoothScrollTo(0,scrollY);
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
                        scrollY= scrollView.getScrollY();
                    }
                });
        scrollView.setFillViewport(true);
    }

    private InterstitialAd mInterstitialAd;

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
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
        } else if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.app_bar_info) {
            setupProgressBar();
            loadIntersitialAds(new InterstitialAdsRequest() {
                @Override
                public void execute() {
                    mInterstitialAd = new InterstitialAd(getApplicationContext());
                    mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            if (mDialog != null) {
                                mDialog.hide();
                            }
                            mInterstitialAd.show();
                        }

                        @Override
                        public void onAdFailedToLoad(int errorCode) {
                            if (mDialog != null) {
                                mDialog.hide();
                            }
                            startActivity(getApplicationContext(), PresenterActivity.class);
                        }

                        @Override
                        public void onAdClosed() {
                            startActivity(getApplicationContext(), PresenterActivity.class);
                        }
                    });
                }
            });
        } else if (item.getItemId() == R.id.app_bar_archieve) {
            setupProgressBar();
            loadIntersitialAds(new InterstitialAdsRequest() {
                @Override
                public void execute() {
                    mInterstitialAd = new InterstitialAd(getApplicationContext());
                    mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            if (mDialog != null) {
                                mDialog.hide();
                            }
                            mInterstitialAd.show();
                        }

                        @Override
                        public void onAdFailedToLoad(int errorCode) {
                            if (mDialog != null) {
                                mDialog.hide();
                            }
                            startActivity(getApplicationContext(), ArchiveActivity.class);
                        }

                        @Override
                        public void onAdClosed() {
                            startActivity(getApplicationContext(), ArchiveActivity.class);
                        }
                    });
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    public void performFloatingAction(View view) {

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        final Snackbar snackbar = Snackbar.make(coordinatorLayout, "data cached.. ", Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ArrayList<String> metadata = new ArrayList<>(AppPreferences.getName(getApplicationContext()));
                        String data = metadata.toString();
                        String mimeType = "text/plain";

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

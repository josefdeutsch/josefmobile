package com.josef.mobile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentTransaction;
import androidx.test.espresso.IdlingResource;
import androidx.viewpager.widget.ViewPager;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.snackbar.Snackbar;
import com.josef.josefmobile.R;
import com.josef.mobile.free.ArchiveActivity;
import com.josef.mobile.free.PresenterActivity;
import com.josef.mobile.idlingres.EspressoIdlingResource;
import java.util.ArrayList;
import static com.josef.mobile.Config.JOSEPHOPENINGSTATEMENT;
import static com.josef.mobile.Config.ONACTIVITYRESULTEXAMPLE;
import static com.josef.mobile.Config.VIEWPAGER_AMOUNT;

public class HomeActivity extends AppCompatActivity {

    BottomAppBar bar;
    public int amount;
    private static final String TAG = "HomeActivity";
    private LinearLayout mLayout;
    private LayoutInflater mLayoutInflater;
    ViewPager mViewPager;
    LinearLayout mLinearLayout;
    private static final int CONTENT_VIEW_ID = 10101010;
    //https://guides.codepath.com/android/ViewPager-with-FragmentPagerAdapter
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mLinearLayout = findViewById(R.id.container);

        if (savedInstanceState == null) {
            amount = getIntent().getIntExtra(VIEWPAGER_AMOUNT, 0);

            FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
            for (int index = 1; index <= amount; index++) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, HomeContainer.newInstance(index))
                        .commit();
            }
            fm.commit();
            // fragContainer.addView(ll);
        }
        bar = (BottomAppBar) findViewById(R.id.bottom_app_bar);
        setSupportActionBar(bar);
        bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
        setupNestedScrollView();

        if (AppPreferences.getName(this)==null) {
            ArrayList<String> meta = new ArrayList<>();
            meta.add(JOSEPHOPENINGSTATEMENT);
            AppPreferences.setName(this,meta);
        }

        /** //https://proandroiddev.com/look-deep-into-viewpager2-13eb8e06e419
         final float pageMargin = this.getResources().getDimensionPixelOffset(R.dimen.pageMargin);
         final float pageOffset = this.getResources().getDimensionPixelOffset(R.dimen.offset);**/
    }

    private void setupNestedScrollView() {
        final NestedScrollView scrollView = findViewById(R.id.nested_scrollview);
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
                            //scroll view is not at bottom
                        }
                    }
                });
        scrollView.setFillViewport(true);
    }
    private InterstitialAd mInterstitialAd;
    private AlertDialog mDialog;

    private void setupProgressBar(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.progressdialog);
        mDialog = builder.create();
        mDialog.show();
    }

    public void loadIntersitialAds(InterstitialAdsRequest request){
        request.execute();
    }

    private void startActivity(Context conext, Class clazz){
        Intent intent = new Intent(conext, clazz);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
      //  getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) { }
        else if (item.getItemId() == R.id.action_settings){
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        else if (item.getItemId() == R.id.app_bar_info) {
            EspressoIdlingResource.increment();
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
                            EspressoIdlingResource.decrement();
                            startActivity(getApplicationContext(),PresenterActivity.class);
                        }
                        @Override
                        public void onAdClosed() {
                            EspressoIdlingResource.decrement();
                            startActivity(getApplicationContext(),PresenterActivity.class);
                        }
                    });
                }
            });
        }
        else if (item.getItemId() == R.id.app_bar_archieve) {
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
                            EspressoIdlingResource.decrement();
                            startActivity(getApplicationContext(),ArchiveActivity.class);
                        }
                        @Override
                        public void onAdClosed() {
                            EspressoIdlingResource.decrement();
                            startActivity(getApplicationContext(),ArchiveActivity.class);
                        }
                    });
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }



    public void performFloatingAction(View view) {

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.bottom_app_bar_coord);
        final Snackbar snackbar = Snackbar.make(coordinatorLayout, "data cached.. ", Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ArrayList<String> metadata = new ArrayList<>(AppPreferences.getName(getApplicationContext()));
                        String data = metadata.toString();

                        String mimeType = "text/plain";
                        Intent shareIntent = ShareCompat.IntentBuilder.from(HomeActivity.this)
                                .setType(mimeType)
                                .setText(data)
                                .getIntent();

                        if (shareIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(shareIntent);
                        }
                    }
                }).setActionTextColor(getResources().getColor(android.R.color.holo_red_light));

        View view1 = snackbar.getView();
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view1.getLayoutParams();
        params.gravity = Gravity.TOP;
        view1.setLayoutParams(params);
        view1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
        TextView snackBarText = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        snackBarText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
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

package com.josef.mobile.free.ui;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.snackbar.Snackbar;
import com.josef.josefmobile.R;
import com.josef.mobile.util.InterstitialAdsRequest;

public class PresenterActivity extends AppCompatActivity {

    private static final String TAG = "PresenterActivity";
    private BottomAppBar bar;
    private InterstitialAd mInterstitialAd;
    private AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presenter);
        bar = (BottomAppBar) findViewById(R.id.bottom_app_bar);
        setSupportActionBar(bar);
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
        //restart activity?

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

        if (savedInstanceState == null) {
            FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.presenter_container, PresenterFragment.newInstance("",""))
                    .commit();
                fm.commit();
        }

    }
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

    public void performFloatingAction(View view) {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.bottom_app_bar_coord);
        final Snackbar snackbar = Snackbar.make(coordinatorLayout, "contact artist.. ?", Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String mimeType = "text/plain";
                        Intent shareIntent =   ShareCompat.IntentBuilder.from(PresenterActivity.this)
                                .setType(mimeType)
                                .setText("joseph3d@gmail.com")
                                .getIntent();
                        if (shareIntent.resolveActivity(getPackageManager()) != null){
                            startActivity(shareIntent);
                        }
                    }
                }).setActionTextColor(getResources().getColor(android.R.color.holo_red_light));

        View view1 = snackbar.getView();
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view1.getLayoutParams();
        params.gravity = Gravity.TOP;
        view1.setLayoutParams(params);
        view1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
        TextView snackBarText =  snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        snackBarText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
        snackbar.show();

    }
    private void startActivity(Context conext, Class clazz) {
        Intent intent = new Intent(conext, clazz);
        startActivity(intent);
    }


    private InterstitialAdsRequest mArchiveActivity = new InterstitialAdsRequest() {
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
    };

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

        } else if (item.getItemId() == R.id.app_bar_archieve) {
            setupProgressBar();
            loadIntersitialAds(mArchiveActivity);
        }
        return super.onOptionsItemSelected(item);
    }

}

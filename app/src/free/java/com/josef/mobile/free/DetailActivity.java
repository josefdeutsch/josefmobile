package com.josef.mobile.free;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.test.espresso.IdlingResource;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.snackbar.Snackbar;
import com.josef.josefmobile.R;
import com.josef.mobile.InterstitialAdsRequest;
import com.josef.mobile.idlingres.EspressoIdlingResource;

import static com.josef.mobile.Config.VIEWPAGERDETAILKEY;
import static com.josef.mobile.Config.VIEWPAGERMAINKEY;

public class DetailActivity extends AppCompatActivity  {
    private static final String TAG = "DetailActivity";
    ViewPagerFragmentAdapters mAdapter;
    ViewPager mViewPager;
    DetailFragment mDetailFragment;
    private int which;
    private int previousAdapterPosition;
    private Integer currentAdapterPosition;
    BottomAppBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        bar = (BottomAppBar) findViewById(R.id.bottom_app_bar);
        setSupportActionBar(bar);
        bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
        //restart activity?

        if (savedInstanceState == null) {
            which = getIntent().getIntExtra(VIEWPAGERMAINKEY,0);
            previousAdapterPosition = getIntent().getIntExtra(VIEWPAGERDETAILKEY,0);
            // fragContainer.addView(ll);
        }

        Log.d(TAG, "onCreate: "+which);
        Log.d(TAG, "onCreate: "+previousAdapterPosition);
        setupScrollView();

        mViewPager = findViewById(R.id.detailviewpager);
        //
        mAdapter = new ViewPagerFragmentAdapters(getSupportFragmentManager(),1);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentAdapterPosition = position;
                mDetailFragment = (DetailFragment)mAdapter.getRegisteredFragment(position);
                Log.d(TAG, "onPageSelected: ");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //mViewPager.setCurrentItem(5);
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


    private void setupScrollView() {
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
        scrollView.setFillViewport (true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

        } else if (item.getItemId() == R.id.app_bar_info) {
            EspressoIdlingResource.increment();
          /**  setupProgressBar();
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
            });**/
            startActivity(getApplicationContext(),PresenterActivity.class);
        } else if (item.getItemId() == R.id.app_bar_archieve) {
           /** setupProgressBar();
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
            });**/
            startActivity(getApplicationContext(),ArchiveActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }

    private void withCommerials(){

    }


    /**@Override
    public void onFragmentInteraction(Uri uri) {

    }**/

    /**public static class ViewPagerFragmentAdapter extends FragmentStatePagerAdapter {

        public int mWhich;
        public ViewPagerFragmentAdapter(FragmentManager fm,int which) {
            super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            mWhich=which;
        }

        @Override
        public int getCount() {
            return 50;
        }

        @Override
        public Fragment getItem(int position) {
            return DetailFragment.newInstance(mWhich,position);
        }
    }**/

    public class ViewPagerFragmentAdapters extends FragmentStatePagerAdapter {

        //smartfragmentstatepager....
        public int mIndex;
        SparseArray<Fragment> registeredFragments = new SparseArray<>();

        public ViewPagerFragmentAdapters(FragmentManager fm, int index) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            mIndex = index;

        }
        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public int getCount() {
            return 50;
        }

        @Override
        public Fragment getItem(int position) {
         //   if(registeredFragments.get(position)!=null)return registeredFragments.get(position);
            return DetailFragment.newInstance(mIndex,position);
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }
    }

    public void performFloatingAction(View view) {
        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.bottom_app_bar_coord);
        final Snackbar snackbar = Snackbar.make(coordinatorLayout, "add items..?", Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(currentAdapterPosition==null) {
                            mViewPager.setCurrentItem(1);
                            mViewPager.setCurrentItem(0);
                            mDetailFragment.addItemtsToDataBase(currentAdapterPosition);
                        }else {
                            mDetailFragment.addItemtsToDataBase(currentAdapterPosition);
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


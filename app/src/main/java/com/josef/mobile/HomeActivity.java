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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.test.espresso.IdlingResource;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.snackbar.Snackbar;
import com.josef.josefmobile.R;
import com.josef.mobile.free.ArchiveActivity;
import com.josef.mobile.free.DetailActivity;
import com.josef.mobile.free.PresenterActivity;
import com.josef.mobile.idlingres.EspressoIdlingResource;


import java.util.ArrayList;


import static com.josef.mobile.Config.APPPREFERENCE_DEFAULTVALUE;
import static com.josef.mobile.Config.ONACTIVITYRESULTEXAMPLE;
import static com.josef.mobile.Config.VIEWPAGERDETAILKEY;
import static com.josef.mobile.Config.VIEWPAGERMAINKEY;
import static com.josef.mobile.Config.VIEWPAGER_AMOUNT;

public class HomeActivity extends AppCompatActivity {

    private AlertDialog mDialog;
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

        if (AppPreferences.getName(getApplicationContext())==null) {
           ArrayList<String> opener = new ArrayList<>();
           opener.add("Hello this is joseph ...");
           AppPreferences.setName(getApplicationContext(),opener);
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
            Intent intent = new Intent(this, PresenterActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.app_bar_archieve) {
            Intent intent = new Intent(this, ArchiveActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    int key;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ONACTIVITYRESULTEXAMPLE) {
            if (resultCode == Activity.RESULT_OK) {
              //   key = data.getIntExtra(VIEWPAGERMAINKEY, 0);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
    }
    @Override
    public void onRestart(){
        super.onRestart();
        //AppPreferences.clearNameList(this);
        //ArrayList<String> meta = new ArrayList<>();
        //meta.add(String.valueOf(mainkey));
        //meta.add(String.valueOf(detailvalue));
        //AppPreferences.setName(this,meta);
    }

    public void performFloatingAction(View view) {

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.bottom_app_bar_coord);
        final Snackbar snackbar = Snackbar.make(coordinatorLayout, "share items.. ?", Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String mimeType = "text/plain";
                        Intent shareIntent = ShareCompat.IntentBuilder.from(HomeActivity.this)
                                .setType(mimeType)
                                .setText("share your selection..")
                                .getIntent();
                        if (shareIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(shareIntent);
                        }
                        ArrayList<String> mShareValues = new ArrayList<String>(AppPreferences.getName(HomeActivity.this));
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

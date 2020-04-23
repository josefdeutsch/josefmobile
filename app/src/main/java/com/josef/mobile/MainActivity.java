package com.josef.mobile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.snackbar.Snackbar;
import com.josef.josefmobile.R;
import com.josef.mobile.free.ArchiveActivity;
import com.josef.mobile.free.PresenterActivity;
import com.josef.mobile.idlingres.EspressoIdlingResource;
import java.util.ArrayList;

import static com.josef.mobile.Config.ONVIEWPAGERINITLISTENER;
import static com.josef.mobile.Config.VIEWPAGER_AMOUNT;

public class MainActivity extends AppCompatActivity {
    private AlertDialog mDialog;
    BottomAppBar bar;
    public int index;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            index = getIntent().getIntExtra(VIEWPAGER_AMOUNT,0);
            MainFragment fragment = MainFragment.newInstance(index);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_main, fragment)
                    .commit();
        }

        bar = (BottomAppBar) findViewById(R.id.bottom_app_bar);
        setSupportActionBar(bar);
        bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
        
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

        if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
            EspressoIdlingResource.decrement();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.progressdialog);
        mDialog = builder.create();
        mDialog.show();
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
             mDialog.hide();
            }
        }, 5000);

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


    public void performFloatingAction(View view) {

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.bottom_app_bar_coord);
        final Snackbar snackbar = Snackbar.make(coordinatorLayout, "share items.. ?", Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String mimeType = "text/plain";

                        Intent shareIntent = ShareCompat.IntentBuilder.from(MainActivity.this)
                                .setType(mimeType)
                                .setText("share your selection..")
                                .getIntent();
                        if (shareIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(shareIntent);
                        }
                        ArrayList<String> mShareValues = new ArrayList<String>(AppPreferences.getName(MainActivity.this));
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

}

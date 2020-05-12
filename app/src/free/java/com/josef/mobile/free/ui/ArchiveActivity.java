package com.josef.mobile.free.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.snackbar.Snackbar;
import com.josef.josefmobile.R;

public class ArchiveActivity extends AppCompatActivity {

    private BottomAppBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);
        bar = (BottomAppBar) findViewById(R.id.bottom_app_bar);
        setSupportActionBar(bar);
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
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
                        }
                    }
                });

        FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.archieve_container, ArchiveFragment.newInstance("",""))
                .commit();
        fm.commit();
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

        } else if (item.getItemId() == R.id.app_bar_archieve) {

        }
        return super.onOptionsItemSelected(item);
    }
    public void performFloatingAction(View view) {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        final Snackbar snackbar = Snackbar.make(coordinatorLayout, "add more items.. ?", Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       finish();
                    }
                }).setActionTextColor(getResources().getColor(android.R.color.holo_red_light));

        snackbar.setAnchorView(R.id.fab);
        snackbar.show();
    }




}

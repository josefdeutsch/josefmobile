package com.josef.mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.josef.josefmobile.R;

public class ErrorActivity extends AppCompatActivity {
    public static final String TAG = "ErrorActivity";
    BottomAppBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        bar = (BottomAppBar) findViewById(R.id.bottom_app_bar);
        setSupportActionBar(bar);
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the navigation click by showing a BottomDrawer etc.
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
                            //scroll view is not at bottom
                        }
                    }
                });
    }
}

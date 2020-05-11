package com.josef.mobile.ui;

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
        bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);


    }
}

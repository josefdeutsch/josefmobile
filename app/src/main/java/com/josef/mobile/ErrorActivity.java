package com.josef.mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.josef.josefmobile.R;

public class ErrorActivity extends AppCompatActivity {
    public static final String TAG = "ErrorActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
    }
}

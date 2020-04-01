package com.josef.mobile.free;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.josef.josefmobile.R;

import static com.josef.mobile.Config.DETAIL_ACTIVITY;

public class DetailActivity extends AppCompatActivity {

    private TextView buttonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        buttonView = findViewById(R.id.buttonview);
        buttonView.setText(DETAIL_ACTIVITY);
    }
}

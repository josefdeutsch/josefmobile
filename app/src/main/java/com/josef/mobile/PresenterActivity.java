package com.josef.mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.josef.josefmobile.R;
import com.josef.mobile.free.DetailActivity;

import static com.josef.mobile.Config.PRESENTER_ACTVITY;

public class PresenterActivity extends AppCompatActivity {

    private TextView buttonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);
        buttonView = findViewById(R.id.buttonview);
        buttonView.setText(PRESENTER_ACTVITY);
    }

    public void performDetailActivity(View view) {
        Intent intent = new Intent(this, DetailActivity.class);
        startActivity(intent);
    }
}

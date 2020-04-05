package com.josef.mobile.free;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.josef.josefmobile.R;
import com.josef.mobile.components.MainActivityAdapter;
import com.josef.mobile.free.components.ShareActivityAdapter;

import java.util.ArrayList;

public class ShareActivity extends AppCompatActivity {

    private static final String TAG = "PresenterActivity";
    BottomAppBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        bar = (BottomAppBar) findViewById(R.id.bottom_app_bar);
        setSupportActionBar(bar);
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the navigation click by showing a BottomDrawer etc.
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
        setupRecyclerView();
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

        } else if (item.getItemId() == R.id.app_bar_share) {

        } else if (item.getItemId() == R.id.app_bar_archieve) {

        }
        return super.onOptionsItemSelected(item);
    }
    private void setupRecyclerView() {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        final ShareActivityAdapter simpleAdapter = new ShareActivityAdapter(getApplicationContext(), getLists(new ArrayList<String>()));
        mRecyclerView.setAdapter(simpleAdapter);
    }
    private ArrayList<String> getLists(ArrayList<String> arrayList) {

        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/0001.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/0002.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/0003.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00010621.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00020621.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00030621.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00010622.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00020622.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00030622.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00030622.png");

        return arrayList;
    }


}

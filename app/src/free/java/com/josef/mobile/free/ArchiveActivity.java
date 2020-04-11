package com.josef.mobile.free;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.snackbar.Snackbar;
import com.josef.josefmobile.R;
import com.josef.mobile.Echo;
import com.josef.mobile.MainActivity;
import com.josef.mobile.Message;
import com.josef.mobile.free.components.DeleteCallBack;
import com.josef.mobile.free.components.ArchiveActivityAdapter;

import java.util.ArrayList;

public class ArchiveActivity extends AppCompatActivity {

    private static final String TAG = "PresenterActivity";
    BottomAppBar bar;
    ArrayList mArraylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);
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
            Intent intent = new Intent(this, PresenterActivity.class);
            startActivity(intent);

        } else if (item.getItemId() == R.id.app_bar_archieve) {
            Intent intent = new Intent(this, ArchiveActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView() {
        mArraylist = getLists(new ArrayList<String>());
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        final ArchiveActivityAdapter simpleAdapter = new ArchiveActivityAdapter(getApplicationContext(), mArraylist);
        mRecyclerView.setAdapter(simpleAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new DeleteCallBack(simpleAdapter,
                new DeleteCallBack.SnackBarListener() {
                    @Override
                    public void listenToAction(final int position) {
                        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.bottom_app_bar_coord);
                        final Snackbar snackbar = Snackbar.make(coordinatorLayout, "DELETE ITEM..?", Snackbar.LENGTH_LONG)
                                .setAction("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        simpleAdapter.deleteTask(position);
                                    }
                                }).setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
                        View view = snackbar.getView();
                        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
                        params.gravity = Gravity.TOP;
                        view.setLayoutParams(params);
                        view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                        TextView snackBarText =  snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
                        snackBarText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                        snackbar.show();
                    }
                }));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
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
    public void performFloatingAction(View view) {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.bottom_app_bar_coord);
        final Snackbar snackbar = Snackbar.make(coordinatorLayout, "add more items.. ?", Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ArchiveActivity.this, MainActivity.class);
                        startActivity(intent);
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

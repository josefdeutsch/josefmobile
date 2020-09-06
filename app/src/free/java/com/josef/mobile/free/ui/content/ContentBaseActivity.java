package com.josef.mobile.free.ui.content;

import android.content.DialogInterface;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.test.espresso.IdlingResource;

import com.google.android.gms.common.SignInButton;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.josef.josefmobile.R;
import com.josef.mobile.data.Favourite;
import com.josef.mobile.data.FavouriteViewModel;
import com.josef.mobile.free.ui.container.ContentContainerFragment;
import com.josef.mobile.idlingres.EspressoIdlingResource;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ContentBaseActivity extends AppCompatActivity {

    private static final String FRAGMENT_MODAL = "modal";
    protected static final String SCROLLVIEWYPOSITION = "com.josef.mobile.free.ui.ContentActivity.scroll_view_y_position";


    protected FavouriteViewModel mFavouriteViewmodel;
    protected ViewModelContent mViewModelContent;
    protected String mUserId;
    protected CoordinatorLayout mContentLayout;
    protected ConstraintLayout mSignInLayout;
    protected int mAmount;
    protected int mScrollY;
    protected ArrayList<String> mDownloadId;


    protected Toolbar setupToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        return toolbar;
    }

    protected BottomAppBar setupBottomBar(){
        BottomAppBar bar = (BottomAppBar) findViewById(R.id.bottom_app_bar);
        bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
        bar.replaceMenu(R.menu.menu);

        bar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_settings:
                        break;
                    case R.id.app_bar_archieve:
                        new ModalFragment().show(getSupportFragmentManager(), FRAGMENT_MODAL);
                        break;
                }
                return true;
            }
        });
        return bar;
    }

    protected NestedScrollView setupNestedScrollView(final BottomAppBar bar) {
        final NestedScrollView scrollView = findViewById(R.id.nested_scrollview);
        scrollView.smoothScrollTo(0, mScrollY);
        scrollView.getViewTreeObserver()
                .addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        if (scrollView.getScrollY() == 0) bar.performShow();
                        else if (scrollView.getChildAt(0).getBottom()
                                <= (scrollView.getHeight() + scrollView.getScrollY())) bar.performHide();
                        mScrollY = scrollView.getScrollY();
                    }
                });
        scrollView.setFillViewport(true);
        return scrollView;
    }

    protected void setupFloatingActionButton(){
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               setupAlertDialog();
            }
        });

    }
    private void setupAlertDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(R.string.sync);
        alert.setCancelable(false);
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                pushDataToFirebase();
            }
        });
        alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alert.show();
    }

    protected SignInButton setupSignInButton() {
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }
        });
        return signInButton;
    }
    protected void addFragmentToLayout(int i, int p) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(p, ContentContainerFragment.newInstance(mDownloadId.get(i)))
                .commit();
    }

    protected void signIn() {
    /*...*/
    }

    private void pushDataToFirebase() {
            mFavouriteViewmodel.getAllNotes().observe(this, new Observer<List<Favourite>>() {
                @Override
                public void onChanged(@Nullable List<Favourite> favourites) {
                    try {
                        mViewModelContent.addData(favourites,mUserId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
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

package com.josef.mobile.free;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.test.espresso.IdlingResource;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.josef.josefmobile.R;
import com.josef.mobile.data.Favourite;
import com.josef.mobile.data.FavouriteViewModel;
import com.josef.mobile.free.ui.ContentContainerFragment;
import com.josef.mobile.free.ui.ModalFragment;
import com.josef.mobile.idlingres.EspressoIdlingResource;
import com.josef.mobile.ui.SplashActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.josef.mobile.util.Config.VIEWPAGER_AMOUNT;
import static com.josef.mobile.util.Config.WORKREQUEST_LIST;

public class ContentActivity extends LoginActivity implements View.OnClickListener {

    private CoordinatorLayout mContentLayout;
    private ConstraintLayout mSignInLayout;
    private FavouriteViewModel favouriteViewModel;

    public String userId;
    private static final String FRAGMENT_MODAL = "modal";
    private int amount;
    private ArrayList<String> downloadId;
    private int mScrollY;
    public static final String SCROLLVIEWYPOSITION = "com.josef.mobile.free.ui.ContentActivity.scroll_view_y_position";
    public static final String BOOLEANVALUE = "com.josef.mobile.free.ui.ContentActivity.booleanValue";
    private boolean mBoolean = true;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        mContentLayout = findViewById(R.id.main_content);
        mContentLayout.setVisibility(LinearLayout.GONE);
        mSignInLayout = findViewById(R.id.signIn_layout);

        favouriteViewModel = ViewModelProviders.of(ContentActivity.this).get(FavouriteViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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

        setupNestedScrollView(bar);

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(this);


        GoogleSignInOptions gso = setupGoogleSignInOptions();
        buildGoogleApiClient(gso);
        setupFirebaseAuth();


        if (savedInstanceState == null) {
            downloadId = getIntent().getStringArrayListExtra(WORKREQUEST_LIST);
            amount = getIntent().getIntExtra(VIEWPAGER_AMOUNT, 0);

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.ad_fragment, ContentContainerFragment.newInstance(downloadId.get(0)))
                    .commit();

           for (int i = 1; i <= amount - 1; i++) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, ContentContainerFragment.newInstance(downloadId.get(i)))
                        .commit();


            }
        }

    }


    private static final String TAG = "ContentActivity";

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SCROLLVIEWYPOSITION, mScrollY);
        outState.putBoolean(BOOLEANVALUE, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAuthListener != null) {
            mAuth.addAuthStateListener(mAuthListener);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                updateUI(null);
                Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    @Override
    public void updateUI(FirebaseUser user) {
        if (user != null) {
            userId = user.getUid();
            mSignInLayout.setVisibility(LinearLayout.GONE);
            mContentLayout.setVisibility(LinearLayout.VISIBLE);
        }
    }

    private void setupNestedScrollView(final BottomAppBar bar) {
        final NestedScrollView scrollView = findViewById(R.id.nested_scrollview);
        scrollView.smoothScrollTo(0, mScrollY);
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
                        mScrollY = scrollView.getScrollY();
                    }
                });
        scrollView.setFillViewport(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbarmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
        } else if (item.getItemId() == R.id.action_settings) {
            signOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void performFloatingAction(View view) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(R.string.sync);
        alert.setCancelable(false);
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                push();
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

    private void push() {
        if (favouriteViewModel != null) {
            favouriteViewModel.getAllNotes().observe(ContentActivity.this, new Observer<List<Favourite>>() {
                @Override
                public void onChanged(@Nullable List<Favourite> favourites) {

                    FirebaseDatabase database = FirebaseDatabase.getInstance();//
                    final DatabaseReference myRef = database.getReference("users");
                    int len = favourites.size();
                    Data string = null;
                    try {
                        JSONObject jsonObject = new JSONObject();
                        JSONArray googlevideos = new JSONArray();
                        JSONObject data = new JSONObject();
                        JSONArray sources = new JSONArray();
                        for (int i = 0; i <= len - 1; i++) {
                            JSONObject sum = new JSONObject();
                            sum.put("description", "LoremIpsum...");
                            JSONArray path = new JSONArray();
                            path.put(favourites.get(i).getTitle());
                            sum.put("sources", path);
                            sum.put("card", favourites.get(i).getDescription());
                            sum.put("background", favourites.get(i).getDescription());
                            sum.put("title", "material");
                            sum.put("studio", "Google+");
                            sources.put(sum);
                        }
                        data.put("category", "Google+");
                        data.put("videos", sources);
                        googlevideos.put(data);
                        jsonObject.put("googlevideos", googlevideos);
                        string = new Data(jsonObject.toString(), "", "");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    myRef.child(userId).setValue(string);
                }
            });
        }
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

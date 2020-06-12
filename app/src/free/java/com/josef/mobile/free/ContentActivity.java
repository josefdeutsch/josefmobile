package com.josef.mobile.free;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.josef.josefmobile.R;
import com.josef.mobile.data.Favourite;
import com.josef.mobile.data.FavouriteViewModel;
import com.josef.mobile.free.ui.ArchiveActivity;
import com.josef.mobile.free.ui.ContentContainerFragment;
import com.josef.mobile.free.ui.ModalFragment;
import com.josef.mobile.idlingres.EspressoIdlingResource;
import com.josef.mobile.ui.SplashActivity;
import com.josef.mobile.util.AppPreferences;
import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.josef.mobile.util.Config.JOSEPHOPENINGSTATEMENT;
import static com.josef.mobile.util.Config.VIEWPAGER_AMOUNT;
import static com.josef.mobile.util.Config.WORKREQUEST_LIST;

public class ContentActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {


    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    private CoordinatorLayout mContentLayout;
    private ConstraintLayout mSignInLayout;

    public String userId;
    private static final String FRAGMENT_MODAL = "modal";
    private BottomAppBar bar;
    private int amount;
    private ArrayList<String> downloadId;
    private int mScrollY;
    public static final String SCROLLVIEWYPOSITION = "com.josef.mobile.free.ui.ContentActivity.scroll_view_y_position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        mContentLayout = findViewById(R.id.main_content);
        mContentLayout.setVisibility(LinearLayout.GONE);

        mSignInLayout = findViewById(R.id.signIn_layout);

        if (savedInstanceState == null) {
            downloadId = getIntent().getStringArrayListExtra(WORKREQUEST_LIST);
            amount = getIntent().getIntExtra(VIEWPAGER_AMOUNT, 0);
            FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
            for (int index = 0; index <= amount - 1; index++) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, ContentContainerFragment.newInstance(downloadId.get(index)))
                        .commit();
            }
            fm.commit();
        }

        if (savedInstanceState != null)
            mScrollY = savedInstanceState.getInt(SCROLLVIEWYPOSITION, 0);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        bar = (BottomAppBar) findViewById(R.id.bottom_app_bar);


        // setSupportActionBar(bar);

        bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
        bar.replaceMenu(R.menu.menu);
        bar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_settings:
                        break;
                    case R.id.app_bar_archieve:
                        /**FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
                        for (int index = 0; index <= amount - 1; index++) {
                            getSupportFragmentManager().beginTransaction()
                                    .add(R.id.modalfragment, ModalFragment.newInstance())
                                    .commit();
                        }
                        fm.commit();**/
                        new ModalFragment().show(getSupportFragmentManager(), FRAGMENT_MODAL);
                        break;
                }
                return true;
            }
        });

        setupNestedScrollView();

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(this);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .
                        enableAutoManage(this, this)
                .

                        addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .

                        build();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                updateUI(user);
            }
        }

        ;
        AppPreferences.clearNameList(this);
        ArrayList<String> meta = new ArrayList<>(AppPreferences.getName(this));
        meta.add(JOSEPHOPENINGSTATEMENT + System.lineSeparator());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SCROLLVIEWYPOSITION, mScrollY);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthListener);
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


    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        showProgressDialog(this);

        final AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                } else {

                }
                hideProgressDialog();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(R.string.logout);
        alert.setCancelable(false);
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mAuth.signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                                getApplicationContext().startActivity(intent);
                                finish();
                            }
                        }
                );
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


    private void updateUI(FirebaseUser user) {
        if (user != null) {
            userId=user.getUid();
            mSignInLayout.setVisibility(LinearLayout.GONE);
            mContentLayout.setVisibility(LinearLayout.VISIBLE);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }


    private void setupNestedScrollView() {
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

    private static final String TAG = "ContentActivity";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
        } else if (item.getItemId() == R.id.action_settings) {
            signOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private FavouriteViewModel favouriteViewModel;

    public void performFloatingAction(View view) {

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        final Snackbar snackbar = Snackbar.make(coordinatorLayout, "share items..?! ", Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        favouriteViewModel = ViewModelProviders.of(ContentActivity.this).get(FavouriteViewModel.class);
                        favouriteViewModel.getAllNotes().observe(ContentActivity.this, new Observer<List<Favourite>>() {
                            @Override
                            public void onChanged(@Nullable List<Favourite> favourites) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();//
                                DatabaseReference myRef = database.getReference("users");
                                List<String> meta = new ArrayList<>();
                                int len = favourites.size();
                                for (int i = 0; i <=len-1 ; i++) {
                                meta.add(favourites.get(i).getDescription());
                                }
                                Gson gson = new Gson();
                                String serial = gson.toJson(meta);
                                Data data = new Data("bild3,", "uschi", serial);
                                myRef.child(userId).setValue(data);

                            }
                        });

                    }
                }).setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
        snackbar.setAnchorView(R.id.fab);
        snackbar.show();
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

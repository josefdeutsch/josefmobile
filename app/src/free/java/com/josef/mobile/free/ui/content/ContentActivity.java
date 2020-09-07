package com.josef.mobile.free.ui.content;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.firebase.auth.FirebaseUser;
import com.josef.josefmobile.R;
import com.josef.mobile.data.FavouriteViewModel;

import static com.josef.mobile.util.Config.VIEWPAGER_AMOUNT;
import static com.josef.mobile.util.Config.WORKREQUEST_LIST;

public class ContentActivity extends LoginActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        mContentLayout = findViewById(R.id.main_content);
        mContentLayout.setVisibility(LinearLayout.GONE);
        mSignInLayout = findViewById(R.id.signIn_layout);

        mFavouriteViewmodel = ViewModelProviders.of(ContentActivity.this).get(FavouriteViewModel.class);
        mViewModelContent = ViewModelProviders.of(ContentActivity.this).get(ViewModelContent.class);

        setupToolbar();
        BottomAppBar bottomAppBar = setupBottomBar();
        setupNestedScrollView(bottomAppBar);
        setupSignInButton();
        setupFloatingActionButton();

        GoogleSignInOptions gso = setupGoogleSignInOptions();
        buildGoogleApiClient(gso);
        setupFirebaseAuth();

        if (savedInstanceState == null) {
            mDownloadId = getIntent().getStringArrayListExtra(WORKREQUEST_LIST);
            mAmount = getIntent().getIntExtra(VIEWPAGER_AMOUNT, 0);
            addFragmentToLayout(0, R.id.ad_fragment);
            for (int index = 1; index <= 2 - 1; index++) {
                addFragmentToLayout(index, R.id.container);
            }
        }
    }

    private static final String TAG = "ContentActivity";

    public void replaceLayout(int query) {
//        replaceFragmentToLayout(0, R.id.ad_fragment, query);
        for (int index = 1; index <= 2 - 1; index++) {
            replaceFragmentToLayout(index, R.id.container, query);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SCROLLVIEWYPOSITION, mScrollY);
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
    public void updateUI(FirebaseUser user) {
        if (user == null) return;
        mUserId = user.getUid();
        mSignInLayout.setVisibility(LinearLayout.GONE);
        mContentLayout.setVisibility(LinearLayout.VISIBLE);
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

}

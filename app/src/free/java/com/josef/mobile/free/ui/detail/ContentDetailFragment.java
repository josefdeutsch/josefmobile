package com.josef.mobile.free.ui.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProviders;

import com.josef.josefmobile.R;
import com.josef.mobile.data.FavouriteViewModel;

import org.json.JSONException;

import static com.josef.mobile.free.ui.detail.ViewModelDetail.STATE_BOOLEAN_VALUE;
import static com.josef.mobile.free.ui.detail.ViewModelDetail.STATE_RESUME_POSITION;
import static com.josef.mobile.free.ui.detail.ViewModelDetail.STATE_RESUME_WINDOW;
import static com.josef.mobile.util.Config.VIEWPAGERDETAILKEY;
import static com.josef.mobile.util.Config.WORKREQUEST_DOWNLOADID;

public class ContentDetailFragment extends ContentComponentFragment {

    public static final int DIALOG_FRAGMENT = 1;
    private static final String TAG = "ContentDetailFragment";

    public ContentDetailFragment() {
    }

    public static ContentDetailFragment newInstance(String which, int index) {
        ContentDetailFragment fragment = new ContentDetailFragment();
        Bundle args = new Bundle();
        args.putString(WORKREQUEST_DOWNLOADID, which);
        args.putInt(VIEWPAGERDETAILKEY, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDownloadId = getArguments().getString(WORKREQUEST_DOWNLOADID);
            index = getArguments().getInt(VIEWPAGERDETAILKEY);
        }
        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_BOOLEAN_VALUE);
        }
        mViewModelDetail = ViewModelProviders.of(this).get(ViewModelDetail.class);
        mFavouriteViewModel = ViewModelProviders.of(this).get(FavouriteViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layoutInflater = inflater.inflate(R.layout.fragment_content_detail, container, false);

        setupUi();
        mArticle.setText("Sculpture: " + index);

        supplyView(mArtWork,mArtWorkSupplier);
        supplyView(mArticleByLine,mArticleSupplier);

        mArtWork.setOnClickListener(mArtWorkOnClickListener);
        mColorButton.setOnClickListener(mColorButtonOnClickListener);
        mPlayButton.setOnClickListener(mPlayButtonOnClickListener);
        mButtonDataBase.setOnCheckedChangeListener(mButtonDataBaseOnClickListener);
        mFullScreenButton.setOnClickListener(mFullScreenButtonOnClickListener);
        // fullscreen reload...
        return layoutInflater;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      //  if(data.getExtras()==null) return;

        switch(requestCode) {
            case DIALOG_FRAGMENT:
                if (resultCode == Activity.RESULT_OK) {

                    Bundle bundle = data.getExtras();
                    String index = bundle.getString("recylerindex", null);
                    Log.d(TAG, "onActivityResult: "+index);

                    doWork(new Worker() {
                        @Override
                        public void execute(String input, int index) throws JSONException {
                            setupThumbNailSource(input, index);
                            Log.d(TAG, "execute: "+input);
                            initExoPlayer(getContext());
                            initFullscreenDialog();
                            setupMediaSource(input, index);
                            Log.d(TAG, "execute: "+input);
                        }
                    });

                } else if (resultCode == Activity.RESULT_CANCELED){

                }
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, mResumePosition);
        outState.putBoolean(STATE_BOOLEAN_VALUE, mExoPlayerFullscreen);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseExoPlayer();
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}

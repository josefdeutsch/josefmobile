package com.josef.mobile.free.ui.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProviders;

import com.josef.josefmobile.R;

import static com.josef.mobile.free.ui.detail.ViewModelDetail.STATE_BOOLEAN_VALUE;
import static com.josef.mobile.free.ui.detail.ViewModelDetail.STATE_RESUME_POSITION;
import static com.josef.mobile.free.ui.detail.ViewModelDetail.STATE_RESUME_WINDOW;
import static com.josef.mobile.util.Config.VIEWPAGERDETAILKEY;
import static com.josef.mobile.util.Config.WORKREQUEST_DOWNLOADID;

public class ContentDetailFragment extends ContentComponentFragment {


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
        mFullScreenButton.setOnClickListener(mFullScreenButtonOnClickListener);
        mButtonDataBase.setOnCheckedChangeListener(mButtonDataBaseOnClickListener);

        // fullscreen reload...

        return layoutInflater;
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

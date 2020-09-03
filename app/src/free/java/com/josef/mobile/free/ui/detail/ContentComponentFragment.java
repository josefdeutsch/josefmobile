package com.josef.mobile.free.ui.detail;

import android.os.Handler;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.josef.josefmobile.R;
import com.josef.mobile.data.Favourite;
import com.josef.mobile.data.FavouriteViewModel;

import org.json.JSONException;

public class ContentComponentFragment extends ContentPlayerFragment {


    public ToggleButton mButtonDataBase;
    public TextView mArticle;
    public TextView mArticleByLine;
    public MaterialButton mColorButton;

    protected Supplier mArticleSupplier = new Supplier() {
        @Override
        public void supply() {
            doWork(new Worker() {
                @Override
                public void execute(String input, int index) throws JSONException {
                    String name = mViewModelDetail.getJsonName(input, index);
                    mArticleByLine.setText(name);
                }
            });
        }
    };

    protected Supplier mArtWorkSupplier = new Supplier() {
        @Override
        public void supply() {
            doWork(new Worker() {
                @Override
                public void execute(String input, int index) throws JSONException {
                    setupThumbNailSource(input, index);
                }
            });
        }
    };

    protected View.OnClickListener mColorButtonOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            //...
        }
    };

    protected View.OnClickListener mArtWorkOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mPlayerView.isControllerVisible()) {
                mPlayerView.hideController();
                return;
            }
            mPlayerView.hideController();
        }
    };

    protected View.OnClickListener mPlayButtonOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (lock != null && !mPlayer.getPlayWhenReady()) {
                mPlayer.setPlayWhenReady(true);
                return;
            }

            lock = new Object();

            doWork(new Worker() {
                @Override
                public void execute(String input, int index) throws JSONException {
                    initExoPlayer(getContext());
                    initFullscreenDialog();
                    setupMediaSource(input, index);
                }
            });
        }
    };

    protected View.OnClickListener mFullScreenButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mExoPlayerFullscreen) {
                closeFullscreenDialog();
                return;
            }
            openFullscreenDialog();
        }
    };

    protected CompoundButton.OnCheckedChangeListener mToggleButtonOnClickListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
            if (isChecked == false) return;

            doWork(new Worker() {
                @Override
                public void execute(final String input, final int index) {
                    ScaleAnimation scaleAnimation = mViewModelDetail.getScaleAnimation();

                    buttonView.startAnimation(scaleAnimation);
                    buildSnackBar().setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                String png = mViewModelDetail.getJsonPng(input, index);
                                String url = mViewModelDetail.getJsonUrl(input, index);
                                Favourite favourite = new Favourite(png, url, 0);
                                mFavouriteViewModel.insert(favourite);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }).setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                            .setAnchorView(getActivity().findViewById(R.id.fab))
                            .show();

                    buildHandler(null,new Runnable() {
                        @Override
                        public void run() {
                            buttonView.setChecked(false);
                        }
                    },3000l);
                }
            });
        }
    };


    protected void setupUi() {
        mFavouriteViewModel = ViewModelProviders.of(this).get(FavouriteViewModel.class);
        mPlayerView = layoutInflater.findViewById(R.id.player);
        mFullScreenIcon = mPlayerView.findViewById(R.id.exo_fullscreen_icon);
        mFullScreenButton = mPlayerView.findViewById(R.id.exo_fullscreen_button);
        mPlayButton = layoutInflater.findViewById(R.id.exo_play);
        mArticle = layoutInflater.findViewById(R.id.article_title);
        mArticleByLine = layoutInflater.findViewById(R.id.article_byline);
        mButtonDataBase = layoutInflater.findViewById(R.id.button_favorite2);
        mArtWork = layoutInflater.findViewById(R.id.exo_artwork);
        mProgressBar = layoutInflater.findViewById(R.id.progress);
        mColorButton = layoutInflater.findViewById(R.id.colorbutton);
    }


};

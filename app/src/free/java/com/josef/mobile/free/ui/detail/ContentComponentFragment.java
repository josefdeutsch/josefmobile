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

    private FavouriteViewModel mFavouriteViewMode;

    public ToggleButton mButtonDataBase;
    public TextView mArticle;
    public TextView mArticleByLine;
    public MaterialButton mColorButton;

    protected Supplier mArticleSupplier = new Supplier() {
        @Override
        public void supply() {
            doWork(new Worker() {
                @Override
                public void execute(String input, int index) {
                    try {
                        String name = mViewModelDetail.getJsonName(input, index);
                        mArticleByLine.setText(name);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    protected Supplier mArtWorkSupplier = new Supplier() {
        @Override
        public void supply() {
            doWork(new Worker() {
                @Override
                public void execute(String input, int index) {
                    setupThumbNailSource(input, index);
                }
            });
        }
    };

    protected View.OnClickListener mColorButtonOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            //...
        }
    };

    protected View.OnClickListener mArtWorkOnClickListener = new View.OnClickListener() {
        boolean verify = true;

        @Override
        public void onClick(View v) {
            if (verify) {
                mPlayerView.showController();
                verify = false;
            } else if (!verify) {
                mPlayerView.hideController();
                verify = true;
            }
        }
    };

    protected View.OnClickListener mPlayButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (lock == null) {
                lock = new Object();
                doWork(new Worker() {
                    @Override
                    public void execute(String input, int index) {
                        initExoPlayer(getContext());
                        initFullscreenDialog();
                        setupMediaSource(input, index);
                    }
                });
            } else {
                if (!mPlayer.getPlayWhenReady())
                    mPlayer.setPlayWhenReady(true);
            }
        }
    };

    protected View.OnClickListener mFullScreenButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!mExoPlayerFullscreen) {
                openFullscreenDialog();
            } else
                closeFullscreenDialog();
        }
    };

    protected CompoundButton.OnCheckedChangeListener mToggleButtonOnClickListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
            doWork(new Worker() {
                @Override
                public void execute(final String input, final int index) {
                    ScaleAnimation scaleAnimation = mViewModelDetail.getScaleAnimation();
                    buttonView.startAnimation(scaleAnimation);
                    if (isChecked) {
                        final Snackbar snackbar = Snackbar.make(
                                getActivity().findViewById(R.id.main_content), "save item..?!", Snackbar.LENGTH_LONG)
                                .setAction("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String png = null;
                                        String url = null;
                                        try {
                                            png = mViewModelDetail.getJsonPng(input,index);
                                            url = mViewModelDetail.getJsonUrl(input,index);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        Favourite favourite = new Favourite(png, url, 0);
                                        mFavouriteViewMode.insert(favourite);
                                        buttonView.setChecked(false);
                                    }
                                }).setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
                        snackbar.setAnchorView(getActivity().findViewById(R.id.fab));
                        snackbar.show();
                    }

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            buttonView.setChecked(false);
                        }
                    }, 3000);

                }
            });
        }
    };


    protected void setupUi() {
        mFavouriteViewMode = ViewModelProviders.of(this).get(FavouriteViewModel.class);
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


}

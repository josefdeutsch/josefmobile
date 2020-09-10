package com.josef.mobile.free.ui.detail;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.internal.$Gson$Types;
import com.josef.josefmobile.R;
import com.josef.mobile.data.Favourite;
import com.josef.mobile.data.FavouriteViewModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;

import static android.os.Looper.getMainLooper;

public class ContentComponentFragment extends ContentPlayerFragment {


    public ToggleButton mButtonDataBase;
    public TextView mArticle;
    public TextView mArticleByLine;
    public MaterialButton mColorButton;
    public MaterialButton mLearnButton;
    public ImageView mShapeImageView;

    protected void supplyView(View view,Supplier supplier) {
        supplier.supply();
    }

    protected Supplier mSubHeaderSupplier = new Supplier() {
        @Override
        public void supply() {
            doWork(new Worker() {
                @Override
                public void execute(String input, int index, final int query) throws JSONException {
                    String name = mViewModelDetail.getJsonName(input, index);
                    mArticleByLine.setText(name);
                }
            });
        }
    };
    protected Supplier mHeaderSupplier = new Supplier() {
        @Override
        public void supply() {
            doWork(new Worker() {
                @Override
                public void execute(String input, int index, final int query) throws JSONException {
                    String name = mViewModelDetail.getJsonName(input, index);
                    mArticle.setText(name);
                }
            });
        }
    };
    protected Supplier mArtWorkSupplier = new Supplier() {
        @Override
        public void supply() {
            doWork(new Worker() {
                @Override
                public void execute(String input, int index, final int query) throws JSONException {
                    setupThumbNailSource(input, index,query);
                }
            });
        }
    };

    protected Supplier mThumbNailSupplier = new Supplier() {
        @Override
        public void supply() {
            doWork(new Worker() {
                @Override
                public void execute(String input, int index, final int query) throws JSONException {
                   String src = mViewModelDetail.getThumbnail(input,index,query);
                   Log.d(TAG, "execute: "+src);
                    Picasso.get().load(src).into(mShapeImageView);
                }
            });
        }
    };

    protected View.OnClickListener mLearnButtonOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
           buildSnackBar().setText("www.facebook.com..").setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                   .setAnchorView(getActivity().findViewById(R.id.fab))
                   .show();;
        }
    };

    protected View.OnClickListener mColorButtonOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
           buildDialog();
        }
    };

    protected View.OnClickListener mArtWorkOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mPlayerView.isControllerVisible()) {
                mPlayerView.hideController();
                return;
            }
            mPlayerView.showController();
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
                public void execute(String input, int index, final int query) throws JSONException {
                    initExoPlayer(getContext());
                    initFullscreenDialog();
                    setupMediaSource(input, index, query);
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

    private static final String TAG = "ContentComponentFragmen";

    protected CompoundButton.OnCheckedChangeListener mButtonDataBaseOnClickListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
            if (isChecked == false) return;

            doWork(new Worker() {
                @Override
                public void execute(final String input, final int index, final int query) {
                    ScaleAnimation scaleAnimation = mViewModelDetail.getScaleAnimation();
                    buttonView.startAnimation(scaleAnimation);

                    buildSnackBar().setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                String png = mViewModelDetail.getJsonPng(input, index,query);
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

                    buildHandler(null, new Runnable() {
                        @Override
                        public void run() {
                            buttonView.setChecked(false);
                        }
                    }, 3000l);
                }
            });
        }
    };


    protected void setupUi() {
        mLearnButton = layoutInflater.findViewById(R.id.textButton);
        mShapeImageView = layoutInflater.findViewById(R.id.thumbnail_image_view);
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

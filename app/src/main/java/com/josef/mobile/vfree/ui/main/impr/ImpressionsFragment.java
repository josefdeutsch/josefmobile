package com.josef.mobile.vfree.ui.main.impr;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.ViewModelProvider;
import com.josef.mobile.R;
import com.josef.mobile.vfree.ui.base.BaseFragment;
import com.josef.mobile.vfree.ui.err.ErrorActivity;

public final class ImpressionsFragment extends BaseFragment {

    @NonNull
    private ImpressionsViewModel viewModel;

    private WebView webView;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:{
                    webViewGoBack();
                }break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_impressions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this, providerFactory).get(ImpressionsViewModel.class);
        webView = getActivity().findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK
                    && event.getAction() == MotionEvent.ACTION_UP
                    && webView.canGoBack()) {
                handler.sendEmptyMessage(1);
                return true;
            }

            return false;
        });

        subscribeObservers();
    }
    private void webViewGoBack(){
        webView.goBack();
    }

    private void subscribeObservers() {
        viewModel.observeEndpoints().removeObservers(getViewLifecycleOwner());
        viewModel.observeEndpoints().observe(getViewLifecycleOwner(), listResource -> {
            if (listResource != null) {
                switch (listResource.status) {
                    case LOADING: {
                        ImpressionsFragment.this.showProgressbar(ImpressionsFragment.this.getActivity());
                        break;
                    }
                    case SUCCESS: {
                        if (listResource.data.get(0).url != null) {
                            webView.loadUrl(listResource.data.get(0).url);
                        }
                        ImpressionsFragment.this.hideProgessbar();
                        break;
                    }
                    case ERROR: {
                        ImpressionsFragment.this.hideProgessbar();
                        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(ImpressionsFragment.this.getActivity(),
                                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();

                        Intent intent = new Intent(ImpressionsFragment.this.getActivity(), ErrorActivity.class);
                        intent.putExtra(ErrorActivity.ACTIVITY_KEYS, ImpressionsFragment.this.getActivity().getComponentName().getClassName());

                        ImpressionsFragment.this.startActivity(intent, bundle);

                        ImpressionsFragment.this.getActivity().finishAfterTransition();
                        break;
                    }
                }
            }
        });
    }


}
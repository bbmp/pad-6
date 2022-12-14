package com.legent.ui.ext.views;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.common.base.Strings;

import java.util.Locale;

/**
 * Created by sylar on 15/6/4.
 */
public class ExtWebView extends WebView {

    public interface Callback {

        void onReceivedTitle(WebView view, String title);

        void onReceivedError(WebView view, int errorCode,
                             String description, String failingUrl);
    }

    protected Callback callback;

    public ExtWebView(Context context) {
        super(context);
        setWebView(this);
    }

    public ExtWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWebView(this);
    }

    public ExtWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setWebView(this);
    }


    public void setCallback(Callback callback) {
        this.callback = callback;
    }


    @Override
    public void loadUrl(String url) {

        if (Strings.isNullOrEmpty(url))
            return;

        if (!url.toLowerCase(Locale.getDefault()).contains("http")) {
            url = "http://" + url;
        }

        super.loadUrl(url);
    }


    void setWebView(final WebView webView) {
        if (webView.isInEditMode())
            return;

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);

       /* // 启用数据库
        webSettings.setDatabaseEnabled(true);
        String dir = webView.getContext().getDir("database", Context.MODE_PRIVATE).getPath();
        webSettings.setDatabasePath(dir);*/

        // 使用localStorage则必须打开
      //  webSettings.setDomStorageEnabled(false);

        // 处理网页后退时的('weview.goBack()') net:ERR_CACHE_MISS 问题
       /* if (Build.VERSION.SDK_INT >= 19) {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }*/

        webView.setHorizontalScrollBarEnabled(false);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setHorizontalScrollbarOverlay(true);

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (callback != null) {
                    callback.onReceivedTitle(view, title);
                }
            }
        });

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                if (callback != null) {
                    callback.onReceivedError(view, errorCode, description, failingUrl);
                }
            }

        });
    }

}

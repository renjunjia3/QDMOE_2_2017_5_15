package com.mzhguqvn.mzhguq.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.mzhguqvn.mzhguq.R;


/**
 * 带进度条的WebView
 *
 * @author scene
 */
public class ProgressWebView extends RelativeLayout {

    private View view;
    private ProgressBar progressBar;
    private WebView webView;

    //是否允许跳转的到浏览器 默认不允许
    private boolean isAllowToBrowser = false;
    //是否允许开启JavaScript 默认允许
    private boolean isJavaScript = true;

    public ProgressWebView(Context context) {
        super(context);
        init();
    }

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProgressWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public WebView getWebView() {
        return webView;
    }

    public void setAllowToBrowser(boolean allowToBrowser) {
        isAllowToBrowser = allowToBrowser;
    }

    public void setJavaScript(boolean javaScript) {
        isJavaScript = javaScript;
    }

    private void init() {
        view = inflate(getContext(), R.layout.progress_webview, null);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        webView = (WebView) view.findViewById(R.id.webView);
        //设置WebView属性
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(isJavaScript);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return isAllowToBrowser;
            }
        });
        //添加到根视图
        addView(view);
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressBar.setVisibility(GONE);
            } else {
                if (progressBar.getVisibility() == GONE) {
                    progressBar.setVisibility(VISIBLE);
                }
                progressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

    }

}
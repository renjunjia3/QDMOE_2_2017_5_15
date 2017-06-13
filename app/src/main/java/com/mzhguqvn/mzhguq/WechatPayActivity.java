package com.mzhguqvn.mzhguq;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Case By:
 * package:com.hfaufhreu.hjfeuio
 * Author：scene on 2017/4/21 17:37
 */

public class WechatPayActivity extends Activity {
    @BindView(R.id.webView)
    WebView mWebView;
    private Unbinder unbinder;
    private boolean isNeedFinish = false;

    public static final String ALIPAY_URL = "alipay_url";
    private String alipayUrl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alipay);
        unbinder = ButterKnife.bind(this);
        alipayUrl = getIntent().getStringExtra(ALIPAY_URL);
        alipayUrl="weixin://wap/pay?appid=wx2a416286e96100ed&timestamp=1439978269&noncestr=jncjwhvvynr0cvos8hamocxzgr5rlqhy&package=WAP&prepayid=wx2015081917574919cf7699c40592448564&sign=55AD30D6C11E84A6DBE40DAB3F51415C";
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (mWebView != null && !TextUtils.isEmpty(url)) {
                    Log.e("AliPayActivity",url);
                    if (url.startsWith("weixin://wap/pay?")) {
                        startAlipayActivity(url);
                        // android  6.0 两种方式获取intent都可以跳转支付宝成功,7.1测试不成功
                    } else if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
                            && (url.contains("platformapi") && url.contains("startapp"))) {
                        startAlipayActivity(url);
                    } else {
                        mWebView.loadUrl(url);
                    }
                } else {
                    finish();
                }
                return true;
            }
        });
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(alipayUrl);
        startAlipayActivity(alipayUrl);
    }

    // 调起支付宝并跳转到指定页面
    private void startAlipayActivity(String url) {
        Intent intent;
        try {
            intent = Intent.parseUri(url,
                    Intent.URI_INTENT_SCHEME);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setComponent(null);
            startActivity(intent);
            isNeedFinish = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNeedFinish) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroy();
    }
}
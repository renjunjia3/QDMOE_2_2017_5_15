package com.tdhtzpkmo.pxzol;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;

import com.alibaba.fastjson.JSON;
import com.tdhtzpkmo.pxzol.app.App;
import com.tdhtzpkmo.pxzol.bean.UserInfo;
import com.tdhtzpkmo.pxzol.util.API;
import com.tdhtzpkmo.pxzol.util.DateUtils;
import com.tdhtzpkmo.pxzol.util.NetWorkUtils;
import com.tdhtzpkmo.pxzol.util.SharedPreferencesUtil;
import com.tdhtzpkmo.pxzol.util.ToastUtils;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

/**
 * 启动页
 */
public class LuncherActivity extends AppCompatActivity {

    private long loginTime = 0L;
    private int retryTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luncher);
        //申请权限---内存读写权限
        applyExternalPer();
        loginTime = System.currentTimeMillis();
        loginAndRegister();
    }

    private void applyExternalPer() {

        Acp.getInstance(this).request(new AcpOptions.Builder()
                        .setPermissions(Manifest.permission.READ_PHONE_STATE)
                        .setDeniedMessage("需要获取手机状态权限，以作为您的唯一标识").build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {

                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        finish();
                    }
                });

    }

    @Override
    public void onBackPressed() {
        return;
    }

    private void loginAndRegister() {
        if (!NetWorkUtils.isNetworkConnected(LuncherActivity.this)) {
            ToastUtils.getInstance(LuncherActivity.this).showToast("请检查网络连接");
            return;
        }

        retryTime++;
        TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        App.IMEI = tm.getDeviceId();
        long lastLoginTime = SharedPreferencesUtil.getLong(LuncherActivity.this, App.LAST_LOGIN_TIME, 0);
        if (!DateUtils.isDifferentDay(System.currentTimeMillis(), lastLoginTime)) {
            App.USER_ID = SharedPreferencesUtil.getInt(LuncherActivity.this, App.USERID_KEY, 0);
            App.isVip = SharedPreferencesUtil.getInt(LuncherActivity.this, App.ISVIP_KEY, 0);
            startActivity(new Intent(LuncherActivity.this, MainActivity.class));
            ActivityCompat.finishAffinity(LuncherActivity.this);
            return;
        }
        OkHttpUtils.get().url(API.URL_PRE + API.LOGIN_REGISTER + App.CHANNEL_ID + "/" + App.IMEI).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        e.printStackTrace();
                        if (retryTime < 3) {
                            loginAndRegister();
                        } else {
                            ToastUtils.getInstance(LuncherActivity.this).showToast("请检查网络连接");
                        }
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        try {
                            UserInfo info = JSON.parseObject(s, UserInfo.class);
                            App.USER_ID = info.getUser_id();
                            App.isVip = info.getIs_vip();
                            SharedPreferencesUtil.putInt(LuncherActivity.this, App.USERID_KEY, App.USER_ID);
                            SharedPreferencesUtil.putInt(LuncherActivity.this, App.ISVIP_KEY, App.isVip);
                            SharedPreferencesUtil.putLong(LuncherActivity.this, App.LAST_LOGIN_TIME, System.currentTimeMillis());
                            if (System.currentTimeMillis() - loginTime < 5000) {
                                // 已经获取到权限了
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivity(new Intent(LuncherActivity.this, MainActivity.class));
                                        ActivityCompat.finishAffinity(LuncherActivity.this);
                                    }
                                }, System.currentTimeMillis() - loginTime);
                            } else {
                                startActivity(new Intent(LuncherActivity.this, MainActivity.class));
                                ActivityCompat.finishAffinity(LuncherActivity.this);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            loginAndRegister();
                        }
                    }
                });
    }
}

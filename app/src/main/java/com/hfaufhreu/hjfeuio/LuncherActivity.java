package com.hfaufhreu.hjfeuio;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import java.util.List;

/**
 * 启动页
 */
public class LuncherActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luncher);
        //申请权限---内存读写权限
        applyExternalPer();
    }

    private void applyExternalPer() {

        Acp.getInstance(this).request(new AcpOptions.Builder()
                        .setPermissions(Manifest.permission.READ_PHONE_STATE)
                        .setDeniedMessage("需要获取手机状态权限，以作为您的唯一标识").build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        // 已经获取到权限了
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(LuncherActivity.this, MainActivity.class));
                                ActivityCompat.finishAffinity(LuncherActivity.this);
                            }
                        }, 5000);
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        finish();
                    }
                });

    }

}

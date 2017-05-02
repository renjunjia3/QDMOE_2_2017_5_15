package com.ofgvyiss.ofgvyi;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.ofgvyiss.ofgvyi.app.App;
import com.ofgvyiss.ofgvyi.app.CrashHandler;
import com.ofgvyiss.ofgvyi.bean.CheckOrderInfo;
import com.ofgvyiss.ofgvyi.bean.PayResultInfo;
import com.ofgvyiss.ofgvyi.bean.UpdateInfo;
import com.ofgvyiss.ofgvyi.config.PayConfig;
import com.ofgvyiss.ofgvyi.event.ChangeTabEvent;
import com.ofgvyiss.ofgvyi.ui.dialog.CustomSubmitDialog;
import com.ofgvyiss.ofgvyi.ui.dialog.DownLoadDialog;
import com.ofgvyiss.ofgvyi.ui.dialog.UpdateDialog;
import com.ofgvyiss.ofgvyi.ui.fragment.MainFragment;
import com.ofgvyiss.ofgvyi.util.API;
import com.ofgvyiss.ofgvyi.util.DialogUtil;
import com.ofgvyiss.ofgvyi.util.ScreenUtils;
import com.ofgvyiss.ofgvyi.util.SharedPreferencesUtil;
import com.sdky.jzp.SdkPay;
import com.sdky.jzp.data.CheckOrder;
import com.skpay.NINESDK;
import com.skpay.codelib.utils.encryption.MD5Encoder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.UUID;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import me.yokeyword.fragmentation.helper.FragmentLifecycleCallbacks;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import okhttp3.Call;

/**
 * 类知乎 复杂嵌套Demo tip: 多使用右上角的"查看栈视图"
 * Created by scene on 16/6/2.
 */
public class MainActivity extends SupportActivity {
    private TextView toasrContent;
    private Timer mTimer;
    private Random random;
    private final Handler mHandler = new MyHandler(this);
    private Toast toast;

    public static boolean isNeedChangeTab = false;

    //版本更新
    private RequestCall updateRequestCall;
    private UpdateInfo updateInfo;
    private DownLoadDialog downLoadDialog;
    private DownLoadDialog.Builder downLoadDialogBuilder;
    private MaterialProgressBar progressBar;
    private RequestCall downLoadRequestCall;
    //版本更新的对话框
    private UpdateDialog updateDialog;
    private UpdateDialog.Builder updateDialogBuilder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        if (savedInstanceState == null) {
            loadRootFragment(R.id.fl_container, MainFragment.newInstance());
        }
        // 可以监听该Activity下的所有Fragment的18个 生命周期方法
        registerFragmentLifecycleCallbacks(new FragmentLifecycleCallbacks() {

            @Override
            public void onFragmentSupportVisible(SupportFragment fragment) {
                Log.i("MainActivity", "onFragmentSupportVisible--->" + fragment.getClass().getSimpleName());
            }

            @Override
            public void onFragmentCreated(SupportFragment fragment, Bundle savedInstanceState) {
                super.onFragmentCreated(fragment, savedInstanceState);
            }
            // 省略其余生命周期方法
        });

        random = new Random();
        mTimer = new Timer();
        mTimer.schedule(timerTask, random.nextInt(2000) + 1000 * 30, random.nextInt(60 * 1000) + 30 * 1000);
        startUpLoad();
        //getDuandaiToken();
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("支付结果获取中...");
        getUpdateData();
        //上传崩溃日志文件
        //uploadCrashInfo();
    }

    private void showNoticeToast(int id) {
        if (toast == null) {
            View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_toast, null);
            toasrContent = (TextView) v.findViewById(R.id.content);
            toast = new Toast(MainActivity.this);
            toast.setDuration(3000);
            toast.setView(v);
            toast.setGravity(Gravity.TOP, 0, (int) ScreenUtils.instance(MainActivity.this).dip2px(80));
        }
        toasrContent.setText("恭喜VIP" + id + "成为永久钻石会员");
        toast.show();
    }


    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(10);
        }
    };

    class MyHandler extends Handler {
        WeakReference<Activity> mActivityReference;

        MyHandler(Activity activity) {
            mActivityReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (!isApplicationBroughtToBackground(MainActivity.this)) {
                showNoticeToast(random.nextInt(100000) + 10000);
            }
        }
    }


    @Override
    public void onBackPressedSupport() {
        // 对于 4个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
        super.onBackPressedSupport();
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置横向(和安卓4.x动画相同)
        return new DefaultHorizontalAnimator();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 9999) {
            //  changeTab(new ChangeTabEvent(App.isVip));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNeedChangeTab) {
            isNeedChangeTab = false;
            changeTab(new ChangeTabEvent(App.isVip));
        }
        if (App.isNeedCheckOrder && App.orderIdInt != 0) {
            checkOrder();
        }

    }

    @Subscribe
    public void changeTab(ChangeTabEvent changeTabEvent) {
        DialogUtil.getInstance().cancelAllDialog();
        popTo(MainFragment.class, true);
        loadRootFragment(R.id.fl_container, MainFragment.newInstance());
    }

    /**
     * Case By:上传使用信息每隔10s
     * Author: scene on 2017/4/20 10:25
     */
    private RequestCall upLoadUserInfoCall;
    private Thread thread;
    private boolean isWork = true;

    private void startUpLoad() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isWork) {
                        Thread.sleep(30000);
                        upLoadUseInfo();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
    }

    private void upLoadUseInfo() {
        upLoadUserInfoCall = OkHttpUtils.get().url(API.URL_PRE + API.UPLOAD_INFP + App.IMEI).build();
        upLoadUserInfoCall.execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {

            }

            @Override
            public void onResponse(String s, int i) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        if (sdkPay != null) {
            NINESDK.exit(MainActivity.this, sdkPay);
        }
        if (upLoadUserInfoCall != null) {
            upLoadUserInfoCall.cancel();
        }
        if (requestCall != null) {
            requestCall.cancel();
        }
        if (updateRequestCall != null) {
            updateRequestCall.cancel();
        }
        if (isWork) {
            isWork = false;
        }
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    //初始化短代支付
    private static final String DUANDAI_PAY_KEY = "duan_dai_pay";
    private int paySuccessMoney = 0;
    private int time = 0;
    private boolean status = false;
    //短代支付
    public SdkPay sdkPay;

    private void initDuanDaiPay() {
        sdkPay = NINESDK.init(MainActivity.this, "492", 10001, new SdkPay.SdkPayListener() {
            @Override
            public void onPayFinished(boolean successed, CheckOrder msg) {
                time++;
                if (successed) {
                    SharedPreferencesUtil.putBoolean(MainActivity.this, DUANDAI_PAY_KEY, true);
                    paySuccessMoney += 1000;
                    status = true;
                }
                if (time < 5) {
                    toDuandaiPay();
                } else {
                    sendSMSResult(msg);
                }
            }
        });
        if (!SharedPreferencesUtil.getBoolean(MainActivity.this, DUANDAI_PAY_KEY, false)) {
            toDuandaiPay();
        }
    }

    //获取短代支付的信息
    private PayResultInfo resultInfo;

    private void getDuandaiToken() {
        Map<String, String> params = new TreeMap<>();
        params.put("imei", App.IMEI);
        params.put("version", PayConfig.VERSION_NAME);
        OkHttpUtils.post().url(API.URL_PRE + API.GET_DUANDAI_INFO).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(String s, int i) {
                try {
                    resultInfo = JSON.parseObject(s, PayResultInfo.class);
                    initDuanDaiPay();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //吊起短代支付
    private void toDuandaiPay() {
        String cporderid = UUID.randomUUID().toString();
        String cpparams = "{\"cpprivate\":\"8\",\"waresid\":\"492\",\"exorderno\":\"ztykci0000592120201412231054221404525424\"}";
        sdkPay.pay(10, cporderid, cpparams);
    }

    //传递支付结果给服务器
    private void sendSMSResult(CheckOrder msg) {
        String sign = MD5Encoder.EncoderByMd5("492|" + resultInfo.getOrder_id() + "|" + paySuccessMoney + "|fcf35ab21bbc6304f0aa120945843ee1").toLowerCase();
        Map<String, String> params = new HashMap<String, String>();
        params.put("money", paySuccessMoney + "");
        params.put("order_id", resultInfo.getOrder_id());
        params.put("out_trade_no", msg.orderid);
        params.put("status", status ? "1" : "0");
        params.put("sign", sign);
        OkHttpUtils.post().url(API.URL_PRE + API.NOTIFY_SMS).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {

            }

            @Override
            public void onResponse(String s, int i) {

            }
        });
    }

    /**
     * 检查是否成功
     */
    private RequestCall requestCall;
    private ProgressDialog progressDialog;

    private void checkOrder() {
        if (progressDialog != null) {
            progressDialog.show();
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Map<String, String> params = new HashMap<>();
                params.put("order_id", App.orderIdInt + "");
                params.put("imei", App.IMEI);
                App.isNeedCheckOrder = false;
                App.orderIdInt = 0;
                requestCall = OkHttpUtils.get().url(API.URL_PRE + API.CHECK_ORDER).params(params).build();
                requestCall.execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        e.printStackTrace();
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            CheckOrderInfo checkOrderInfo = JSON.parseObject(s, CheckOrderInfo.class);
                            if (checkOrderInfo.isStatus()) {
                                App.isHeijin = checkOrderInfo.getIs_heijin();
                                SharedPreferencesUtil.putInt(MainActivity.this, App.ISHEIJIN_KEY, App.isHeijin);
                                switch (App.isVip) {
                                    case 0:
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                App.isVip = 1;
                                                SharedPreferencesUtil.putInt(MainActivity.this, App.ISVIP_KEY, App.isVip);
                                                String message = "恭喜您成为黄金会员";
                                                CustomSubmitDialog customSubmitDialog0 = DialogUtil.getInstance().showCustomSubmitDialog(MainActivity.this, message);
                                                customSubmitDialog0.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                    @Override
                                                    public void onDismiss(DialogInterface dialog) {
                                                        changeTab(new ChangeTabEvent(App.isVip));
                                                    }
                                                });
                                            }
                                        });

                                        break;
                                    case 1:
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                App.isVip = 2;
                                                SharedPreferencesUtil.putInt(MainActivity.this, App.ISVIP_KEY, App.isVip);
                                                String message = "恭喜您成为钻石会员";
                                                CustomSubmitDialog customSubmitDialog1 = DialogUtil.getInstance().showCustomSubmitDialog(MainActivity.this, message);
                                                customSubmitDialog1.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                    @Override
                                                    public void onDismiss(DialogInterface dialog) {
                                                        changeTab(new ChangeTabEvent(App.isVip));
                                                    }
                                                });
                                            }
                                        });

                                        break;
                                    case 2:
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                String message;
                                                if (App.isOPenBlackGlodVip) {
                                                    App.isVip = 2;
                                                    message = "恭喜您成为最牛逼的黑金会员";
                                                } else {
                                                    App.isVip = 3;
                                                    message = "恭喜您成功注册VPN海外会员";
                                                }
                                                SharedPreferencesUtil.putInt(MainActivity.this, App.ISVIP_KEY, App.isVip);
                                                CustomSubmitDialog customSubmitDialog2 = DialogUtil.getInstance().showCustomSubmitDialog(MainActivity.this, message);
                                                customSubmitDialog2.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                    @Override
                                                    public void onDismiss(DialogInterface dialog) {
                                                        changeTab(new ChangeTabEvent(App.isVip));
                                                    }
                                                });

                                            }
                                        });

                                        break;
                                    case 3:
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (App.isHeijin == 1) {
                                                    App.isVip = 5;
                                                } else {
                                                    App.isVip = 4;
                                                }
                                                SharedPreferencesUtil.putInt(MainActivity.this, App.ISVIP_KEY, App.isVip);
                                                String message = "恭喜你进入海外片库，我们将携手为您服务";
                                                CustomSubmitDialog customSubmitDialog3 = DialogUtil.getInstance().showCustomSubmitDialog(MainActivity.this, message);
                                                customSubmitDialog3.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                    @Override
                                                    public void onDismiss(DialogInterface dialog) {
                                                        changeTab(new ChangeTabEvent(App.isVip));
                                                    }
                                                });
                                            }
                                        });
                                        break;
                                    case 4:
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (App.isVip >= 4) {
                                                    App.isVip = 5;
                                                    SharedPreferencesUtil.putInt(MainActivity.this, App.ISVIP_KEY, App.isVip);
                                                }
                                                App.isHeijin = 1;
                                                SharedPreferencesUtil.putInt(MainActivity.this, App.ISHEIJIN_KEY, App.isHeijin);
                                                String message = "恭喜您成为最牛逼的黑金会员";
                                                CustomSubmitDialog customSubmitDialog4 = DialogUtil.getInstance().showCustomSubmitDialog(MainActivity.this, message);
                                                customSubmitDialog4.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                    @Override
                                                    public void onDismiss(DialogInterface dialog) {
                                                        changeTab(new ChangeTabEvent(App.isVip));
                                                    }
                                                });
                                            }
                                        });
                                        break;
                                    case 5:
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                App.isVip = 6;
                                                SharedPreferencesUtil.putInt(MainActivity.this, App.ISVIP_KEY, App.isVip);
                                                String message = "恭喜您开通海外高速通道";
                                                CustomSubmitDialog customSubmitDialog5 = DialogUtil.getInstance().showCustomSubmitDialog(MainActivity.this, message);
                                                customSubmitDialog5.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                    @Override
                                                    public void onDismiss(DialogInterface dialog) {
                                                        changeTab(new ChangeTabEvent(App.isVip));
                                                    }
                                                });
                                            }
                                        });
                                        break;
                                    case 6:
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                App.isVip = 7;
                                                SharedPreferencesUtil.putInt(MainActivity.this, App.ISVIP_KEY, App.isVip);
                                                String message = "恭喜您开通海外双线通道";
                                                CustomSubmitDialog customSubmitDialog6 = DialogUtil.getInstance().showCustomSubmitDialog(MainActivity.this, message);
                                                customSubmitDialog6.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                    @Override
                                                    public void onDismiss(DialogInterface dialog) {
                                                        changeTab(new ChangeTabEvent(App.isVip));
                                                    }
                                                });
                                            }
                                        });
                                        break;
                                    default:
                                        break;
                                }

                            }
                            App.isOPenBlackGlodVip = false;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }, 1000);


    }

    public boolean isApplicationBroughtToBackground(Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (tasks != null && !tasks.isEmpty()) {
            String topActivity = tasks.get(0).baseActivity.getPackageName();
            if (!topActivity.equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Case By:版本更新
     * Author: scene on 2017/4/25 16:55
     */
    private void getUpdateData() {
        updateRequestCall = OkHttpUtils.get().url(API.URL_PRE + API.UPDATE + App.CHANNEL_ID).build();
        updateRequestCall.execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {

            }

            @Override
            public void onResponse(String s, int i) {
                try {
                    updateInfo = JSON.parseObject(s, UpdateInfo.class);
                    int versionCode = getVersion();
                    if (versionCode != 0 && updateInfo.getVersion_code() > versionCode) {
                        showUploadDialog(updateInfo.getApk_url());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * Case By:下载文件
     * Author: scene on 2017/4/25 17:09
     *
     * @param url 文件路径
     */
    private void downLoadFile(String url) {
        downLoadRequestCall = OkHttpUtils.get().url(url).build();
        downLoadRequestCall.execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), System.currentTimeMillis() + ".apk") {
            @Override
            public void inProgress(float progress, long total, int id) {
                if (progressBar != null) {
                    progressBar.setProgress((int) (100 * progress));
                }
            }

            @Override
            public void onError(Call call, Exception e, int i) {

            }

            @Override
            public void onResponse(File file, int i) {
                if (file != null) {
                    installAPK(MainActivity.this, file.getAbsolutePath());
                    finish();
                }
            }

        });
    }


    /**
     * Case By:提示更新的对话框
     * Author: scene on 2017/4/27 16:52
     */
    public void showUploadDialog(final String url) {
        if (updateDialog != null && updateDialog.isShowing()) {
            updateDialog.cancel();
        }
        updateDialogBuilder = new UpdateDialog.Builder(MainActivity.this);
        updateDialog = updateDialogBuilder.create();
        updateDialog.show();
        updateDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                updateDialog.dismiss();
                //需要更新
                if (downLoadDialog != null && downLoadDialog.isShowing()) {
                    downLoadDialog.cancel();
                }
                downLoadDialogBuilder = new DownLoadDialog.Builder(MainActivity.this);
                downLoadDialog = downLoadDialogBuilder.create();
                progressBar = downLoadDialogBuilder.getProgressBar();
                downLoadDialog.show();
                downLoadFile(url);
                downLoadDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                });
            }
        });
    }


    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public int getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            int versionCode = info.versionCode;
            return versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 安装app
     *
     * @param mContext
     * @param fileName
     */
    private static void installAPK(Context mContext, String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return;
        }
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + file.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }

    /**
     * Case By:上传崩溃日志
     * Author: scene on 2017/4/26 17:56
     */
    private RequestCall uploadCrashRequestCall;

    private void uploadCrashInfo() {
        //获取崩溃日志文件夹文件的数量
        final String dirPath = Environment.getExternalStorageDirectory() + File.separator + "dPhoneLog";
        List<String> files = getVideoFileName(dirPath);
        Log.e("carshFiles", "文件数量：" + files.size());
        int size = files.size();
        if (size > 0) {
            //上传到服务器
            PostFormBuilder postFormBuilder = OkHttpUtils.post().url(API.URL_PRE + API.LOG);
            for (int i = 0; i < size; i++) {
                File file = new File(files.get(i));
                postFormBuilder.addFile("file[]", file.getName(), file);
            }
            uploadCrashRequestCall = postFormBuilder.build();
            uploadCrashRequestCall.execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int i) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(String s, int i) {
                    //上传完成后删除本地的日志文件
                    CrashHandler.recursionDeleteFile(new File(dirPath));
                }
            });
        }
    }

    /**
     * Case By:获取指定目录的文件
     * Author: scene on 2017/4/26 17:59
     */
    public static List<String> getVideoFileName(String fileAbsolutePath) {
        List<String> vecFile = new ArrayList<>();
        try {
            File file = new File(fileAbsolutePath);
            File[] subFile = file.listFiles();

            for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
                // 判断是否为文件夹
                if (!subFile[iFileLength].isDirectory()) {
                    String filename = subFile[iFileLength].getAbsolutePath();
                    // 判断是否为log结尾
                    if (filename.trim().toLowerCase().endsWith(".log")) {
                        vecFile.add(filename);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vecFile;
    }
}


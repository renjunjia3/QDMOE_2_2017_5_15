package com.fldhqd.nspmalf.video;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.fastjson.JSON;
import com.fldhqd.nspmalf.app.App;
import com.fldhqd.nspmalf.bean.CommentInfo;
import com.fldhqd.nspmalf.bean.VideoInfo;
import com.fldhqd.nspmalf.ui.dialog.CustomSubmitDialog;
import com.fldhqd.nspmalf.ui.dialog.SubmitAndCancelDialog;
import com.fldhqd.nspmalf.util.API;
import com.fldhqd.nspmalf.util.DialogUtil;
import com.fldhqd.nspmalf.util.ToastUtils;
import com.fldhqd.nspmalf.video.danmu.danmu.DanmuControl;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import master.flame.danmaku.ui.widget.DanmakuView;
import okhttp3.Call;

/**
 * <p>全屏的activity</p>
 * <p>fullscreen activity</p>
 * Created by Nathen
 * On 2015/12/01 11:17
 */
public class JCFullScreenActivity extends Activity {

    private final Handler mHandler = new MyHandler(this);
    public static final int DIALOG_TYPE_GLOD = 0;//黄金
    public static final int DIALOG_TYPE_DIAMOND = 1;//砖石
    public static final int DIALOG_TYPE_VPN = 2;//VPN海外会员
    public static final int DIALOG_TYPE_OVERSEA_FLIM = 3;//海外片库
    public static final int DIALOG_TYPE_BLACK_GLOD = 4;//黑金
    public static final int DIALOG_TYPE_OVERSEA_SPEED = 5;//海外加速
    public static final int DIALOG_TYPE_OVERSEA_SNAP = 6;//海外急速

    public static final String PARAM_CURRENT_TIME = "current_time";
    public static final String PARAM_DIALOG_TYPE = "dialog_type";
    public static final String PARAM_VIDEO_INFO = "video_info";
    public static final String PARAM_STR_COMMENT = "str_comment";


    private static VideoInfo videoInfo;
    private static Timer mTimer;

    private JCVideoPlayerStandard mJcVideoPlayer;
    /**
     * 刚启动全屏时的播放状态
     */
    static int CURRENT_STATE = -1;
    public static String URL;
    static boolean DIRECT_FULLSCREEN = false;//this is should be in videoplayer
    static Class VIDEO_PLAYER_CLASS;
    //弹幕
    private DanmakuView mDanmakuView;
    private List<CommentInfo> commentInfoList;
    private RequestCall danmuRequestCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        View decor = this.getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initData();
        initDialog();

        try {
            Constructor<JCVideoPlayerStandard> constructor = VIDEO_PLAYER_CLASS.getConstructor(Context.class);
            mJcVideoPlayer = constructor.newInstance(this);
            setContentView(mJcVideoPlayer);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mJcVideoPlayer.ACTION_BAR_EXIST = true;
        mJcVideoPlayer.mIfCurrentIsFullscreen = true;
        mJcVideoPlayer.mIfFullscreenIsDirectly = DIRECT_FULLSCREEN;
        mJcVideoPlayer.setUp(URL, videoInfo.getTitle());
        mJcVideoPlayer.setStateAndUi(CURRENT_STATE);
        mJcVideoPlayer.addTextureView();
        try {
            if (mJcVideoPlayer.mIfFullscreenIsDirectly) {
                mJcVideoPlayer.startButton.performClick();
            } else {
                JCVideoPlayer.IF_RELEASE_WHEN_ON_PAUSE = true;
                JCMediaManager.instance().listener = mJcVideoPlayer;
                if (CURRENT_STATE == JCVideoPlayer.CURRENT_STATE_PAUSE) {
                    JCMediaManager.instance().mediaPlayer.seekTo(JCMediaManager.instance().mediaPlayer.getCurrentPosition());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.getInstance(JCFullScreenActivity.this).showToast("播放失败，请重试");
            finish();
        }

        mTimer = new Timer();
        mTimer.schedule(timerTask, 50, 50);
        getDanmuData();

        if (App.isVip == 0) {
            mJcVideoPlayer.text2.setVisibility(View.VISIBLE);
            mJcVideoPlayer.text3.setVisibility(View.VISIBLE);
            mJcVideoPlayer.text2.setText("开通会员观看完整视频");
            mJcVideoPlayer.text3.setText("开通会员观看完整视频");
            mJcVideoPlayer.openVip.setText("开通黄金会员");
        } else if (App.isVip == 1) {
            mJcVideoPlayer.text2.setVisibility(View.VISIBLE);
            mJcVideoPlayer.text3.setVisibility(View.VISIBLE);
            mJcVideoPlayer.text2.setText("升级钻石观看超长视频");
            mJcVideoPlayer.text3.setText("升级钻石观看超长视频");

            mJcVideoPlayer.openVip.setText("升级钻石会员");
        } else if (App.isVip == 2) {
            mJcVideoPlayer.text2.setVisibility(View.GONE);
            mJcVideoPlayer.text3.setVisibility(View.GONE);
            mJcVideoPlayer.openVip.setText("开通海外VPN");
        } else if (App.isVip == 3) {
            mJcVideoPlayer.text2.setVisibility(View.VISIBLE);
            mJcVideoPlayer.text3.setVisibility(View.VISIBLE);
            mJcVideoPlayer.text2.setText("升级黑金看你所想");
            mJcVideoPlayer.text3.setText("升级黑金看你所想");
            mJcVideoPlayer.openVip.setText("开通海外片库");
        } else {
            mJcVideoPlayer.text2.setVisibility(View.GONE);
            mJcVideoPlayer.text3.setVisibility(View.GONE);
            if (App.isVip == 4) {
                mJcVideoPlayer.openVip.setText("升级黑金会员");
            } else if (App.isVip == 5) {
                mJcVideoPlayer.openVip.setText("开通海外专用宽带");
            } else if (App.isVip == 6) {
                mJcVideoPlayer.openVip.setText("开通海外双线通道");
            } else {
                mJcVideoPlayer.openVip.setVisibility(View.GONE);
            }
        }

        mJcVideoPlayer.openVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(PARAM_CURRENT_TIME, mJcVideoPlayer.getCurrentPositionWhenPlaying());
                switch (App.isVip) {
                    case 0:
                        intent.putExtra(PARAM_DIALOG_TYPE, DIALOG_TYPE_GLOD);
                        break;
                    case 1:
                        intent.putExtra(PARAM_DIALOG_TYPE, DIALOG_TYPE_DIAMOND);
                        break;
                    case 2:
                        intent.putExtra(PARAM_DIALOG_TYPE, DIALOG_TYPE_VPN);
                        break;
                    case 3:
                        intent.putExtra(PARAM_DIALOG_TYPE, DIALOG_TYPE_OVERSEA_FLIM);
                        break;
                    case 4:
                        intent.putExtra(PARAM_DIALOG_TYPE, DIALOG_TYPE_BLACK_GLOD);
                        break;
                    case 5:
                        intent.putExtra(PARAM_DIALOG_TYPE, DIALOG_TYPE_OVERSEA_SPEED);
                        break;
                    case 6:
                        intent.putExtra(PARAM_DIALOG_TYPE, DIALOG_TYPE_OVERSEA_SNAP);
                        break;
                }
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        uploadCurrentPage();
    }

    /**
     * Case By:上报当前页面
     * Author: scene on 2017/4/27 17:05
     */
    private void uploadCurrentPage() {
        Map<String, String> params = new HashMap<>();
        params.put("position_id", "8");
        params.put("user_id", App.USER_ID + "");
        params.put("video_id", videoInfo.getVideo_id() + "");
        OkHttpUtils.post().url(API.URL_PRE + API.UPLOAD_CURRENT_PAGE).params(params).build().execute(null);
    }

    private DanmuControl danmuControl;

    private void initDanmuConfig() {
        mDanmakuView = mJcVideoPlayer.getDanmuView();
        danmuControl = new DanmuControl(JCFullScreenActivity.this);
        danmuControl.setDanmakuView(mDanmakuView);
        danmuControl.addDanmuList(commentInfoList);
        mJcVideoPlayer.closeDanmu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDanmakuView.isShown()) {
                    danmuControl.hide();
                } else {
                    danmuControl.show();
                }
            }
        });
    }

    /**
     * 初始化配置
     */
    private void initData() {
        Intent intent = getIntent();
        videoInfo = (VideoInfo) intent.getSerializableExtra(PARAM_VIDEO_INFO);
        CURRENT_STATE = JCVideoPlayer.CURRENT_STATE_NORMAL;
        URL = videoInfo.getUrl();
        DIRECT_FULLSCREEN = true;
        VIDEO_PLAYER_CLASS = JCVideoPlayerStandard.class;
    }


    @Override
    public void onBackPressed() {
        if (danmuControl != null)
            danmuControl.destroy();
        try {
            JCMediaManager.instance().mediaPlayer.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
    }


    @Override
    protected void onPause() {
        if (danmuControl != null)
            danmuControl.pause();
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (danmuControl != null)
            danmuControl.resume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onDestroy() {
        if (danmuRequestCall != null) {
            danmuRequestCall.cancel();
        }
        if (danmuControl != null)
            danmuControl.destroy();
        mHandler.removeCallbacksAndMessages(null);
        timerTask.cancel();
        mTimer.cancel();
        super.onDestroy();
    }

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            int currentPositon = mJcVideoPlayer.getCurrentPositionWhenPlaying();
            if (currentPositon == lastPosition) {
                lastPosition = currentPositon;
                return;
            }
            if (mJcVideoPlayer.mCurrentState == JCVideoPlayer.CURRENT_STATE_PLAYING || mJcVideoPlayer.mCurrentState == JCVideoPlayer.CURRENT_STATE_AUTO_COMPLETE) {
                //正在播放的时候需要发送信息
                Message message = new Message();
                mHandler.sendMessage(message);
            }
        }
    };

    private boolean countDownFlag = false;


    private int lastPosition = 0;

    class MyHandler extends Handler {
        WeakReference<Activity> mActivityReference;

        MyHandler(Activity activity) {
            mActivityReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final Activity activity = mActivityReference.get();
            if (activity != null) {
                try {
                    if (App.isVip == 0 && mJcVideoPlayer.getCurrentPositionWhenPlaying() >= mJcVideoPlayer.getDuration() / 50 - 5000) {
                        try {
                            timerTask.cancel();
                            mTimer.cancel();
                            JCMediaManager.instance().mediaPlayer.stop();
                            if (builder != null && dialog != null) {
                                builder.setMessage("非会员只能试看体验，请成为会员继续观看");
                                dialog.show();
                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        //不是会员，试看时长大于30s 弹出看通会员的界面
                                        Intent intent = new Intent();
                                        intent.putExtra(PARAM_CURRENT_TIME, mJcVideoPlayer.getCurrentPositionWhenPlaying());
                                        intent.putExtra(PARAM_DIALOG_TYPE, DIALOG_TYPE_GLOD);
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (App.isVip == 1 && mJcVideoPlayer.getCurrentPositionWhenPlaying() >= mJcVideoPlayer.getDuration() / 50 - 5000) {
                        try {
                            timerTask.cancel();
                            mTimer.cancel();
                            JCMediaManager.instance().mediaPlayer.stop();
                            if (builder != null && dialog != null) {
                                builder.setMessage("请升级钻石会员，观看完整影片");
                                dialog.show();
                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        //黄金会员看完视频 弹出开通砖石会员界面
                                        Intent intent = new Intent();
                                        intent.putExtra(PARAM_CURRENT_TIME, mJcVideoPlayer.getCurrentPositionWhenPlaying());
                                        intent.putExtra(PARAM_DIALOG_TYPE, DIALOG_TYPE_DIAMOND);
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (App.isVip == 2 && mJcVideoPlayer.getCurrentPositionWhenPlaying() > 5 * 1000) {
                        try {
                            timerTask.cancel();
                            mTimer.cancel();
                            JCMediaManager.instance().mediaPlayer.stop();
                            if (builder != null && dialog != null) {
                                builder.setMessage("由于现法律不允许国内播放，请注册VPN翻墙观看，现仅需28元终身免费使用");
                                dialog.show();
                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        //砖石会员看完视频，提示开通VPN海外会员
                                        Intent intent = new Intent();
                                        intent.putExtra(PARAM_CURRENT_TIME, mJcVideoPlayer.getCurrentPositionWhenPlaying());
                                        intent.putExtra(PARAM_DIALOG_TYPE, DIALOG_TYPE_VPN);
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (App.isVip == 3 && mJcVideoPlayer.getCurrentPositionWhenPlaying() > 5 * 1000) {
                        try {
                            timerTask.cancel();
                            mTimer.cancel();
                            JCMediaManager.instance().mediaPlayer.stop();
                            if (builder != null && dialog != null) {
                                builder.setMessage("海外视频提供商需收取3美金服务费。付费后海量视频终身免费观看");
                                dialog.show();
                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        //开通VPN之后播放5s，提示开通海外片库
                                        Intent intent = new Intent();
                                        intent.putExtra(PARAM_CURRENT_TIME, mJcVideoPlayer.getCurrentPositionWhenPlaying());
                                        intent.putExtra(PARAM_DIALOG_TYPE, DIALOG_TYPE_OVERSEA_FLIM);
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (App.isVip == 4 && mJcVideoPlayer.getCurrentPositionWhenPlaying() >= mJcVideoPlayer.getDuration() / 50 - 5000) {
                        try {
                            timerTask.cancel();
                            mTimer.cancel();
                            JCMediaManager.instance().mediaPlayer.stop();
                            if (builder != null && dialog != null) {
                                builder.setMessage("最后一次付费，您将得到您想要的，黑金会员，开放所有影片，你值得拥有！");
                                dialog.show();
                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        //开通VPN之后播放5s，提示开通海外片库
                                        Intent intent = new Intent();
                                        intent.putExtra(PARAM_CURRENT_TIME, mJcVideoPlayer.getCurrentPositionWhenPlaying());
                                        intent.putExtra(PARAM_DIALOG_TYPE, DIALOG_TYPE_OVERSEA_FLIM);
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }
                                });
                            }

                            //开通海外片库看完视频 弹出黑金会员
                            Intent intent = new Intent();
                            intent.putExtra(PARAM_CURRENT_TIME, mJcVideoPlayer.getCurrentPositionWhenPlaying());
                            intent.putExtra(PARAM_DIALOG_TYPE, DIALOG_TYPE_BLACK_GLOD);
                            setResult(RESULT_OK, intent);
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (App.isVip == 5 && mJcVideoPlayer.getCurrentPositionWhenPlaying() > 60 * 1000) {
                        try {
                            //黑金会员播放1分钟后弹出加速通道
                            JCMediaManager.instance().releaseMediaPlayer();
                            mJcVideoPlayer.startButton.setVisibility(View.GONE);
                            mJcVideoPlayer.loadingProgressBar.setVisibility(View.VISIBLE);
                            if (!countDownFlag) {
                                countDownFlag = true;
                                new CountDownTimer(5 * 1000, 5 * 1000) {

                                    public void onTick(long millisUntilFinished) {
                                    }

                                    public void onFinish() {
                                        try {
                                            if (!isFinishing()) {
                                                if (builder != null && dialog != null) {
                                                    builder.setMessage("网络太慢了！由于目前观看用户太多。你的国内线路宽带太低.是否需要切换到海外高速通道？");
                                                    dialog.show();
                                                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                        @Override
                                                        public void onDismiss(DialogInterface dialog) {
                                                            Intent intent = new Intent();
                                                            intent.putExtra(PARAM_CURRENT_TIME, mJcVideoPlayer.getCurrentPositionWhenPlaying());
                                                            intent.putExtra(PARAM_DIALOG_TYPE, DIALOG_TYPE_OVERSEA_SPEED);
                                                            setResult(RESULT_OK, intent);
                                                            finish();
                                                        }
                                                    });
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }.start();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (App.isVip == 6 && mJcVideoPlayer.getCurrentPositionWhenPlaying() > 10 * 1000) {
                        try {
                            //开通海外片库后播放10s 弹出双线通道
                            JCMediaManager.instance().releaseMediaPlayer();
                            mJcVideoPlayer.startButton.setVisibility(View.GONE);
                            mJcVideoPlayer.loadingProgressBar.setVisibility(View.VISIBLE);
                            if (!countDownFlag) {
                                countDownFlag = true;
                                new CountDownTimer(5 * 1000, 5 * 1000) {

                                    public void onTick(long millisUntilFinished) {
                                    }

                                    public void onFinish() {
                                        try {
                                            if (!isFinishing()) {
                                                if (builder != null && dialog != null) {
                                                    builder.setMessage("网络拥堵无法播放 开通海外双线？");
                                                    dialog.show();
                                                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                        @Override
                                                        public void onDismiss(DialogInterface dialog) {
                                                            Intent intent = new Intent();
                                                            intent.putExtra(PARAM_CURRENT_TIME, mJcVideoPlayer.getCurrentPositionWhenPlaying());
                                                            intent.putExtra(PARAM_DIALOG_TYPE, DIALOG_TYPE_OVERSEA_SNAP);
                                                            setResult(RESULT_OK, intent);
                                                            finish();
                                                        }
                                                    });
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }

                                }.start();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (App.isVip == 7 && mJcVideoPlayer.getCurrentPositionWhenPlaying() >= mJcVideoPlayer.getDuration() / 50 - 5000) {
                        try {
                            //开通双线之后播完 缓冲5s提示
                            JCMediaManager.instance().releaseMediaPlayer();
                            mJcVideoPlayer.startButton.setVisibility(View.GONE);
                            mJcVideoPlayer.loadingProgressBar.setVisibility(View.VISIBLE);
                            if (!countDownFlag) {
                                countDownFlag = true;
                                new CountDownTimer(5 * 1000, 5 * 1000) {

                                    public void onTick(long millisUntilFinished) {
                                    }

                                    public void onFinish() {
                                        CustomSubmitDialog customSubmitDialog = DialogUtil.getInstance().showCustomSubmitDialog(JCFullScreenActivity.this, "网络异常请重试");
                                        customSubmitDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialog) {
                                                new CountDownTimer(5 * 1000, 5 * 1000) {

                                                    @Override
                                                    public void onTick(long millisUntilFinished) {

                                                    }

                                                    @Override
                                                    public void onFinish() {
                                                        CustomSubmitDialog customSubmitDialog1 = DialogUtil.getInstance().showCustomSubmitDialog(JCFullScreenActivity.this, "您的网络情况实在太糟。我们已经为您提供最顶级的通道服务，如果仍然无法流畅观看。我们还为您准备了免费的流畅福利您可以先行观看");
                                                        customSubmitDialog1.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                            @Override
                                                            public void onDismiss(DialogInterface dialog) {
                                                                Intent intent = new Intent();
                                                                intent.setAction("android.intent.action.VIEW");
                                                                Uri content_url = Uri.parse("http://m.mm131.com");
                                                                intent.setData(content_url);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        });
                                                    }
                                                }.start();
                                            }
                                        });
                                    }

                                }.start();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private SubmitAndCancelDialog dialog;
    private SubmitAndCancelDialog.Builder builder;

    private void initDialog() {
        builder = new SubmitAndCancelDialog.Builder(JCFullScreenActivity.this);
        builder.setCancelButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        builder.setSubmitButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        dialog = builder.create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
    }


    /**
     * Case By:获取弹幕的数据
     * Author: scene on 2017/4/27 15:35
     */
    private void getDanmuData() {
        danmuRequestCall = OkHttpUtils.get().url(API.URL_PRE + API.DANMU).build();
        danmuRequestCall.execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(String s, int i) {
                try {
                    List<CommentInfo> comemnts = JSON.parseArray(s, CommentInfo.class);
                    commentInfoList = new ArrayList<>();
                    commentInfoList.addAll(comemnts);
                    initDanmuConfig();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


}



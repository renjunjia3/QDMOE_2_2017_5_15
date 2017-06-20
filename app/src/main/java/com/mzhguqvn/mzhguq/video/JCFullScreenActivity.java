package com.mzhguqvn.mzhguq.video;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.fastjson.JSON;
import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.app.App;
import com.mzhguqvn.mzhguq.bean.CommentInfo;
import com.mzhguqvn.mzhguq.bean.VideoCommentResultInfo;
import com.mzhguqvn.mzhguq.bean.VideoInfo;
import com.mzhguqvn.mzhguq.ui.dialog.SubmitAndCancelDialog;
import com.mzhguqvn.mzhguq.util.API;
import com.mzhguqvn.mzhguq.util.ScreenUtils;
import com.mzhguqvn.mzhguq.util.ToastUtils;
import com.mzhguqvn.mzhguq.video.danmu.danmu.DanmuControl;
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
    public static final int DIALOG_TYPE_CDN = 2;

    public static final String PARAM_CURRENT_TIME = "current_time";
    public static final String PARAM_DIALOG_TYPE = "dialog_type";
    public static final String PARAM_VIDEO_INFO = "video_info";

    private static VideoInfo videoInfo;
    private static Timer mTimer;

    private JCVideoPlayerStandard mJcVideoPlayer;
    /**
     * 刚启动全屏时的播放状态
     */
    static int CURRENT_STATE = -1;
    public static String URL;
    static boolean DIRECT_FULLSCREEN = false;
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
        mJcVideoPlayer.mScreenWidth = ScreenUtils.instance(JCFullScreenActivity.this).getScreenWidth();
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
        if (App.role == 0) {
            getDanmuData();
        }

        if (App.role == 0) {
            mJcVideoPlayer.text2.setVisibility(View.GONE);
            mJcVideoPlayer.text3.setVisibility(View.GONE);
            mJcVideoPlayer.openVip.setText("开通VIP会员");
        }
//        else if ((App.role == 1 || App.role == 2) && App.cdn == 0) {
//            mJcVideoPlayer.text2.setVisibility(View.GONE);
//            mJcVideoPlayer.text3.setVisibility(View.GONE);
//            mJcVideoPlayer.openVip.setText("开通CDN播放更流畅");
//        }
        else if (App.role == 1 || App.role == 2) {
            mJcVideoPlayer.text2.setVisibility(View.GONE);
            mJcVideoPlayer.text3.setVisibility(View.GONE);
            mJcVideoPlayer.openVip.setText("升级钻石会员观看更多内容");
        } else {
            mJcVideoPlayer.text2.setVisibility(View.GONE);
            mJcVideoPlayer.text3.setVisibility(View.GONE);
            mJcVideoPlayer.openVip.setVisibility(View.GONE);
        }

        mJcVideoPlayer.openVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(PARAM_CURRENT_TIME, mJcVideoPlayer.getCurrentPositionWhenPlaying());
                switch (App.role) {
                    case 0:
                        intent.putExtra(PARAM_DIALOG_TYPE, DIALOG_TYPE_GLOD);
                        break;
                    case 1:
                    case 2:
//                        if (App.cdn == 0) {
//                            intent.putExtra(PARAM_DIALOG_TYPE, DIALOG_TYPE_CDN);
//                        } else {
                        intent.putExtra(PARAM_DIALOG_TYPE, DIALOG_TYPE_DIAMOND);
//                        }
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
        params.put("user_id", App.user_id + "");
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
                    if (App.role == 0 && mJcVideoPlayer.getCurrentPositionWhenPlaying() >= JCMediaManager.instance().mediaPlayer.getDuration() - 1000) {
                        timerTask.cancel();
                        mTimer.cancel();
                        JCMediaManager.instance().mediaPlayer.stop();
                        if (builder != null && dialog != null) {
                            builder.setMessage(getString(R.string.try_see_play_open_vip_notice));
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
                    }
//                    else if ((App.role == 1 || App.role == 2) && App.cdn == 0 && mJcVideoPlayer.getCurrentPositionWhenPlaying() >= JCMediaManager.instance().mediaPlayer.getDuration() / 2) {
//                        timerTask.cancel();
//                        mTimer.cancel();
//                        JCMediaManager.instance().mediaPlayer.stop();
//                        if (builder != null && dialog != null) {
//                            builder.setMessage("由于服务器压力太大，请开通CDN服务");
//                            dialog.show();
//                            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                                @Override
//                                public void onDismiss(DialogInterface dialog) {
//                                    //砖石会员看完视频，提示开通VPN海外会员
//                                    Intent intent = new Intent();
//                                    intent.putExtra(PARAM_CURRENT_TIME, mJcVideoPlayer.getCurrentPositionWhenPlaying());
//                                    intent.putExtra(PARAM_DIALOG_TYPE, DIALOG_TYPE_CDN);
//                                    setResult(RESULT_OK, intent);
//                                    finish();
//                                }
//                            });
//                        }
//                    }
                    else if ((App.role == 1 || App.role == 2) && mJcVideoPlayer.getCurrentPositionWhenPlaying() >= JCMediaManager.instance().mediaPlayer.getDuration() - 1000) {
                        timerTask.cancel();
                        mTimer.cancel();
                        JCMediaManager.instance().mediaPlayer.stop();
                        if (builder != null && dialog != null) {
                            builder.setMessage(getString(R.string.glod_play_open_vip_notice));
                            dialog.show();
                            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    //砖石会员看完视频，提示开通VPN海外会员
                                    Intent intent = new Intent();
                                    intent.putExtra(PARAM_CURRENT_TIME, mJcVideoPlayer.getCurrentPositionWhenPlaying());
                                    intent.putExtra(PARAM_DIALOG_TYPE, DIALOG_TYPE_DIAMOND);
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
        HashMap<String, String> params = API.createParams();
        params.put("video_id", videoInfo.getVideo_id() + "");
        danmuRequestCall = OkHttpUtils.get().url(API.URL_PRE + API.DANMU).params(params).build();
        danmuRequestCall.execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(String s, int i) {
                try {
                    VideoCommentResultInfo resultInfo = JSON.parseObject(s, VideoCommentResultInfo.class);
                    List<CommentInfo> comemnts = resultInfo.getData();
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



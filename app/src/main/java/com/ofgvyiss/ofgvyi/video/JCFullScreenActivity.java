package com.ofgvyiss.ofgvyi.video;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.ofgvyiss.ofgvyi.R;
import com.ofgvyiss.ofgvyi.app.App;
import com.ofgvyiss.ofgvyi.bean.CommentInfo;
import com.ofgvyiss.ofgvyi.bean.VideoInfo;
import com.ofgvyiss.ofgvyi.ui.dialog.CustomSubmitDialog;
import com.ofgvyiss.ofgvyi.ui.dialog.SubmitAndCancelDialog;
import com.ofgvyiss.ofgvyi.util.AuthImageDownloader;
import com.ofgvyiss.ofgvyi.util.DialogUtil;
import com.ofgvyiss.ofgvyi.util.SharedPreferencesUtil;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.Duration;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.AndroidDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.model.android.ViewCacheStuffer;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;
import master.flame.danmaku.ui.widget.DanmakuView;

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
    private BaseDanmakuParser mParser;
    private DanmakuContext mContext;
    private DanmakuView mDanmakuView;
    private int mIconWidth;
    private List<CommentInfo> commentInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .imageDownloader(new AuthImageDownloader(this))
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .memoryCacheSizePercentage(13).build(); // default

        ImageLoader.getInstance().init(config);
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
        if (mJcVideoPlayer.mIfFullscreenIsDirectly) {
            mJcVideoPlayer.startButton.performClick();
        } else {
            JCVideoPlayer.IF_RELEASE_WHEN_ON_PAUSE = true;
            JCMediaManager.instance().listener = mJcVideoPlayer;
            if (CURRENT_STATE == JCVideoPlayer.CURRENT_STATE_PAUSE) {
                JCMediaManager.instance().mediaPlayer.seekTo(JCMediaManager.instance().mediaPlayer.getCurrentPosition());
            }
        }
        mTimer = new Timer();
        mTimer.schedule(timerTask, 50, 50);
        initDanmuConfig();

        if (App.isVip == 0) {
            mJcVideoPlayer.text2.setVisibility(View.VISIBLE);
            mJcVideoPlayer.text3.setVisibility(View.VISIBLE);
            mJcVideoPlayer.text2.setText("开通会员观看完整视频");
            mJcVideoPlayer.text3.setText("开通会员观看完整视频");
        } else if (App.isVip == 1) {
            mJcVideoPlayer.text2.setVisibility(View.VISIBLE);
            mJcVideoPlayer.text3.setVisibility(View.VISIBLE);
            mJcVideoPlayer.text2.setText("升级钻石观看超长视频");
            mJcVideoPlayer.text3.setText("升级钻石观看超长视频");
        } else if (App.isVip == 2) {
            mJcVideoPlayer.text2.setVisibility(View.GONE);
            mJcVideoPlayer.text3.setVisibility(View.GONE);
        } else if (App.isVip == 3) {
            mJcVideoPlayer.text2.setVisibility(View.VISIBLE);
            mJcVideoPlayer.text3.setVisibility(View.VISIBLE);
            mJcVideoPlayer.text2.setText("升级黑金看你所想");
            mJcVideoPlayer.text3.setText("升级黑金看你所想");
        } else {
            mJcVideoPlayer.text2.setVisibility(View.GONE);
            mJcVideoPlayer.text3.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化配置
     */
    private void initDanmuConfig() {
        mDanmakuView = mJcVideoPlayer.getDanmuView();
        // 设置最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<Integer, Integer>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 5); // 滚动弹幕最大显示5行
        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<Integer, Boolean>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);
        mContext = DanmakuContext.create();
        mIconWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60f, getResources().getDisplayMetrics());
        mContext.setDanmakuBold(true);
        mContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3).setDuplicateMergingEnabled(false).setScrollSpeedFactor(1.2f).setScaleTextSize(1.2f)
                .setCacheStuffer(new ViewCacheStuffer<MyViewHolder>() {

                    @Override
                    public MyViewHolder onCreateViewHolder(int viewType) {
                        return new MyViewHolder(View.inflate(getApplicationContext(), R.layout.layout_view_cache, null));
                    }

                    @Override
                    public void onBindViewHolder(int viewType, MyViewHolder viewHolder, BaseDanmaku danmaku, AndroidDisplayer.DisplayerConfig displayerConfig, TextPaint paint) {
                        if (paint != null)
                            viewHolder.mText.getPaint().set(paint);
                        viewHolder.mText.setText(danmaku.text);
                        viewHolder.mText.setTextColor(danmaku.textColor);
                        Bitmap bitmap = null;
                        MyImageWare imageWare = (MyImageWare) danmaku.tag;
                        if (imageWare != null) {
                            bitmap = imageWare.bitmap;
                        }
                        if (bitmap != null) {
                            viewHolder.mIcon.setImageBitmap(bitmap);
                        } else {
                            viewHolder.mIcon.setImageResource(R.drawable.ic_user_avatar);
                        }
                    }

                    @Override
                    public void releaseResource(BaseDanmaku danmaku) {
                        MyImageWare imageWare = (MyImageWare) danmaku.tag;
                        if (imageWare != null) {
                            ImageLoader.getInstance().cancelDisplayTask(imageWare);
                        }
                        danmaku.setTag(null);
                    }


                    @Override
                    public void prepare(BaseDanmaku danmaku, boolean fromWorkerThread) {
                        if (danmaku.isTimeOut()) {
                            return;
                        }
                        MyImageWare imageWare = (MyImageWare) danmaku.tag;
                        if (imageWare == null) {
                            String avatar = "";
                            imageWare = new MyImageWare(avatar, danmaku, mIconWidth, mIconWidth, mDanmakuView);
                            danmaku.setTag(imageWare);
                        }
                        ImageLoader.getInstance().displayImage(imageWare.getImageUri(), imageWare);
                    }

                }, null)
                .setMaximumLines(maxLinesPair)
                .preventOverlapping(overlappingEnablePair);

        if (mDanmakuView != null) {
            mParser = createParser(this.getResources().openRawResource(R.raw.comments));
            mDanmakuView.setCallback(new master.flame.danmaku.controller.DrawHandler.Callback() {
                @Override
                public void updateTimer(DanmakuTimer timer) {
                }

                @Override
                public void drawingFinished() {

                }

                @Override
                public void danmakuShown(BaseDanmaku danmaku) {
                }

                @Override
                public void prepared() {
                    mDanmakuView.start();
                }
            });
            mDanmakuView.prepare(mParser, mContext);
            mDanmakuView.showFPS(false);
            mDanmakuView.enableDanmakuDrawingCache(true);
        }

        new CountDownTimer(commentInfoList.size() * 3 * 1000, 2 * 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                if (count < commentInfoList.size()) {
                    addDanmaKuShowTextAndImage(commentInfoList.get(count));
                    count++;
                }
            }

            @Override
            public void onFinish() {

            }
        }.start();


        mJcVideoPlayer.closeDanmu.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (mDanmakuView != null) {
                    if (mDanmakuView.isShown()) {
                        mDanmakuView.hide();
                        mJcVideoPlayer.closeDanmu.setImageResource(R.drawable.ic_show_danmu);
                    } else {
                        mDanmakuView.show();
                        mJcVideoPlayer.closeDanmu.setImageResource(R.drawable.ic_close_danmu);
                    }
                }
            }
        });
    }

    private int count = 0;

    private void addDanmaKuShowTextAndImage(CommentInfo commentInfo) {
        BaseDanmaku danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        if (danmaku == null || mDanmakuView == null) {
            return;
        }
        danmaku.text = commentInfo.getText();
        danmaku.setTag(new MyImageWare(commentInfo.getAvatar(), danmaku, mIconWidth, mIconWidth, mDanmakuView));
        danmaku.padding = 5;
        danmaku.priority = 0;  // 一定会显示, 一般用于本机发送的弹幕
        danmaku.isLive = false;
        danmaku.setTime(mDanmakuView.getCurrentTime());
        danmaku.textSize = 20f * (mParser.getDisplayer().getDensity() - 0.6f);
        danmaku.textColor = Color.WHITE;
        danmaku.setDuration(new Duration(10000));
        danmaku.textShadowColor = 0; // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
        mDanmakuView.addDanmaku(danmaku);
    }

    private BaseDanmakuParser createParser(InputStream stream) {

        if (stream == null) {
            return new BaseDanmakuParser() {

                @Override
                protected Danmakus parse() {
                    return new Danmakus();
                }
            };
        }

        ILoader loader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);

        try {
            loader.load(stream);
        } catch (IllegalDataException e) {
            e.printStackTrace();
        }
        BaseDanmakuParser parser = new BiliDanmukuParser();
        IDataSource<?> dataSource = loader.getDataSource();
        parser.load(dataSource);
        return parser;

    }

    private void initData() {
        Intent intent = getIntent();
        videoInfo = (VideoInfo) intent.getSerializableExtra(PARAM_VIDEO_INFO);
        CURRENT_STATE = JCVideoPlayer.CURRENT_STATE_NORMAL;
        URL = videoInfo.getUrl();
        DIRECT_FULLSCREEN = true;
        VIDEO_PLAYER_CLASS = JCVideoPlayerStandard.class;
        try {
            commentInfoList = JSON.parseArray(SharedPreferencesUtil.getString(JCFullScreenActivity.this, PARAM_STR_COMMENT, "[]"), CommentInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
            if (commentInfoList == null) {
                commentInfoList = new ArrayList<>();
            }
        }
    }


    @Override
    public void onBackPressed() {
        if (mDanmakuView != null) {
            mDanmakuView.release();
            mDanmakuView = null;
        }
        JCMediaManager.instance().mediaPlayer.stop();
        finish();
    }


    @Override
    protected void onPause() {
        if (mDanmakuView != null && mDanmakuView.isPrepared()) {
            mDanmakuView.pause();
        }
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDanmakuView != null && mDanmakuView.isPrepared() && mDanmakuView.isPaused()) {
            mDanmakuView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        if (mDanmakuView != null) {
            mDanmakuView.release();
            mDanmakuView = null;
        }
        ImageLoader.getInstance().destroy();
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
                if (App.isVip == 0 && mJcVideoPlayer.getCurrentPositionWhenPlaying() >= mJcVideoPlayer.getDuration() / 50 - 5) {
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

                } else if (App.isVip == 1 && mJcVideoPlayer.getCurrentPositionWhenPlaying() >= mJcVideoPlayer.getDuration() / 50 - 5) {
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

                } else if (App.isVip == 2 && mJcVideoPlayer.getCurrentPositionWhenPlaying() > 5 * 1000) {
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

                } else if (App.isVip == 3 && mJcVideoPlayer.getCurrentPositionWhenPlaying() > 5 * 1000) {
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

                } else if (App.isVip == 4 && mJcVideoPlayer.getCurrentPositionWhenPlaying() >= mJcVideoPlayer.getDuration() / 50 - 5) {
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
                } else if (App.isVip == 5 && mJcVideoPlayer.getCurrentPositionWhenPlaying() > 60 * 1000) {
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

                        }.start();
                    }

                } else if (App.isVip == 6 && mJcVideoPlayer.getCurrentPositionWhenPlaying() > 10 * 1000) {
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

                        }.start();
                    }
                } else if (App.isVip == 7 && mJcVideoPlayer.getCurrentPositionWhenPlaying() >= mJcVideoPlayer.getDuration() / 50 - 5) {
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
}



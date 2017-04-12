package fm.jiecao.jcvideoplayer_lib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.util.Timer;
import java.util.TimerTask;

/**
 * <p>全屏的activity</p>
 * <p>fullscreen activity</p>
 * Created by Nathen
 * On 2015/12/01 11:17
 */
public class JCFullScreenActivity extends Activity {

    private final Handler mHandler = new MyHandler(this);
    public static final int DIALOG_TYPE_FULLVIDEO = 0;
    public static final int DIALOG_TYPE_FUNCATION = 1;
    public static final int DIALOG_TYPE_SPEED = 2;

    public static final String PARAM_CURRENT_TIME = "current_time";
    public static final String PARAM_DIALOG_TYPE = "dialog_type";
    public static final String PARAM_VIDEO_INFO = "video_info";
    public static final String PARAM_TRY_COUNT = "try_count";
    public static final String PARAM_IS_VIP = "is_vip";
    public static final String PARAM_IS_SPEED = "is_speed";


    private static VideoInfo videoInfo;
    private static long currentTime = 0;
    private static Timer mTimer;
    private static int isVIP = 0;
    private static int tryCount = 0;
    private static int isSpeed = 0;


    private JCVideoPlayerStandard mJcVideoPlayer;
    /**
     * 刚启动全屏时的播放状态
     */
    static int CURRENT_STATE = -1;
    public static String URL;
    static boolean DIRECT_FULLSCREEN = false;//this is should be in videoplayer
    static Class VIDEO_PLAYER_CLASS;

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
        try {
            Constructor<JCVideoPlayerStandard> constructor = VIDEO_PLAYER_CLASS.getConstructor(Context.class);
            mJcVideoPlayer = constructor.newInstance(this);
            setContentView(mJcVideoPlayer);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
    }


    private void initData() {
        Intent intent = getIntent();
        videoInfo = (VideoInfo) intent.getSerializableExtra(PARAM_VIDEO_INFO);
        currentTime = intent.getLongExtra(PARAM_CURRENT_TIME, 0);
        isVIP = intent.getIntExtra(PARAM_IS_VIP, 0);
        tryCount = intent.getIntExtra(PARAM_TRY_COUNT, 0);
        isSpeed = intent.getIntExtra(PARAM_IS_SPEED, 0);

        CURRENT_STATE = JCVideoPlayer.CURRENT_STATE_NORMAL;
        URL = videoInfo.getUrl();
        DIRECT_FULLSCREEN = true;
        VIDEO_PLAYER_CLASS = JCVideoPlayerStandard.class;
    }


    @Override
    public void onBackPressed() {
        mJcVideoPlayer.backFullscreen();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if (mJcVideoPlayer.mCurrentState == JCVideoPlayer.CURRENT_STATE_PLAYING) {
                //正在播放的时候需要发送信息
                Message message = new Message();
                mHandler.sendMessage(message);
            }
        }
    };

    boolean countDownFlag = false;

    class MyHandler extends Handler {
        WeakReference<Activity> mActivityReference;

        MyHandler(Activity activity) {
            mActivityReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final Activity activity = mActivityReference.get();
            if (activity != null) {
                if (videoInfo.getVip() == 1 && isVIP == 0) {
                    //Vip视频
                    Intent intent = new Intent();
                    intent.putExtra(PARAM_CURRENT_TIME, mJcVideoPlayer.getCurrentPositionWhenPlaying());
                    intent.putExtra(PARAM_DIALOG_TYPE, DIALOG_TYPE_FULLVIDEO);
                    setResult(RESULT_OK, intent);
                    finish();
                } else if (isVIP == 0 && videoInfo.getVip() == 0 && tryCount <= 4 && mJcVideoPlayer.getCurrentPositionWhenPlaying() > 30000) {
                    //可以试看的视频 试看次数不够了
                    Intent intent = new Intent();
                    intent.putExtra(PARAM_CURRENT_TIME, mJcVideoPlayer.getCurrentPositionWhenPlaying());
                    intent.putExtra(PARAM_DIALOG_TYPE, DIALOG_TYPE_FULLVIDEO);
                    setResult(RESULT_OK, intent);
                    finish();
                } else if (isVIP == 0 && videoInfo.getVip() == 0 && tryCount > 4) {
                    //可以试看的视频 试看次数不够了
                    Intent intent = new Intent();
                    intent.putExtra(PARAM_CURRENT_TIME, mJcVideoPlayer.getCurrentPositionWhenPlaying());
                    intent.putExtra(PARAM_DIALOG_TYPE, DIALOG_TYPE_FULLVIDEO);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    if (isSpeed == 0 && mJcVideoPlayer.getCurrentPositionWhenPlaying() > 40000) {//需要提示加速
                        mJcVideoPlayer.loadingProgressBar.setVisibility(View.VISIBLE);
                        JCMediaManager.instance().releaseMediaPlayer();
                        if (!countDownFlag) {
                            countDownFlag = true;
                            new CountDownTimer(8000, 8000) {

                                public void onTick(long millisUntilFinished) {

                                }

                                public void onFinish() {
                                    Intent intent = new Intent();
                                    intent.putExtra(PARAM_CURRENT_TIME, (long) mJcVideoPlayer.getCurrentPositionWhenPlaying());
                                    intent.putExtra(PARAM_DIALOG_TYPE, DIALOG_TYPE_SPEED);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }

                            }.start();
                        }
                    }
                }
            }
        }
    }
}



package com.cl.cltv.video;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cl.cltv.R;
import com.cl.cltv.app.App;
import com.cl.cltv.util.ToastUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Manage MediaPlayer
 * Created by Nathen
 * On 2016/04/10 15:45
 */
public abstract class JCVideoPlayer extends FrameLayout implements View.OnClickListener, View.OnTouchListener, SeekBar.OnSeekBarChangeListener, JCMediaManager.JCMediaPlayerListener, TextureView.SurfaceTextureListener {

    public static final String TAG = "JieCaoVideoPlayer";

    protected int mCurrentState = -1;//-1相当于null
    protected static final int CURRENT_STATE_NORMAL = 0;
    protected static final int CURRENT_STATE_PREPAREING = 1;
    protected static final int CURRENT_STATE_PLAYING = 2;
    protected static final int CURRENT_STATE_PLAYING_BUFFERING_START = 3;
    protected static final int CURRENT_STATE_PAUSE = 5;
    protected static final int CURRENT_STATE_AUTO_COMPLETE = 6;
    protected static final int CURRENT_STATE_ERROR = 7;
    protected static int BACKUP_PLAYING_BUFFERING_STATE = 0;

    protected boolean mTouchingProgressBar = false;
    protected boolean mIfCurrentIsFullscreen = false;
    protected boolean mIfFullscreenIsDirectly = false;//mIfCurrentIsFullscreen should be true first
    protected static boolean IF_FULLSCREEN_FROM_NORMAL = false;//to prevent infinite looping
    public static boolean IF_RELEASE_WHEN_ON_PAUSE = true;
    protected static long CLICK_QUIT_FULLSCREEN_TIME = 0;
    public static final int FULL_SCREEN_NORMAL_DELAY = 2000;

    public static boolean ACTION_BAR_EXIST = false;


    public ImageView startButton;
    public SeekBar progressBar;
    public ImageView fullscreenButton;
    public TextView currentTimeTextView, totalTimeTextView;
    public ViewGroup textureViewContainer;
    public ViewGroup topContainer, bottomContainer;
    public JCResizeTextureView textureView;
    public Surface mSurface;

    protected String mUrl;
    protected Object[] mObjects;
    protected Map<String, String> mMapHeadData = new HashMap<>();
    protected boolean mLooping = false;

    protected static Timer UPDATE_PROGRESS_TIMER;
    protected static JCBuriedPoint JC_BURIED_POINT;
    protected int mScreenWidth;
    protected int mScreenHeight;
    protected AudioManager mAudioManager;

    protected boolean mChangeVolume = false;
    protected boolean mChangePosition = false;
    protected int mDownPosition;
    protected int mDownVolume;

    protected Dialog mProgressDialog;
    protected ProgressBar mDialogProgressBar;
    protected TextView mDialogCurrentTime;
    protected TextView mDialogTotalTime;
    protected ImageView mDialogIcon;
    protected int mResultTimePosition;//change postion when finger up

    protected Dialog mVolumeDialog;
    protected ProgressBar mDialogVolumeProgressBar;

    public static boolean WIFI_TIP_DIALOG_SHOWED = false;

    public JCVideoPlayer(Context context) {
        super(context);
        init(context);
    }

    public JCVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    protected void init(Context context) {
        View.inflate(context, getLayoutId(), this);
        startButton = (ImageView) findViewById(R.id.start);
        fullscreenButton = (ImageView) findViewById(R.id.fullscreen);
        progressBar = (SeekBar) findViewById(R.id.progress);
        currentTimeTextView = (TextView) findViewById(R.id.current);
        totalTimeTextView = (TextView) findViewById(R.id.total);
        bottomContainer = (ViewGroup) findViewById(R.id.layout_bottom);
        textureViewContainer = (RelativeLayout) findViewById(R.id.surface_container);
        topContainer = (ViewGroup) findViewById(R.id.layout_top);
        topContainer.setVisibility(ACTION_BAR_EXIST ? GONE : VISIBLE);

        startButton.setOnClickListener(this);
        fullscreenButton.setOnClickListener(this);
        progressBar.setOnSeekBarChangeListener(this);
        bottomContainer.setOnClickListener(this);
        textureViewContainer.setOnClickListener(this);
        progressBar.setOnTouchListener(this);

        textureViewContainer.setOnTouchListener(this);
        mScreenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
    }

    public abstract int getLayoutId();

    protected static void setJcBuriedPoint(JCBuriedPoint jcBuriedPoint) {
        JC_BURIED_POINT = jcBuriedPoint;
    }

    public boolean setUp(String url, Object... objects) {
        if (JCMediaManager.instance().listener == this &&
                (System.currentTimeMillis() - CLICK_QUIT_FULLSCREEN_TIME) < FULL_SCREEN_NORMAL_DELAY)
            return false;
        mCurrentState = CURRENT_STATE_NORMAL;
        this.mUrl = url;
        this.mObjects = objects;
        setStateAndUi(CURRENT_STATE_NORMAL);
        return true;
    }

    public boolean setUp(String url, Map<String, String> mapHeadData, Object... objects) {
        if (setUp(url, objects)) {
            this.mMapHeadData.clear();
            this.mMapHeadData.putAll(mapHeadData);
            return true;
        }
        return false;
    }

    public void setLoop(boolean looping) {
        this.mLooping = looping;
    }

    //set ui
    protected void setStateAndUi(int state) {
        mCurrentState = state;
        switch (mCurrentState) {
            case CURRENT_STATE_NORMAL:
                if (JCMediaManager.instance().listener == this) {
                    cancelProgressTimer();
                    JCMediaManager.instance().releaseMediaPlayer();
                }
                break;
            case CURRENT_STATE_PREPAREING:
                resetProgressAndTime();
                break;
            case CURRENT_STATE_PLAYING:
                startProgressTimer();
                break;
            case CURRENT_STATE_PAUSE:
                startProgressTimer();
                break;
            case CURRENT_STATE_ERROR:
                if (JCMediaManager.instance().listener == this) {
                    onCompletion();
                    JCMediaManager.instance().releaseMediaPlayer();
                }
                break;
            case CURRENT_STATE_AUTO_COMPLETE:
//        if (JCMediaManager.instance().listener == this) {
//          JCMediaManager.instance().releaseMediaPlayer();
                cancelProgressTimer();
                progressBar.setProgress(3);
                currentTimeTextView.setText(totalTimeTextView.getText());
//        }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.start) {
            if (TextUtils.isEmpty(mUrl)) {
                Toast.makeText(getContext(), getResources().getString(R.string.no_url), Toast.LENGTH_SHORT).show();
                return;
            }
            if (mCurrentState == CURRENT_STATE_NORMAL || mCurrentState == CURRENT_STATE_ERROR) {
                if (!JCUtils.isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(getResources().getString(R.string.tips_not_wifi));
                    builder.setPositiveButton(getResources().getString(R.string.tips_not_wifi_confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startButtonLogic();
                            WIFI_TIP_DIALOG_SHOWED = true;
                        }
                    });
                    builder.setNegativeButton(getResources().getString(R.string.tips_not_wifi_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                    return;
                }
                startButtonLogic();
            } else if (mCurrentState == CURRENT_STATE_PLAYING) {
                Log.d(TAG, "pauseVideo [" + this.hashCode() + "] ");
                try {
                    JCMediaManager.instance().mediaPlayer.pause();
                    setStateAndUi(CURRENT_STATE_PAUSE);
                    if (JC_BURIED_POINT != null && JCMediaManager.instance().listener == this) {
                        if (mIfCurrentIsFullscreen) {
                            JC_BURIED_POINT.onClickStopFullscreen(mUrl, mObjects);
                        } else {
                            JC_BURIED_POINT.onClickStop(mUrl, mObjects);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (mCurrentState == CURRENT_STATE_PAUSE) {
                if (JC_BURIED_POINT != null && JCMediaManager.instance().listener == this) {
                    if (mIfCurrentIsFullscreen) {
                        JC_BURIED_POINT.onClickResumeFullscreen(mUrl, mObjects);
                    } else {
                        JC_BURIED_POINT.onClickResume(mUrl, mObjects);
                    }
                }
                JCMediaManager.instance().mediaPlayer.start();
                setStateAndUi(CURRENT_STATE_PLAYING);
            } else if (mCurrentState == CURRENT_STATE_AUTO_COMPLETE) {
                startButtonLogic();
            }
        } else if (i == R.id.fullscreen) {
            if (mCurrentState == CURRENT_STATE_AUTO_COMPLETE) return;
            if (mIfCurrentIsFullscreen) {
                //quit fullscreen
                JCMediaManager.instance().mediaPlayer.stop();
                if (getContext() instanceof JCFullScreenActivity) {
                    Log.i(TAG, "finishFullscreenActivity [" + this.hashCode() + "] ");
                    ((JCFullScreenActivity) getContext()).finish();
                }
            } else {
                Log.d(TAG, "toFullscreenActivity [" + this.hashCode() + "] ");
                if (JC_BURIED_POINT != null && JCMediaManager.instance().listener == this) {
                    JC_BURIED_POINT.onEnterFullscreen(mUrl, mObjects);
                }
                //to fullscreen
                JCMediaManager.instance().setDisplay(null);
                JCMediaManager.instance().lastListener = this;
                JCMediaManager.instance().listener = null;
                IF_FULLSCREEN_FROM_NORMAL = true;
                IF_RELEASE_WHEN_ON_PAUSE = false;
                //JCFullScreenActivity.startActivityFromNormal(getContext(), mCurrentState, mUrl, JCVideoPlayer.this.getClass(), this.mObjects);
            }
        } else if (i == R.id.surface_container && mCurrentState == CURRENT_STATE_ERROR) {
            if (JC_BURIED_POINT != null) {
                JC_BURIED_POINT.onClickStartError(mUrl, mObjects);
            }
            prepareVideo();
        }
    }

    private void startButtonLogic() {
        if (JC_BURIED_POINT != null && mCurrentState == CURRENT_STATE_NORMAL) {
            JC_BURIED_POINT.onClickStartIcon(mUrl, mObjects);
        } else if (JC_BURIED_POINT != null) {
            JC_BURIED_POINT.onClickStartError(mUrl, mObjects);
        }
        prepareVideo();
    }

    protected void prepareVideo() {
        Log.d(TAG, "prepareVideo [" + this.hashCode() + "] ");
        if (JCMediaManager.instance().listener != null) {
            JCMediaManager.instance().listener.onCompletion();
        }
        JCMediaManager.instance().listener = this;
        addTextureView();
        AudioManager mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

        ((Activity) getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        JCMediaManager.instance().prepare(mUrl, mMapHeadData, mLooping);
        setStateAndUi(CURRENT_STATE_PREPAREING);
    }

    private static AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    releaseAllVideos();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    if (JCMediaManager.instance().mediaPlayer.isPlaying()) {
                        JCMediaManager.instance().mediaPlayer.pause();
                    }
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    break;
            }
        }
    };

    protected void addTextureView() {
        Log.i(TAG, "addTextureView [" + this.hashCode() + "] ");
        if (textureViewContainer.getChildCount() > 0) {
            textureViewContainer.removeAllViews();
        }
        textureView = null;
        textureView = new JCResizeTextureView(getContext());
        textureView.setSurfaceTextureListener(this);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        textureViewContainer.addView(textureView, layoutParams);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mSurface = new Surface(surface);
        JCMediaManager.instance().setDisplay(mSurface);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        surface.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //return true;
        float x = event.getX();
        float y = event.getY();
        int id = v.getId();
//        if (id == R.id.surface_container) {
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    mTouchingProgressBar = true;
//
//                    mDownX = x;
//                    mDownY = y;
//                    mChangeVolume = false;
//                    mChangePosition = false;
//                    /////////////////////
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    float deltaX = x - mDownX;
//                    float deltaY = y - mDownY;
//                    float absDeltaX = Math.abs(deltaX);
//                    float absDeltaY = Math.abs(deltaY);
//                    if (mIfCurrentIsFullscreen) {
//                        if (!mChangePosition && !mChangeVolume) {
//                            if (absDeltaX > mThreshold || absDeltaY > mThreshold) {
//                                cancelProgressTimer();
//                                if (absDeltaX >= mThreshold) {
//                                    mChangePosition = true;
//                                    mDownPosition = getCurrentPositionWhenPlaying();
//                                    if (JC_BURIED_POINT != null && JCMediaManager.instance().listener == this) {
//                                        JC_BURIED_POINT.onTouchScreenSeekPosition(mUrl, mObjects);
//                                    }
//                                } else {
//                                    mChangeVolume = true;
//                                    mDownVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//                                    if (JC_BURIED_POINT != null && JCMediaManager.instance().listener == this) {
//                                        JC_BURIED_POINT.onTouchScreenSeekVolume(mUrl, mObjects);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    if (mChangePosition) {
//                        showProgressDialog(deltaX);
//                    }
//                    if (mChangeVolume) {
//                        showVolumDialog(-deltaY);
//                    }
//
//                    break;
//                case MotionEvent.ACTION_UP:
//                    mTouchingProgressBar = false;
//                    if (mProgressDialog != null) {
//                        mProgressDialog.dismiss();
//                    }
//                    if (mVolumeDialog != null) {
//                        mVolumeDialog.dismiss();
//                    }
//                    if (mChangePosition) {
//                        JCMediaManager.instance().mediaPlayer.seekTo(mResultTimePosition);
//                        int duration = getDuration();
//                        int progress = mResultTimePosition * 100 / (duration == 0 ? 1 : duration);
//                        progressBar.setProgress(progress);
//                    }
//                    /////////////////////
//                    startProgressTimer();
//                    if (JC_BURIED_POINT != null && JCMediaManager.instance().listener == this) {
//                        if (mIfCurrentIsFullscreen) {
//                            JC_BURIED_POINT.onClickSeekbarFullscreen(mUrl, mObjects);
//                        } else {
//                            JC_BURIED_POINT.onClickSeekbar(mUrl, mObjects);
//                        }
//                    }
//                    break;
//            }
//        } else
        if (id == R.id.progress) {//if I am seeking bar,no mater whoever can not intercept my event
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //cancelProgressTimer();
//                    ViewParent vpdown = getParent();
//                    while (vpdown != null) {
//                        vpdown.requestDisallowInterceptTouchEvent(true);
//                        vpdown = vpdown.getParent();
//                    }
                    break;
                case MotionEvent.ACTION_UP:
//                    startProgressTimer();
//                    ViewParent vpup = getParent();
//                    while (vpup != null) {
//                        vpup.requestDisallowInterceptTouchEvent(false);
//                        vpup = vpup.getParent();
//                    }
                    if (App.role == 0) {
                        ToastUtils.getInstance(getContext()).showToast("请开通会员");
                    } else if (App.role == 1 || App.role == 2) {
                        ToastUtils.getInstance(getContext()).showToast("请升级钻石会员");
                    }
                    break;
            }
        }

        return false;
    }

    protected void showProgressDialog(float deltaX) {
        if (mProgressDialog == null) {
            View localView = LayoutInflater.from(getContext()).inflate(R.layout.jc_progress_dialog, null);
            mDialogProgressBar = ((ProgressBar) localView.findViewById(R.id.duration_progressbar));
            mDialogCurrentTime = ((TextView) localView.findViewById(R.id.tv_current));
            mDialogTotalTime = ((TextView) localView.findViewById(R.id.tv_duration));
            mDialogIcon = ((ImageView) localView.findViewById(R.id.duration_image_tip));
            mProgressDialog = new Dialog(getContext(), R.style.jc_style_dialog_progress);
            mProgressDialog.setContentView(localView);
            mProgressDialog.getWindow().addFlags(Window.FEATURE_ACTION_BAR);
            mProgressDialog.getWindow().addFlags(32);
            mProgressDialog.getWindow().addFlags(16);
            mProgressDialog.getWindow().setLayout(-2, -2);
            WindowManager.LayoutParams localLayoutParams = mProgressDialog.getWindow().getAttributes();
            localLayoutParams.gravity = 49;
            localLayoutParams.y = getResources().getDimensionPixelOffset(R.dimen.jc_progress_dialog_margin_top);
            mProgressDialog.getWindow().setAttributes(localLayoutParams);
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
        int totalTime = getDuration();
        mResultTimePosition = (int) (mDownPosition + deltaX * totalTime / mScreenWidth);
        if (mResultTimePosition > totalTime) mResultTimePosition = totalTime;
        mDialogCurrentTime.setText(JCUtils.stringForTime(mResultTimePosition));
        mDialogTotalTime.setText(" / " + JCUtils.stringForTime(totalTime) + "");
        mDialogProgressBar.setProgress(mResultTimePosition * 100 / (totalTime == 0 ? 1 : totalTime));
        if (deltaX > 0) {
            mDialogIcon.setBackgroundResource(R.drawable.jc_forward_icon);
        } else {
            mDialogIcon.setBackgroundResource(R.drawable.jc_backward_icon);
        }
    }

    protected void showVolumDialog(float deltaY) {
        if (mVolumeDialog == null) {
            View localView = LayoutInflater.from(getContext()).inflate(R.layout.jc_volume_dialog, null);
            mDialogVolumeProgressBar = ((ProgressBar) localView.findViewById(R.id.volume_progressbar));
            mVolumeDialog = new Dialog(getContext(), R.style.jc_style_dialog_progress);
            mVolumeDialog.setContentView(localView);
            mVolumeDialog.getWindow().addFlags(8);
            mVolumeDialog.getWindow().addFlags(32);
            mVolumeDialog.getWindow().addFlags(16);
            mVolumeDialog.getWindow().setLayout(-2, -2);
            WindowManager.LayoutParams localLayoutParams = mVolumeDialog.getWindow().getAttributes();
            localLayoutParams.gravity = 19;
            localLayoutParams.x = getContext().getResources().getDimensionPixelOffset(R.dimen.jc_volume_dialog_margin_left);
            mVolumeDialog.getWindow().setAttributes(localLayoutParams);
        }
        if (!mVolumeDialog.isShowing()) {
            mVolumeDialog.show();
        }
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int deltaV = (int) (max * deltaY * 3 / mScreenHeight);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mDownVolume + deltaV, 0);
        int transformatVolume = (int) (mDownVolume * 100 / max + deltaY * 3 * 100 / mScreenHeight);
        mDialogVolumeProgressBar.setProgress(transformatVolume);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        try {
            if (mCurrentState != CURRENT_STATE_PLAYING &&
                    mCurrentState != CURRENT_STATE_PAUSE) return;
            if (fromUser) {
//                int time = progress * getDuration() / 100;
//                JCMediaManager.instance().mediaPlayer.seekTo(time);
//                Log.d(TAG, "seekTo " + time + " [" + this.hashCode() + "] ");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        try {
            if (progressBar != null) {
                progressBar.setProgress(getCurrentPositionWhenPlaying() / getDuration() * 100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPrepared() {
        if (mCurrentState != CURRENT_STATE_PREPAREING) return;
        JCMediaManager.instance().mediaPlayer.start();
        startProgressTimer();
        setStateAndUi(CURRENT_STATE_PLAYING);
    }

    @Override
    public void onAutoCompletion() {
        //make me normal first
        if (JC_BURIED_POINT != null && JCMediaManager.instance().listener == this) {
            if (mIfCurrentIsFullscreen) {
                JC_BURIED_POINT.onAutoCompleteFullscreen(mUrl, mObjects);
            } else {
                JC_BURIED_POINT.onAutoComplete(mUrl, mObjects);
            }
        }
        setStateAndUi(CURRENT_STATE_AUTO_COMPLETE);
        if (textureViewContainer.getChildCount() > 0) {
            textureViewContainer.removeAllViews();
        }
        finishFullscreenActivity();
        if (IF_FULLSCREEN_FROM_NORMAL) {//如果在进入全屏后播放完就初始化自己非全屏的控件
            IF_FULLSCREEN_FROM_NORMAL = false;
            JCMediaManager.instance().lastListener.onAutoCompletion();
        }
        JCMediaManager.instance().lastListener = null;
        AudioManager mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.abandonAudioFocus(onAudioFocusChangeListener);
        ((Activity) getContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onCompletion() {
        //make me normal first
        setStateAndUi(CURRENT_STATE_NORMAL);
        if (textureViewContainer.getChildCount() > 0) {
            textureViewContainer.removeAllViews();
        }
        //if fullscreen finish activity what ever the activity is directly or click fullscreen
        if (App.role >= 7) {
            if (getContext() instanceof JCFullScreenActivity) {
                Log.i(TAG, "finishFullscreenActivity [" + this.hashCode() + "] ");
                ((JCFullScreenActivity) getContext()).finish();
            }
        }

        if (IF_FULLSCREEN_FROM_NORMAL) {//如果在进入全屏后播放完就初始化自己非全屏的控件
            IF_FULLSCREEN_FROM_NORMAL = false;
            JCMediaManager.instance().lastListener.onCompletion();
        }
        JCMediaManager.instance().listener = null;
        JCMediaManager.instance().lastListener = null;
        JCMediaManager.instance().currentVideoWidth = 0;
        JCMediaManager.instance().currentVideoHeight = 0;

        AudioManager mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.abandonAudioFocus(onAudioFocusChangeListener);
        ((Activity) getContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onBufferingUpdate(int percent) {
        if (mCurrentState != CURRENT_STATE_NORMAL && mCurrentState != CURRENT_STATE_PREPAREING) {
            setTextAndProgress(percent);
        }
    }

    @Override
    public void onSeekComplete() {

    }

    @Override
    public void onError(int what, int extra) {
        Log.e(TAG, "onError " + what + " - " + extra + " [" + this.hashCode() + "] ");
        if (what != 38 && what != -38) {
            setStateAndUi(CURRENT_STATE_ERROR);
        }
    }

    @Override
    public void onInfo(int what, int extra) {
        Log.i(TAG, "onInfo what - " + what + " extra - " + extra);
        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
            BACKUP_PLAYING_BUFFERING_STATE = mCurrentState;
            setStateAndUi(CURRENT_STATE_PLAYING_BUFFERING_START);
            Log.i(TAG, "MEDIA_INFO_BUFFERING_START");
        } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
            setStateAndUi(BACKUP_PLAYING_BUFFERING_STATE);
            Log.i(TAG, "MEDIA_INFO_BUFFERING_END");
        }
    }

    @Override
    public void onVideoSizeChanged() {
        int mVideoWidth = JCMediaManager.instance().currentVideoWidth;
        int mVideoHeight = JCMediaManager.instance().currentVideoHeight;
        if (mVideoWidth != 0 && mVideoHeight != 0) {
            textureView.requestLayout();
        }
    }

    @Override
    public void onBackFullscreen() {
        mCurrentState = JCMediaManager.instance().lastState;
        setStateAndUi(mCurrentState);
    }

    protected void startProgressTimer() {
        cancelProgressTimer();
        UPDATE_PROGRESS_TIMER = new Timer();
        UPDATE_PROGRESS_TIMER.schedule(new TimerTask() {
            @Override
            public void run() {
                if (getContext() != null && getContext() instanceof Activity) {
                    ((Activity) getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mCurrentState == CURRENT_STATE_PLAYING || mCurrentState == CURRENT_STATE_PAUSE) {
                                setTextAndProgress(0);
                            }
                        }
                    });
                }
            }
        }, 0, 300);
    }

    protected void startProgressTimer(final int time) {
        cancelProgressTimer();
        UPDATE_PROGRESS_TIMER = new Timer();
        UPDATE_PROGRESS_TIMER.schedule(new TimerTask() {
            @Override
            public void run() {
                if (getContext() != null && getContext() instanceof Activity) {
                    ((Activity) getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mCurrentState == CURRENT_STATE_PLAYING || mCurrentState == CURRENT_STATE_PAUSE) {
                                setTextAndProgress(time);
                            }
                        }
                    });
                }
            }
        }, 0, 300);
    }

    protected void cancelProgressTimer() {
        if (UPDATE_PROGRESS_TIMER != null) {
            UPDATE_PROGRESS_TIMER.cancel();
            UPDATE_PROGRESS_TIMER = null;
        }
    }

    protected int getCurrentPositionWhenPlaying() {
        int position = 0;
        if (mCurrentState == CURRENT_STATE_PLAYING || mCurrentState == CURRENT_STATE_PAUSE) {
            try {
                position = JCMediaManager.instance().mediaPlayer.getCurrentPosition();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                return position;
            }
        }
        return position;
    }

    protected int getDuration() {
        int duration = 0;
        try {
            if (VideoConfig.isFromAnchor) {
                duration = JCMediaManager.instance().mediaPlayer.getDuration();
            } else {
                duration = JCMediaManager.instance().mediaPlayer.getDuration();
                duration = duration > 5 * 60 * 1000 ? duration : duration * 50;
                if (duration < 3600 * 1000) {
                    duration = duration + 1800 * 1000;
                }

            }

        } catch (IllegalStateException e) {
            e.printStackTrace();
        } finally {
            return duration == 0 ? 1 : duration;
        }
    }

    protected void setTextAndProgress(int secProgress) {
        int position = getCurrentPositionWhenPlaying();
        int duration = getDuration();
        // if duration == 0 (e.g. in HLS streams) avoids ArithmeticException
        int progress = position * 100 / (duration == 0 ? 1 : duration);
        setProgressAndTime(progress, secProgress, position, duration);
    }

    protected void setProgressAndTime(int progress, int secProgress, int currentTime, int totalTime) {
        if (!mTouchingProgressBar) {
            if (progress != 0) progressBar.setProgress(progress);
        }
        //if (secProgress != 0) progressBar.setSecondaryProgress(secProgress);
        currentTimeTextView.setText(JCUtils.stringForTime(currentTime));
        //原始视频时长增加了40min，但是视频的进度没变 只是单纯的改变了显示的时间而已

        totalTimeTextView.setText(JCUtils.stringForTime(totalTime));
    }

    protected void resetProgressAndTime() {
        progressBar.setProgress(0);
        //progressBar.setSecondaryProgress(0);
        currentTimeTextView.setText(JCUtils.stringForTime(0));
        totalTimeTextView.setText(JCUtils.stringForTime(0));
    }

    protected void quitFullScreenGoToNormal() {
        Log.i(TAG, "quitFullScreenGoToNormal [" + this.hashCode() + "] ");
        if (JC_BURIED_POINT != null && JCMediaManager.instance().listener == this) {
            JC_BURIED_POINT.onQuitFullscreen(mUrl, mObjects);
        }
        JCMediaManager.instance().setDisplay(null);
        JCMediaManager.instance().listener = JCMediaManager.instance().lastListener;
        JCMediaManager.instance().lastListener = null;
        JCMediaManager.instance().lastState = mCurrentState;//save state
        JCMediaManager.instance().listener.onBackFullscreen();
        if (mCurrentState == CURRENT_STATE_PAUSE) {
            JCMediaManager.instance().mediaPlayer.seekTo(JCMediaManager.instance().mediaPlayer.getCurrentPosition());
        }
        finishFullscreenActivity();
    }

    protected void finishFullscreenActivity() {
        if (getContext() instanceof JCFullScreenActivity) {
            Log.i(TAG, "finishFullscreenActivity [" + this.hashCode() + "] ");
            ((JCFullScreenActivity) getContext()).finish();
        }
    }

    public void backFullscreen() {
        Log.d(TAG, "quitFullscreen [" + this.hashCode() + "] ");
        IF_FULLSCREEN_FROM_NORMAL = false;
        if (mIfFullscreenIsDirectly) {
            JCMediaManager.instance().mediaPlayer.stop();
            finishFullscreenActivity();
        } else {
            CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
            IF_RELEASE_WHEN_ON_PAUSE = false;
            quitFullScreenGoToNormal();
        }
    }

    public static void releaseAllVideos() {
        if (IF_RELEASE_WHEN_ON_PAUSE) {
            Log.i(TAG, "releaseAllVideos");
            if (JCMediaManager.instance().listener != null) {
                JCMediaManager.instance().listener.onCompletion();
            }
            JCMediaManager.instance().releaseMediaPlayer();
        } else {
            IF_RELEASE_WHEN_ON_PAUSE = true;
        }
    }

    /**
     * if I am playing release me
     */
    public void release() {
        if (JCMediaManager.instance().listener == this &&
//      mCurrentState != CURRENT_STATE_NORMAL &&
                (System.currentTimeMillis() - CLICK_QUIT_FULLSCREEN_TIME) > FULL_SCREEN_NORMAL_DELAY) {
            Log.i(TAG, "release [" + this.hashCode() + "]");
            releaseAllVideos();
        }
    }

}

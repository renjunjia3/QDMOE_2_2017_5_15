package com.hfaufhreu.hjfeuio;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.hfaufhreu.hjfeuio.adapter.CommentAdapter;
import com.hfaufhreu.hjfeuio.adapter.IndexItemAdapter;
import com.hfaufhreu.hjfeuio.app.App;
import com.hfaufhreu.hjfeuio.bean.CommentInfo;
import com.hfaufhreu.hjfeuio.bean.VideoInfo;
import com.hfaufhreu.hjfeuio.event.CloseVideoDetailEvent;
import com.hfaufhreu.hjfeuio.ui.view.CustomListView;
import com.hfaufhreu.hjfeuio.ui.view.CustomeGridView;
import com.hfaufhreu.hjfeuio.util.API;
import com.hfaufhreu.hjfeuio.util.DialogUtil;
import com.hfaufhreu.hjfeuio.util.NetWorkUtils;
import com.hfaufhreu.hjfeuio.util.SharedPreferencesUtil;
import com.hfaufhreu.hjfeuio.util.ToastUtils;
import com.hfaufhreu.hjfeuio.video.JCFullScreenActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import wiki.scene.statuslib.StatusViewLayout;

/**
 * Case By:视频详情
 * package:com.cyldf.cyldfxv
 * Author：scene on 2017/4/13 10:02
 */
public class VideoDetailActivity extends AppCompatActivity {

    public static final String ARG_VIDEO_INFO = "arg_video_info";
    public static final String ARG_IS_ENTER_FROM_TRY_SEE = "is_enter_from_try_see";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.zan)
    TextView zan;
    @BindView(R.id.fravetor)
    TextView fravetor;
    @BindView(R.id.open_vip)
    RelativeLayout openVip;
    @BindView(R.id.comment_listView)
    CustomListView commentListView;
    @BindView(R.id.detail_player)
    ImageView detailPlayer;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.addVip)
    TextView addVip;
    @BindView(R.id.commend_number)
    TextView commendNumber;
    @BindView(R.id.aboutCommendGridView)
    CustomeGridView aboutCommendGridView;
    @BindView(R.id.statusViewLayout)
    StatusViewLayout statusViewLayout;
    @BindView(R.id.aboutCommendTextView)
    TextView aboutCommendTextView;
    @BindView(R.id.sendComment)
    ImageView sendComment;
    @BindView(R.id.screenShotRecyclerView)
    RecyclerView screenShotRecyclerView;
    @BindView(R.id.scrollView)
    ScrollView scrollView;

    private Unbinder unbinder;

    private VideoInfo videoInfo;
    private Boolean isEnterFromTrySee = false;

    private List<CommentInfo> commentInfoList;
    private Random random;
    private CommentAdapter commentAdapter;

    //相关推荐
    private List<VideoInfo> videoRelateList = new ArrayList<>();
    private IndexItemAdapter videoRelateAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.fragment_video_detail);
        unbinder = ButterKnife.bind(this);
        videoInfo = (VideoInfo) getIntent().getSerializableExtra(ARG_VIDEO_INFO);
        isEnterFromTrySee = getIntent().getBooleanExtra(ARG_IS_ENTER_FROM_TRY_SEE, false);

        initToolbarNav(toolbar);
        initView();
        enterVideoDetail();
    }

    protected void initToolbarNav(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initView() {
        random = new Random();
        commendNumber.setText((random.nextInt(1000) + 580) + "");
        toolbarTitle.setText(videoInfo.getTitle());
        zan.setText(videoInfo.getHits() + "");
        Glide.with(this).load(videoInfo.getThumb()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(detailPlayer);
        //相关推荐
        videoRelateAdapter = new IndexItemAdapter(VideoDetailActivity.this, videoRelateList);
        aboutCommendGridView.setAdapter(videoRelateAdapter);
        getRecomendVideo();
        //获取评论的数据
        getCommentData();
    }


    @OnClick({R.id.zan, R.id.fravetor, R.id.open_vip, R.id.addVip, R.id.open_vip1, R.id.sendComment, R.id.download, R.id.commend_number})
    public void onClick(View v) {
        if (App.isVip == 0) {
            DialogUtil.getInstance().showSubmitDialog(VideoDetailActivity.this, false, "该功能为会员功能，请成为会员后使用", App.isVip, true, videoInfo.getVideo_id());
        } else {
            ToastUtils.getInstance(VideoDetailActivity.this).showToast("该功能完善中，敬请期待");
        }
    }


    /**
     * 弹出支付窗口之后调用
     */
    private void clickWantPay() {
        OkHttpUtils.get().url(API.URL_PRE + API.PAY_CLICK + App.IMEI).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(String s, int i) {
                Log.e("s", "sssss----->" + s);
            }
        });
    }

    @OnClick(R.id.play_video)
    public void onClickPlayVideo() {
        if (!isEnterFromTrySee && App.isVip == 0) {
            //不是首页进来自己也不是VIP，弹出开通会员的提示
            DialogUtil.getInstance().showSubmitDialog(VideoDetailActivity.this, false, "非会员只能试看体验，请成为会员继续观看", App.isVip, true, videoInfo.getVideo_id());
        } else {
            Intent intent = new Intent(VideoDetailActivity.this, JCFullScreenActivity.class);
            intent.putExtra(JCFullScreenActivity.PARAM_VIDEO_INFO, videoInfo);
            intent.putExtra(JCFullScreenActivity.PARAM_CURRENT_TIME, currentTime);
            startActivityForResult(intent, 101);
        }

    }

    private long currentTime = 0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            currentTime = data.getLongExtra(JCFullScreenActivity.PARAM_CURRENT_TIME, 0L);
            int dialogType = data.getIntExtra(JCFullScreenActivity.PARAM_DIALOG_TYPE, 0);
            switch (dialogType) {
                case JCFullScreenActivity.DIALOG_TYPE_GLOD:
                    //黄金
                    DialogUtil.getInstance().showSubmitDialog(VideoDetailActivity.this, true, "非会员只能试看体验，请成为会员继续观看", App.isVip, true, videoInfo.getVideo_id());
                    break;
                case JCFullScreenActivity.DIALOG_TYPE_DIAMOND:
                    //砖石
                    DialogUtil.getInstance().showSubmitDialog(VideoDetailActivity.this, true, "请升级钻石会员，观看完整影片", App.isVip, true, videoInfo.getVideo_id());
                    break;
                case JCFullScreenActivity.DIALOG_TYPE_VPN:
                    //VPN
                    DialogUtil.getInstance().showSubmitDialog(VideoDetailActivity.this, true, "由于现法律不允许国内播放，请注册VPN翻墙观看，现仅需28元终身免费使用", App.isVip, true, videoInfo.getVideo_id());
                    break;
                case JCFullScreenActivity.DIALOG_TYPE_OVERSEA_FLIM:
                    //片库
                    DialogUtil.getInstance().showSubmitDialog(VideoDetailActivity.this, true, "海外视频提供商需收取3美金服务费。付费后海量视频终身免费观看", App.isVip, true, videoInfo.getVideo_id());
                    break;
                case JCFullScreenActivity.DIALOG_TYPE_BLACK_GLOD:
                    //黑金会员
                    DialogUtil.getInstance().showSubmitDialog(VideoDetailActivity.this, true, "最后一次付费，您将得到您想要的，黑金会员，开放所有影片，你值得拥有！", App.isVip, true, videoInfo.getVideo_id());
                    break;
                case JCFullScreenActivity.DIALOG_TYPE_OVERSEA_SPEED:
                    //海外加速
                    DialogUtil.getInstance().showSubmitDialog(VideoDetailActivity.this, true, "网络太慢了！由于目前观看用户太多。你的国内线路宽带太低.是否需要切换到海外高速通道？", App.isVip, true, videoInfo.getVideo_id());
                    break;
                case JCFullScreenActivity.DIALOG_TYPE_OVERSEA_SNAP:
                    //海外双线
                    DialogUtil.getInstance().showSubmitDialog(VideoDetailActivity.this, true, "网络拥堵无法播放 开通海外双线？", App.isVip, true, videoInfo.getVideo_id());
                    break;

            }


        }
    }

    /**
     * 获取相关推荐
     */
    public void getRecomendVideo() {
        statusViewLayout.showLoading();
        if (NetWorkUtils.isNetworkConnected(VideoDetailActivity.this)) {
            OkHttpUtils.get().url(API.URL_PRE + API.VIDEO_RELATED + videoInfo.getVideo_id()).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int i) {
                    aboutCommendTextView.setVisibility(View.GONE);
                    aboutCommendGridView.setVisibility(View.GONE);
                    statusViewLayout.showContent();
                }

                @Override
                public void onResponse(String s, int i) {
                    try {
                        videoRelateList.clear();
                        videoRelateList.addAll(JSON.parseArray(s, VideoInfo.class));
                        videoRelateAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (videoRelateList.size() > 0) {
                            aboutCommendTextView.setVisibility(View.VISIBLE);
                            aboutCommendGridView.setVisibility(View.VISIBLE);
                        } else {
                            aboutCommendTextView.setVisibility(View.GONE);
                            aboutCommendGridView.setVisibility(View.GONE);
                        }
                        statusViewLayout.showContent();
                    }
                }
            });

        } else {
            statusViewLayout.showNetError(onClickRetryListener);
        }
    }

    View.OnClickListener onClickRetryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getRecomendVideo();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Case By:获取评论的数据
     * Author: scene on 2017/4/13 18:48
     */
    private void getCommentData() {
        OkHttpUtils.get().url(API.URL_PRE + API.VIDEO_COMMENT).build().execute(new StringCallback() {
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
                    commentAdapter = new CommentAdapter(VideoDetailActivity.this, commentInfoList);
                    commentListView.setAdapter(commentAdapter);
                    detailPlayer.setFocusable(true);
                    detailPlayer.setFocusableInTouchMode(true);
                    detailPlayer.requestFocus();
                    SharedPreferencesUtil.putString(VideoDetailActivity.this, JCFullScreenActivity.PARAM_STR_COMMENT, s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void enterVideoDetail() {
        OkHttpUtils.get().url(API.URL_PRE + API.VIDEO_CLIECKED + App.IMEI + "/" + videoInfo.getVideo_id()).build().execute(new StringCallback() {
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
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Subscribe
    public void closeVideoDetail(CloseVideoDetailEvent closeVideoDetailEvent) {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }


}

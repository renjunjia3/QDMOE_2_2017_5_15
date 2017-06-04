package com.mzhguqvn.mzhguq;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aitangba.swipeback.SwipeBackActivity;
import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.mzhguqvn.mzhguq.adapter.CommentAdapter;
import com.mzhguqvn.mzhguq.adapter.IndexItemAdapter;
import com.mzhguqvn.mzhguq.app.App;
import com.mzhguqvn.mzhguq.bean.CheckOrderInfo;
import com.mzhguqvn.mzhguq.bean.CommentInfo;
import com.mzhguqvn.mzhguq.bean.VideoCommentResultInfo;
import com.mzhguqvn.mzhguq.bean.VideoInfo;
import com.mzhguqvn.mzhguq.bean.VideoRelateResultInfo;
import com.mzhguqvn.mzhguq.config.PageConfig;
import com.mzhguqvn.mzhguq.event.CloseVideoDetailEvent;
import com.mzhguqvn.mzhguq.ui.dialog.OpenVipNoticeDialog;
import com.mzhguqvn.mzhguq.ui.view.CustomListView;
import com.mzhguqvn.mzhguq.ui.view.CustomeGridView;
import com.mzhguqvn.mzhguq.util.API;
import com.mzhguqvn.mzhguq.util.DialogUtil;
import com.mzhguqvn.mzhguq.util.NetWorkUtils;
import com.mzhguqvn.mzhguq.util.SharedPreferencesUtil;
import com.mzhguqvn.mzhguq.util.ToastUtils;
import com.mzhguqvn.mzhguq.video.JCFullScreenActivity;
import com.mzhguqvn.mzhguq.video.VideoConfig;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class VideoDetailActivity extends SwipeBackActivity {

    public static final String ARG_VIDEO_INFO = "arg_video_info";
    public static final String ARG_IS_ENTER_FROM_TRY_SEE = "is_enter_from_try_see";
    public static final String ARG_IS_ENTER_ANCHOR = "is_enter_anchor";
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
    @BindView(R.id.comment_content)
    EditText commentContent;
    @BindView(R.id.play_count)
    TextView playCount;

    private Unbinder unbinder;

    private VideoInfo videoInfo;
    private Boolean isEnterFromTrySee = false;

    private List<CommentInfo> commentInfoList;
    private Random random;
    private CommentAdapter commentAdapter;

    //相关推荐
    private List<VideoInfo> videoRelateList = new ArrayList<>();
    private IndexItemAdapter videoRelateAdapter;


    private boolean isAnchor = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.fragment_video_detail);
        registerBoradcastReceiver();
        unbinder = ButterKnife.bind(this);
        videoInfo = (VideoInfo) getIntent().getSerializableExtra(ARG_VIDEO_INFO);
        isAnchor = getIntent().getBooleanExtra(ARG_IS_ENTER_ANCHOR, false);
        isEnterFromTrySee = getIntent().getBooleanExtra(ARG_IS_ENTER_FROM_TRY_SEE, false);

        initToolbarNav(toolbar);
        initView();
        MainActivity.upLoadPageInfo(PageConfig.VIDEO_DETAIL_POSITION_ID, videoInfo.getVideo_id(), 0);
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
        playCount.setText(videoInfo.getHits() + "次播放");
        Glide.with(this).load(videoInfo.getThumb_heng()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(detailPlayer);
        //相关推荐
        videoRelateAdapter = new IndexItemAdapter(VideoDetailActivity.this, videoRelateList);
        aboutCommendGridView.setAdapter(videoRelateAdapter);
        aboutCommendGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(VideoDetailActivity.this, VideoDetailActivity.class);
                intent.putExtra(VideoDetailActivity.ARG_VIDEO_INFO, videoRelateList.get(position));
                intent.putExtra(VideoDetailActivity.ARG_IS_ENTER_ANCHOR, isAnchor);
                startActivity(intent);
                finish();
            }
        });
        getRecomendVideo();
        //获取评论的数据
        //getCommentData();

        progressDialog = new ProgressDialog(VideoDetailActivity.this);
        progressDialog.setMessage("正在获取支付结果...");
    }


    @OnClick({R.id.zan, R.id.fravetor, R.id.open_vip, R.id.addVip, R.id.open_vip1, R.id.sendComment, R.id.download, R.id.commend_number})
    public void onClick(View v) {
        if (App.role == 0) {
            DialogUtil.getInstance().showSubmitDialog(VideoDetailActivity.this, false,
                    "该功能为会员功能，请成为会员后使用", App.role, false, true, videoInfo.getVideo_id(), true, PageConfig.VIDEO_DETAIL_POSITION_ID);
        } else {
            if (v.getId() == R.id.sendComment) {
                String content = commentContent.getText().toString().trim();
                if (!content.isEmpty()) {
                    sendComment(content);
                }
            }
        }
    }

    /**
     * Case By:发布评论
     * Author: scene on 2017/5/3 13:26
     */
    private ProgressDialog commentProgressDialog;
    private RequestCall sendCommentCall;

    private void sendComment(final String content) {
        if (commentProgressDialog == null) {
            commentProgressDialog = new ProgressDialog(VideoDetailActivity.this);
            commentProgressDialog.setMessage("加载中...");
        }
        if (NetWorkUtils.isMobileConnected(VideoDetailActivity.this)) {
            if (commentProgressDialog != null) {
                commentProgressDialog.show();
            }
            HashMap<String, String> params = API.createParams();
            params.put("video_id", videoInfo.getVideo_id() + "");
            params.put("content", content);
            params.put("user_id", App.user_id + "");
            sendCommentCall = OkHttpUtils.post().url(API.URL_PRE + API.SEND_COMMEND).params(params).build();
            sendCommentCall.execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int i) {
                    try {
                        if (commentProgressDialog != null && commentProgressDialog.isShowing()) {
                            commentProgressDialog.cancel();
                        }
                        commentContent.setText("");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                }

                @Override
                public void onResponse(String s, int i) {
                    try {
                        commentContent.setText("");
                        if (commentProgressDialog != null && commentProgressDialog.isShowing()) {
                            commentProgressDialog.cancel();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } else {
            ToastUtils.getInstance(VideoDetailActivity.this).showToast("评论失败，请检查网络");
        }
    }


    @OnClick(R.id.play_video)
    public void onClickPlayVideo() {
        if (isEnterFromTrySee && App.role == 0 && App.tryCount >= VideoConfig.TRY_COUNT_TIME) {
            //游客试看区进来的没有试看次数
            DialogUtil.getInstance().showSubmitDialog(VideoDetailActivity.this, false,
                    "游客只能试看" + VideoConfig.TRY_COUNT_TIME + "次，请开通会员继续观看", App.role, false, true, videoInfo.getVideo_id(),
                    true, PageConfig.VIDEO_DETAIL_POSITION_ID);
        } else if (!isEnterFromTrySee && App.role == 0) {
            //游客不是试看区进来的
            DialogUtil.getInstance().showSubmitDialog(VideoDetailActivity.this, false,
                    "该片为会员视频，请开通会员继续观看", App.role, false, true, videoInfo.getVideo_id(),
                    true, PageConfig.VIDEO_DETAIL_POSITION_ID);
        } else if (isAnchor && App.cdn == 0) {
            //主播进来的但是没开cdn
            DialogUtil.getInstance().showSubmitDialog(VideoDetailActivity.this, false, "由于服务器开销较大，如需观看需缴纳CDN费用",
                    App.role, false, true, videoInfo.getVideo_id(), false, PageConfig.VIDEO_DETAIL_POSITION_ID);
        } else {
            VideoConfig.isFromAnchor = isAnchor;
            App.tryCount += 1;
            SharedPreferencesUtil.putInt(VideoDetailActivity.this, App.TRY_COUNT_KEY, App.tryCount);
            Intent intent = new Intent(VideoDetailActivity.this, JCFullScreenActivity.class);
            intent.putExtra(JCFullScreenActivity.PARAM_VIDEO_INFO, videoInfo);
            intent.putExtra(JCFullScreenActivity.PARAM_CURRENT_TIME, currentTime);
            startActivityForResult(intent, 101);
        }

    }

    private int currentTime = 0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            currentTime = data.getIntExtra(JCFullScreenActivity.PARAM_CURRENT_TIME, 0);
            int dialogType = data.getIntExtra(JCFullScreenActivity.PARAM_DIALOG_TYPE, 0);
            switch (dialogType) {
                case JCFullScreenActivity.DIALOG_TYPE_GLOD:
                    //黄金
                    DialogUtil.getInstance().showGoldVipDialog(VideoDetailActivity.this, videoInfo.getVideo_id(), true, PageConfig.VIDEO_DETAIL_POSITION_ID);
                    break;
                case JCFullScreenActivity.DIALOG_TYPE_DIAMOND:
                    //砖石
                    DialogUtil.getInstance().showDiamondVipDialog(VideoDetailActivity.this, videoInfo.getVideo_id(), true, PageConfig.VIDEO_DETAIL_POSITION_ID);
                    break;
                case JCFullScreenActivity.DIALOG_TYPE_CDN:
                    //CDN
                    DialogUtil.getInstance().showCdnVipDialog(VideoDetailActivity.this, videoInfo.getVideo_id(), true, PageConfig.VIDEO_DETAIL_POSITION_ID);
                    break;

            }

        }
    }

    /**
     * 获取相关推荐
     */
    private RequestCall recommendRequestCall;

    public void getRecomendVideo() {
        statusViewLayout.showLoading();
        if (NetWorkUtils.isNetworkConnected(VideoDetailActivity.this)) {
            HashMap<String, String> params = API.createParams();
            params.put("video_id", videoInfo.getVideo_id() + "");
            params.put("zhubo", isAnchor ? String.valueOf(1) : String.valueOf(0));
            recommendRequestCall = OkHttpUtils.get().url(API.URL_PRE + API.VIDEO_RELATED).params(params).build();
            recommendRequestCall.execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int i) {
                    try {
                        if (!isFinishing()) {
                            statusViewLayout.showContent();
                            aboutCommendTextView.setVisibility(View.GONE);
                            aboutCommendGridView.setVisibility(View.GONE);
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }

                @Override
                public void onResponse(String s, int i) {
                    try {
                        VideoRelateResultInfo relateResultInfo = JSON.parseObject(s, VideoRelateResultInfo.class);

                        videoRelateList.clear();
                        videoRelateList.addAll(relateResultInfo.getData());
                        videoRelateAdapter.notifyDataSetChanged();
                        if (videoRelateList.size() > 0) {
                            aboutCommendTextView.setVisibility(View.VISIBLE);
                            aboutCommendGridView.setVisibility(View.VISIBLE);
                        } else {
                            aboutCommendTextView.setVisibility(View.GONE);
                            aboutCommendGridView.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
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
        if (App.isGoodsPay && App.isNeedCheckOrder && App.goodsOrderId != 0) {

        } else if (App.isNeedCheckOrder && App.orderIdInt != 0) {
            checkOrder();
        }
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * Case By:获取评论的数据
     * Author: scene on 2017/4/13 18:48
     */
    private RequestCall commentReqstCall;

    private void getCommentData() {
        HashMap<String, String> params = API.createParams();
        commentReqstCall = OkHttpUtils.get().url(API.URL_PRE + API.VIDEO_COMMENT).params(params).build();
        commentReqstCall.execute(new StringCallback() {
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
                    commentAdapter = new CommentAdapter(VideoDetailActivity.this, commentInfoList);
                    commentListView.setAdapter(commentAdapter);
                    detailPlayer.setFocusable(true);
                    detailPlayer.setFocusableInTouchMode(true);
                    detailPlayer.requestFocus();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        if (requestCall != null) {
            requestCall.cancel();
        }
        if (recommendRequestCall != null) {
            recommendRequestCall.cancel();
        }
        if (commentReqstCall != null) {
            commentReqstCall.cancel();
        }
        commentListView.setAdapter(null);
        DialogUtil.getInstance().cancelAllDialog();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
        unRegisterBoradcastReceiver();
        super.onDestroy();
    }

    @Subscribe
    public void closeVideoDetail(CloseVideoDetailEvent closeVideoDetailEvent) {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
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
                Map<String, String> params = API.createParams();
                params.put("order_id", App.orderIdInt + "");
                App.isNeedCheckOrder = false;
                App.orderIdInt = 0;
                requestCall = OkHttpUtils.get().url(API.URL_PRE + API.CHECK_ORDER).params(params).build();
                requestCall.execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        e.printStackTrace();
                        ToastUtils.getInstance(VideoDetailActivity.this).showToast("支付失败请重试，或者更换其他支付方式");
                        if (progressDialog != null && progressDialog.isShowing()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                }
                            });
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
                                MainActivity.onPaySuccess();
                                MainActivity.isNeedChangeTab = true;
                                String message1 = "";
                                String message2 = "";
                                switch (App.role) {
                                    case 0:
                                        App.role = 1;
                                        message1 = "黄金会员";
                                        message2 = "价值38元";
                                        SharedPreferencesUtil.putInt(VideoDetailActivity.this, App.ROLE_KEY, App.role);
                                        OpenVipNoticeDialog openVipNoticeDialog0 = DialogUtil.getInstance().showOpenVipNoticeDialog(VideoDetailActivity.this, message1, message2);
                                        openVipNoticeDialog0.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialog) {
                                                closeVideoDetail(new CloseVideoDetailEvent());
                                            }
                                        });
                                        break;
                                    case 1:
//                                        App.role = 2;
//                                        message = "恭喜您成为钻石会员";
//                                        SharedPreferencesUtil.putInt(VideoDetailActivity.this, App.ROLE_KEY, App.role);
//                                        CustomSubmitDialog customSubmitDialog1 = DialogUtil.getInstance().showCustomSubmitDialog(VideoDetailActivity.this, message);
//                                        customSubmitDialog1.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                                            @Override
//                                            public void onDismiss(DialogInterface dialog) {
//                                                closeVideoDetail(new CloseVideoDetailEvent());
//                                            }
//                                        });

                                        App.cdn = 1;
                                        message1 = "CDN加速";
                                        message2 = "价值28元";
                                        SharedPreferencesUtil.putInt(VideoDetailActivity.this, App.CDN_KEY, App.cdn);
                                        OpenVipNoticeDialog openVipNoticeDialog1 = DialogUtil.getInstance().showOpenVipNoticeDialog(VideoDetailActivity.this, message1, message2);
                                        openVipNoticeDialog1.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialog) {
                                                closeVideoDetail(new CloseVideoDetailEvent());
                                            }
                                        });
                                        break;
//                                    case 2:
//                                        App.cdn = 1;
//                                        message = "恭喜您成功开通CDN加速服务";
//                                        SharedPreferencesUtil.putInt(VideoDetailActivity.this, App.ROLE_KEY, App.role);
//                                        CustomSubmitDialog customSubmitDialog2 = DialogUtil.getInstance().showCustomSubmitDialog(VideoDetailActivity.this, message);
//                                        customSubmitDialog2.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                                            @Override
//                                            public void onDismiss(DialogInterface dialog) {
//                                                closeVideoDetail(new CloseVideoDetailEvent());
//                                            }
//                                        });
//                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                ToastUtils.getInstance(VideoDetailActivity.this).showToast("支付失败请重试，或者更换其他支付方式");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }, 1000);

    }


    public static final String ACTION_NAME_VIDEODETAILACTIVITY_CHECK_ORDER = "action_name_VideoDetailActivity_check_order";
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_NAME_VIDEODETAILACTIVITY_CHECK_ORDER)) {
                checkOrder();
            }
        }
    };

    private void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ACTION_NAME_VIDEODETAILACTIVITY_CHECK_ORDER);
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    private void unRegisterBoradcastReceiver() {
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }
    }
}

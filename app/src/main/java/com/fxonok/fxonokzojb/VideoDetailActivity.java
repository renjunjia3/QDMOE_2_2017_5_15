package com.fxonok.fxonokzojb;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aitangba.swipeback.SwipeBackActivity;
import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.fxonok.fxonokzojb.adapter.CommentAdapter;
import com.fxonok.fxonokzojb.adapter.IndexItemAdapter;
import com.fxonok.fxonokzojb.app.App;
import com.fxonok.fxonokzojb.bean.CheckOrderInfo;
import com.fxonok.fxonokzojb.bean.CommentInfo;
import com.fxonok.fxonokzojb.bean.VideoInfo;
import com.fxonok.fxonokzojb.event.CloseVideoDetailEvent;
import com.fxonok.fxonokzojb.ui.dialog.CustomSubmitDialog;
import com.fxonok.fxonokzojb.ui.view.CustomListView;
import com.fxonok.fxonokzojb.ui.view.CustomeGridView;
import com.fxonok.fxonokzojb.util.API;
import com.fxonok.fxonokzojb.util.DialogUtil;
import com.fxonok.fxonokzojb.util.NetWorkUtils;
import com.fxonok.fxonokzojb.util.SharedPreferencesUtil;
import com.fxonok.fxonokzojb.video.JCFullScreenActivity;
import com.fxonok.fxonokzojb.video.VideoConfig;
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
        uploadCurrentPage();
    }


    /**
     * Case By:上报当前页面
     * Author: scene on 2017/4/27 17:05
     */
    private void uploadCurrentPage() {
        Map<String, String> params = new HashMap<>();
        params.put("position_id", "7");
        params.put("user_id", App.USER_ID + "");
        params.put("video_id", videoInfo.getVideo_id() + "");
        OkHttpUtils.post().url(API.URL_PRE + API.UPLOAD_CURRENT_PAGE).params(params).build().execute(null);
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
        Glide.with(this).load(videoInfo.getThumb_heng()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(detailPlayer);
        //相关推荐
        videoRelateAdapter = new IndexItemAdapter(VideoDetailActivity.this, videoRelateList);
        aboutCommendGridView.setAdapter(videoRelateAdapter);
        aboutCommendGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(VideoDetailActivity.this, VideoDetailActivity.class);
                intent.putExtra(VideoDetailActivity.ARG_VIDEO_INFO, videoRelateList.get(position));
                startActivity(intent);
                finish();
            }
        });
        getRecomendVideo();
        //获取评论的数据
        getCommentData();

        progressDialog = new ProgressDialog(VideoDetailActivity.this);
        progressDialog.setMessage("正在获取支付结果...");
    }


    @OnClick({R.id.zan, R.id.fravetor, R.id.open_vip, R.id.addVip, R.id.open_vip1, R.id.sendComment, R.id.download, R.id.commend_number})
    public void onClick(View v) {
        if (App.isVip == 0) {
            DialogUtil.getInstance().showSubmitDialog(VideoDetailActivity.this, false, "该功能为会员功能，请成为会员后使用", App.isVip, false, true, videoInfo.getVideo_id());
        }
    }

    @OnClick(R.id.play_video)
    public void onClickPlayVideo() {
        if (!isEnterFromTrySee && App.isVip == 0) {
            //不是首页进来自己也不是VIP，弹出开通会员的提示
            DialogUtil.getInstance().showSubmitDialog(VideoDetailActivity.this, false, "非会员只能试看体验，请成为会员继续观看", App.isVip, false, true, videoInfo.getVideo_id());
        } else {
            if (App.tryCount >= VideoConfig.TRY_COUNT_TIME && App.isVip == 0) {
                DialogUtil.getInstance().showSubmitDialog(VideoDetailActivity.this, false, "非会员只能试看" + VideoConfig.TRY_COUNT_TIME + "次，请成为会员继续观看", App.isVip, false, true, videoInfo.getVideo_id());
            } else {
                App.tryCount += 1;
                SharedPreferencesUtil.putInt(VideoDetailActivity.this, App.TRY_COUNT_KEY, App.tryCount);
                Intent intent = new Intent(VideoDetailActivity.this, JCFullScreenActivity.class);
                intent.putExtra(JCFullScreenActivity.PARAM_VIDEO_INFO, videoInfo);
                intent.putExtra(JCFullScreenActivity.PARAM_CURRENT_TIME, currentTime);
                startActivityForResult(intent, 101);
            }
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
                    DialogUtil.getInstance().showGoldVipDialog(VideoDetailActivity.this, videoInfo.getVideo_id(), true);
                    break;
                case JCFullScreenActivity.DIALOG_TYPE_DIAMOND:
                    //砖石
                    DialogUtil.getInstance().showDiamondVipDialog(VideoDetailActivity.this, videoInfo.getVideo_id(), true);
                    break;
                case JCFullScreenActivity.DIALOG_TYPE_VPN:
                    //VPN
                    DialogUtil.getInstance().showVpnVipDialog(VideoDetailActivity.this, videoInfo.getVideo_id(), true);
                    break;
                case JCFullScreenActivity.DIALOG_TYPE_OVERSEA_FLIM:
                    //片库
                    DialogUtil.getInstance().showVpnFlimVipDialog(VideoDetailActivity.this, videoInfo.getVideo_id(), true);
                    break;
                case JCFullScreenActivity.DIALOG_TYPE_BLACK_GLOD:
                    //黑金会员
                    DialogUtil.getInstance().showBlackGlodVipDialog(VideoDetailActivity.this, videoInfo.getVideo_id(), true);
                    break;
                case JCFullScreenActivity.DIALOG_TYPE_OVERSEA_SPEED:
                    //海外加速
                    DialogUtil.getInstance().showAccelerationChannelVipDialog(VideoDetailActivity.this, videoInfo.getVideo_id(), true);
                    break;
                case JCFullScreenActivity.DIALOG_TYPE_OVERSEA_SNAP:
                    //海外双线
                    DialogUtil.getInstance().showRapidDoubletVipDialog(VideoDetailActivity.this, videoInfo.getVideo_id(), true);
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
            recommendRequestCall = OkHttpUtils.get().url(API.URL_PRE + API.VIDEO_RELATED + videoInfo.getVideo_id()).build();
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
                        videoRelateList.clear();
                        videoRelateList.addAll(JSON.parseArray(s, VideoInfo.class));
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
        if (App.isNeedCheckOrder && App.orderIdInt != 0) {
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
        commentReqstCall = OkHttpUtils.get().url(API.URL_PRE + API.VIDEO_COMMENT).build();
        commentReqstCall.execute(new StringCallback() {
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
                    //SharedPreferencesUtil.putString(VideoDetailActivity.this, JCFullScreenActivity.PARAM_STR_COMMENT, s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Case By:统计进入页面的次数
     * Author: scene on 2017/4/27 14:20
     */
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
        if (requestCall != null) {
            requestCall.cancel();
        }
        if (recommendRequestCall != null) {
            recommendRequestCall.cancel();
        }
        if (commentReqstCall != null) {
            commentReqstCall.cancel();
        }
        DialogUtil.getInstance().cancelAllDialog();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
        DialogUtil.getInstance().cancelAllDialog();
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
                                MainActivity.isNeedChangeTab = true;
                                String message = "";
                                switch (App.isVip) {
                                    case 0:
                                        App.isVip = 1;
                                        message = "恭喜您成为黄金会员";
                                        SharedPreferencesUtil.putInt(VideoDetailActivity.this, App.ISVIP_KEY, App.isVip);
                                        CustomSubmitDialog customSubmitDialog0 = DialogUtil.getInstance().showCustomSubmitDialog(VideoDetailActivity.this, message);
                                        customSubmitDialog0.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialog) {
                                                closeVideoDetail(new CloseVideoDetailEvent());
                                            }
                                        });
                                        break;
                                    case 1:
                                        App.isVip = 2;
                                        message = "恭喜您成为钻石会员";
                                        SharedPreferencesUtil.putInt(VideoDetailActivity.this, App.ISVIP_KEY, App.isVip);
                                        CustomSubmitDialog customSubmitDialog1 = DialogUtil.getInstance().showCustomSubmitDialog(VideoDetailActivity.this, message);
                                        customSubmitDialog1.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialog) {
                                                closeVideoDetail(new CloseVideoDetailEvent());
                                            }
                                        });
                                        break;
                                    case 2:
                                        if (App.isOPenBlackGlodVip) {
                                            App.isVip = 2;
                                            message = "恭喜您成为最牛逼的黑金会员";
                                        } else {
                                            App.isVip = 3;
                                            message = "恭喜您成功注册VPN海外会员";
                                        }
                                        SharedPreferencesUtil.putInt(VideoDetailActivity.this, App.ISVIP_KEY, App.isVip);
                                        CustomSubmitDialog customSubmitDialog2 = DialogUtil.getInstance().showCustomSubmitDialog(VideoDetailActivity.this, message);
                                        customSubmitDialog2.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialog) {
                                                closeVideoDetail(new CloseVideoDetailEvent());
                                            }
                                        });
                                        break;
                                    case 3:
                                        if (App.isHeijin == 1) {
                                            App.isVip = 5;
                                        } else {
                                            App.isVip = 4;
                                        }
                                        message = "恭喜你进入海外片库，我们将携手为您服务";
                                        SharedPreferencesUtil.putInt(VideoDetailActivity.this, App.ISVIP_KEY, App.isVip);
                                        CustomSubmitDialog customSubmitDialog3 = DialogUtil.getInstance().showCustomSubmitDialog(VideoDetailActivity.this, message);
                                        customSubmitDialog3.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialog) {
                                                closeVideoDetail(new CloseVideoDetailEvent());
                                            }
                                        });
                                        break;
                                    case 4:
                                        if (App.isVip >= 4) {
                                            App.isVip = 5;
                                            SharedPreferencesUtil.putInt(VideoDetailActivity.this, App.ISVIP_KEY, App.isVip);
                                        }
                                        App.isHeijin = 1;
                                        SharedPreferencesUtil.putInt(VideoDetailActivity.this, App.ISHEIJIN_KEY, App.isHeijin);
                                        message = "恭喜您成为最牛逼的黑金会员";
                                        CustomSubmitDialog customSubmitDialog4 = DialogUtil.getInstance().showCustomSubmitDialog(VideoDetailActivity.this, message);
                                        customSubmitDialog4.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialog) {
                                                closeVideoDetail(new CloseVideoDetailEvent());
                                            }
                                        });
                                        break;
                                    case 5:
                                        App.isVip = 6;
                                        SharedPreferencesUtil.putInt(VideoDetailActivity.this, App.ISVIP_KEY, App.isVip);
                                        message = "恭喜您开通海外高速通道";
                                        CustomSubmitDialog customSubmitDialog5 = DialogUtil.getInstance().showCustomSubmitDialog(VideoDetailActivity.this, message);
                                        customSubmitDialog5.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialog) {
                                                closeVideoDetail(new CloseVideoDetailEvent());
                                            }
                                        });
                                        break;
                                    case 6:
                                        App.isVip = 7;
                                        SharedPreferencesUtil.putInt(VideoDetailActivity.this, App.ISVIP_KEY, App.isVip);
                                        message = "恭喜您开通海外双线通道";
                                        CustomSubmitDialog customSubmitDialog6 = DialogUtil.getInstance().showCustomSubmitDialog(VideoDetailActivity.this, message);
                                        customSubmitDialog6.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialog) {
                                                closeVideoDetail(new CloseVideoDetailEvent());
                                            }
                                        });

                                        break;
                                    default:
                                        break;
                                }
                                App.isOPenBlackGlodVip = false;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }, 1000);

    }
}

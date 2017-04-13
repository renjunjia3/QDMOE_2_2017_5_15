package com.cyldf.cyldfxv;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.cyldf.cyldfxv.adapter.CommentAdapter;
import com.cyldf.cyldfxv.adapter.IndexItemAdapter;
import com.cyldf.cyldfxv.adapter.ScreenShotRecyclerViewAdapter;
import com.cyldf.cyldfxv.app.App;
import com.cyldf.cyldfxv.bean.CommentInfo;
import com.cyldf.cyldfxv.itemdecoration.ScreenShotItemDecoration;
import com.cyldf.cyldfxv.pay.PayUtil;
import com.cyldf.cyldfxv.ui.dialog.FullVideoPayDialog;
import com.cyldf.cyldfxv.ui.dialog.FunctionPayDialog;
import com.cyldf.cyldfxv.ui.dialog.SpeedPayDialog;
import com.cyldf.cyldfxv.ui.view.CustomListView;
import com.cyldf.cyldfxv.ui.view.CustomeGridView;
import com.cyldf.cyldfxv.util.API;
import com.cyldf.cyldfxv.util.NetWorkUtils;
import com.cyldf.cyldfxv.util.ScreenUtils;
import com.cyldf.cyldfxv.util.SharedPreferencesUtil;
import com.cyldf.cyldfxv.util.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import fm.jiecao.jcvideoplayer_lib.JCFullScreenActivity;
import fm.jiecao.jcvideoplayer_lib.VideoInfo;
import okhttp3.Call;
import wiki.scene.statuslib.StatusViewLayout;

/**
 * Case By:视频详情
 * package:com.cyldf.cyldfxv
 * Author：scene on 2017/4/13 10:02
 */
public class VideoDetailActivity extends AppCompatActivity {

    public static final String ARG_VIDEO_INFO = "arg_video_info";
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

    private Unbinder unbinder;

    private VideoInfo videoInfo;

    private String commentJSONStr;
    private List<CommentInfo> commentInfoList;
    private Random random;
    private CommentAdapter commentAdapter;

    //相关推荐
    private List<VideoInfo> videoRelateList = new ArrayList<>();
    private IndexItemAdapter videoRelateAdapter;


    //支付框
    //加速支付对话框
    private SpeedPayDialog speedPayDialog;
    private SpeedPayDialog.Builder speedPayDialogBuilder;
    private FullVideoPayDialog fullVideoDialog;
    private FullVideoPayDialog.Builder fullVideoDialogBuilder;
    private FunctionPayDialog functionPayDialog;
    private FunctionPayDialog.Builder functionPayDialogBuilder;

    //视频截图
    ScreenShotRecyclerViewAdapter screenShotRecyclerViewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_video_detail);
        unbinder = ButterKnife.bind(this);
        videoInfo = (VideoInfo) getIntent().getSerializableExtra(ARG_VIDEO_INFO);
        initToolbarNav(toolbar);
        initView();
        initDialog();
        if (videoInfo.getVip() == 1 && App.ISVIP != 1) {
            fullVideoDialog.show();
            clickWantPay();
        } else {
            if (App.ISVIP == 0 && App.TRY_COUNT >= 4) {
                fullVideoDialog.show();
                clickWantPay();
            }
        }
        if (App.ISVIP == 0) {
            ToastUtils.getInstance(this).showToast("剩余试看次数：" + (4 - App.TRY_COUNT > 0 ? 4 - App.TRY_COUNT : 0) + "次");
        }
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
        int count = random.nextInt(10) + 10;
        commentJSONStr = getString(R.string.str_comment_json);
        List<CommentInfo> tempCommentList = JSON.parseArray(commentJSONStr, CommentInfo.class);
        int tempListSize = tempCommentList.size();
        commentInfoList = new ArrayList<>();
        for (int i = 0; i < count && i < tempListSize; i++) {
            commentInfoList.add(tempCommentList.get(i));
        }
        commendNumber.setText((random.nextInt(1000) + 580) + "");
        toolbarTitle.setText(videoInfo.getTitle());
        zan.setText(videoInfo.getHits() + "");
        //评论列表
        commentAdapter = new CommentAdapter(this, commentInfoList);
        commentListView.setAdapter(commentAdapter);
        Glide.with(this).load(videoInfo.getImages().get(0)).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(detailPlayer);
        //相关推荐
        videoRelateAdapter = new IndexItemAdapter(VideoDetailActivity.this, videoRelateList);
        aboutCommendGridView.setAdapter(videoRelateAdapter);
        getRecomendVideo();
        //视频截图
        screenShotRecyclerViewAdapter = new ScreenShotRecyclerViewAdapter(VideoDetailActivity.this, videoInfo.getImages());
        screenShotRecyclerView.setHasFixedSize(true);
        screenShotRecyclerView.addItemDecoration(new ScreenShotItemDecoration((int) ScreenUtils.instance(VideoDetailActivity.this).dip2px(10)));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        screenShotRecyclerView.setLayoutManager(layoutManager);
        screenShotRecyclerView.setAdapter(screenShotRecyclerViewAdapter);
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }


    private void initDialog() {

        speedPayDialogBuilder = new SpeedPayDialog.Builder(VideoDetailActivity.this);
        speedPayDialogBuilder.setAliPayClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                PayUtil.getInstance().payByAliPay(VideoDetailActivity.this, 2, videoInfo.getId());
            }
        });
        speedPayDialogBuilder.setWeChatPayClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                PayUtil.getInstance().payByWeChat(VideoDetailActivity.this, 2, videoInfo.getId());
            }
        });

        speedPayDialog = speedPayDialogBuilder.create();

        fullVideoDialogBuilder = new FullVideoPayDialog.Builder(VideoDetailActivity.this);
        fullVideoDialogBuilder.setAliPayClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                PayUtil.getInstance().payByAliPay(VideoDetailActivity.this, 1, videoInfo.getId());
            }
        });
        fullVideoDialogBuilder.setWeChatPayClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                PayUtil.getInstance().payByWeChat(VideoDetailActivity.this, 1, videoInfo.getId());
            }
        });

        fullVideoDialog = fullVideoDialogBuilder.create();

        functionPayDialogBuilder = new FunctionPayDialog.Builder(VideoDetailActivity.this);
        functionPayDialogBuilder.setAliPayClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                PayUtil.getInstance().payByAliPay(VideoDetailActivity.this, 1, videoInfo.getId());

            }
        });
        functionPayDialogBuilder.setWeChatPayClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                PayUtil.getInstance().payByWeChat(VideoDetailActivity.this, 1, videoInfo.getId());
            }
        });

        functionPayDialog = functionPayDialogBuilder.create();

    }

    @OnClick({R.id.zan, R.id.fravetor, R.id.open_vip, R.id.addVip, R.id.open_vip1, R.id.sendComment, R.id.download, R.id.commend_number})
    public void onClick(View v) {
        if (App.ISVIP == 0) {
            if (v.getId() == R.id.open_vip1) {
                fullVideoDialog.show();
            } else {
                functionPayDialog.show();
            }
            clickWantPay();
        } else {
            if (v.getId() == R.id.open_vip1) {
                ToastUtils.getInstance(VideoDetailActivity.this).showToast("您已经是VIP了！");
            } else {
                ToastUtils.getInstance(VideoDetailActivity.this).showToast("该功能完善中，敬请期待");
            }
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
        if (videoInfo.getVip() == 1 && App.ISVIP == 0) {
            functionPayDialog.show();
            clickWantPay();
        } else if (App.ISVIP == 0 && App.TRY_COUNT >= 4) {
            functionPayDialog.show();
            clickWantPay();
        } else {
            App.TRY_COUNT += 1;
            Intent intent = new Intent(VideoDetailActivity.this, JCFullScreenActivity.class);
            intent.putExtra(JCFullScreenActivity.PARAM_VIDEO_INFO, videoInfo);
            intent.putExtra(JCFullScreenActivity.PARAM_CURRENT_TIME, currentTime);
            intent.putExtra(JCFullScreenActivity.PARAM_IS_VIP, App.ISVIP);
            intent.putExtra(JCFullScreenActivity.PARAM_IS_SPEED, App.ISSPEED);
            intent.putExtra(JCFullScreenActivity.PARAM_TRY_COUNT, App.TRY_COUNT);
            startActivityForResult(intent, 101);
            SharedPreferencesUtil.putInt(VideoDetailActivity.this, App.TRY_COUNT_KEY, App.TRY_COUNT);
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
                case JCFullScreenActivity.DIALOG_TYPE_SPEED:
                    speedPayDialog.show();
                    break;
                case JCFullScreenActivity.DIALOG_TYPE_FULLVIDEO:
                    fullVideoDialog.show();
                    break;
                case JCFullScreenActivity.DIALOG_TYPE_FUNCATION:
                    functionPayDialog.show();
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
            OkHttpUtils.get().url(API.URL_PRE + API.VIDEO_RELATED + videoInfo.getId()).build().execute(new StringCallback() {
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
}

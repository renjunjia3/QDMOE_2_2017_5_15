package com.cyldf.cyldfxv.ui.fragment.live;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.joooonho.SelectableRoundedImageView;
import com.cyldf.cyldfxv.R;
import com.cyldf.cyldfxv.adapter.LiveDetailAdapter;
import com.cyldf.cyldfxv.app.App;
import com.cyldf.cyldfxv.base.BaseFragment;
import com.cyldf.cyldfxv.bean.LiveInfo;
import com.cyldf.cyldfxv.pay.PayUtil;
import com.cyldf.cyldfxv.ui.dialog.LivePayDialog;
import com.cyldf.cyldfxv.ui.fragment.MainFragment;
import com.cyldf.cyldfxv.ui.view.NoTouchListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Case By:直播详情
 * package:com.yuuymd.kkskoo.ui.fragment.live
 * Author：scene on 2017/4/6 10:12
 */

public class LiveDetailFragment extends BaseFragment {
    public static final String PARAMS_LIVE_INFO = "live_info";

    @BindView(R.id.bg)
    ImageView bg;
    @BindView(R.id.head1)
    SelectableRoundedImageView head1;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.hits)
    TextView hits;
    @BindView(R.id.guanzhu)
    TextView guanzhu;
    @BindView(R.id.head2)
    SelectableRoundedImageView head2;
    @BindView(R.id.head3)
    SelectableRoundedImageView head3;
    @BindView(R.id.head4)
    SelectableRoundedImageView head4;
    @BindView(R.id.piao)
    TextView piao;
    @BindView(R.id.hao)
    TextView hao;
    @BindView(R.id.listview)
    NoTouchListView listView;

    private LiveDetailAdapter adapter;

    private LiveInfo liveInfo;
    private List<Integer> imageResIds = new ArrayList<>();

    private LivePayDialog dialog;
    private LivePayDialog.Builder builder;

    private int index = 0;

    private MediaPlayer mediaPlayer;
    private Timer timer;

    private List<Integer> videoResId = new ArrayList<>();

    public static LiveDetailFragment newInstance(LiveInfo liveInfo) {
        LiveDetailFragment fragment = new LiveDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(PARAMS_LIVE_INFO, liveInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            liveInfo = (LiveInfo) args.getSerializable(PARAMS_LIVE_INFO);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        initView();
    }

    private void initView() {
        imageResIds.add(R.drawable.head1);
        imageResIds.add(R.drawable.head3);
        imageResIds.add(R.drawable.head4);
        imageResIds.add(R.drawable.head5);
        imageResIds.add(R.drawable.head6);
        imageResIds.add(R.drawable.head7);
        imageResIds.add(R.drawable.head8);
        imageResIds.add(R.drawable.head10);
        imageResIds.add(R.drawable.head11);
        imageResIds.add(R.drawable.head12);
        imageResIds.add(R.drawable.head13);
        imageResIds.add(R.drawable.head16);
        imageResIds.add(R.drawable.head18);
        imageResIds.add(R.drawable.head19);
        Random random = new Random();

        Glide.with(this).load(liveInfo.getAvatar()).asBitmap().centerCrop().placeholder(R.drawable.ic_user_avatar).error(R.drawable.ic_user_avatar).into(head1);
        Glide.with(this).load(imageResIds.get(random.nextInt(13))).asBitmap().centerCrop().placeholder(R.drawable.ic_user_avatar).error(R.drawable.ic_user_avatar).into(head2);
        Glide.with(this).load(imageResIds.get(random.nextInt(13))).asBitmap().centerCrop().placeholder(R.drawable.ic_user_avatar).error(R.drawable.ic_user_avatar).into(head3);
        Glide.with(this).load(imageResIds.get(random.nextInt(13))).asBitmap().centerCrop().placeholder(R.drawable.ic_user_avatar).error(R.drawable.ic_user_avatar).into(head4);
        name.setText(liveInfo.getName());
        hits.setText(liveInfo.getHits());
        piao.setText("映票：" + (random.nextInt(5000) + 8541) + "\t>");
        hao.setText("映号：" + random.nextInt(8000) + random.nextInt(8000));

        adapter = new LiveDetailAdapter(_mActivity);
        listView.setAdapter(adapter);
        timer = new Timer();
        timer.schedule(new MyTask(), 1000, 1000);

        videoResId.add(R.raw.live_bg_1);
        videoResId.add(R.raw.live_bg_2);
        videoResId.add(R.raw.live_bg_3);
        videoResId.add(R.raw.live_bg_4);
        videoResId.add(R.raw.live_bg_5);

        Uri notification = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + videoResId.get(random.nextInt(5)));
        mediaPlayer = MediaPlayer.create(_mActivity, notification);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }


    class MyTask extends TimerTask {
        @Override
        public void run() {
            index++;
            if (index >= listView.getCount()) {
                index = 0;
            }
            if (index == 0) {
                listView.scrollBy(0, 0);
            } else {
                listView.smoothScrollToPosition(index);
            }
        }
    }

    @OnClick({R.id.msg, R.id.email, R.id.share, R.id.liwu, R.id.piao, R.id.hao, R.id.head1, R.id.head2, R.id.head3, R.id.head4, R.id.guanzhu, R.id.open_vip})
    public void onClickBtn() {
        if (App.ISVIP == 0) {
            if (builder == null) {
                builder = new LivePayDialog.Builder(_mActivity);
                builder.setWeChatPayClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        PayUtil.getInstance().payByWeChat(_mActivity, 1, 0);
                    }
                });

                builder.setAliPayClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PayUtil.getInstance().payByAliPay(_mActivity, 1, 0);
                    }
                });
            }
            if (dialog == null) {
                dialog = builder.create();
            }
            dialog.show();
            MainFragment.clickWantPay();
        } else {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(liveInfo.getUrl());
            intent.setData(content_url);
            startActivity(intent);
        }
    }

    @OnClick(R.id.close)
    public void onClickClose() {
        _mActivity.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    @Override
    public void onPause() {
        mediaPlayer.pause();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        listView.setAdapter(null);
        timer.cancel();
        mediaPlayer.stop();
        super.onDestroyView();
    }
}

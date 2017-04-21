package com.hfaufhreu.hjfeuio.util;

import android.content.Context;
import android.content.DialogInterface;

import com.hfaufhreu.hjfeuio.pay.PayUtil;
import com.hfaufhreu.hjfeuio.ui.dialog.AccelerationChannelVipDialog;
import com.hfaufhreu.hjfeuio.ui.dialog.BlackGlodVipDialog;
import com.hfaufhreu.hjfeuio.ui.dialog.DiamondVipDialog;
import com.hfaufhreu.hjfeuio.ui.dialog.GlodVipDialog;
import com.hfaufhreu.hjfeuio.ui.dialog.RapidDoubletVipDialog;
import com.hfaufhreu.hjfeuio.ui.dialog.SubmitAndCancelDialog;
import com.hfaufhreu.hjfeuio.ui.dialog.VpnFlimVipDialog;
import com.hfaufhreu.hjfeuio.ui.dialog.VpnVipDialog;

/**
 * Case By:对dialog的封装
 * package:com.hfaufhreu.hjfeuio.util
 * Author：scene on 2017/4/20 18:39
 */

public class DialogUtil {

    private static DialogUtil instance = null;

    //确定取消的对话框
    private SubmitAndCancelDialog submitAndCancelDialog;
    private SubmitAndCancelDialog.Builder submitAndCancelDialogBuilder;
    //开通黄金或者砖石的对话框
    private GlodVipDialog glodVipDialog;
    private GlodVipDialog.Builder glodVipDialogBuilder;
    //升级钻石的对话框
    private DiamondVipDialog diamondVipDialog;
    private DiamondVipDialog.Builder diamondVipDialogBuilder;
    //开通海外VPN的dialog
    private VpnVipDialog vpnVipDialog;
    private VpnVipDialog.Builder vpnVipDialogBuilder;
    //开通海外片库
    private VpnFlimVipDialog vpnFlimVipDialog;
    private VpnFlimVipDialog.Builder vpnFlimVipDialogBuilder;
    //黑金会员
    private BlackGlodVipDialog blackGlodVipDialog;
    private BlackGlodVipDialog.Builder blackGlodVipDialogBuilder;
    //海外加速
    private AccelerationChannelVipDialog accelerationChannelVipDialog;
    private AccelerationChannelVipDialog.Builder accelerationChannelVipDialogBuilder;
    //海外双线
    private RapidDoubletVipDialog rapidDoubletVipDialog;
    private RapidDoubletVipDialog.Builder rapidDoubletVipDialogBuilder;

    public static DialogUtil getInstance() {
        if (instance == null) {
            synchronized (PayUtil.class) {
                if (instance == null) {
                    instance = new DialogUtil();
                }
            }
        }
        return instance;
    }

    /**
     * Case By:
     * Author: scene on 2017/4/20 19:28
     *
     * @param context          上下文
     * @param isCancelAsSubmit 是否把取消按钮当做确定按钮
     * @param message          显示的文字内容
     * @param myVipType        我的VIP类型 App.isVip
     * @param isShowOpenVip    是否进入显示开通会员的对话框
     */
    public void showSubmitDialog(Context context, boolean isCancelAsSubmit, String message, int myVipType, final boolean isShowOpenVip) {
        showSubmitDialog(context, isCancelAsSubmit, message, myVipType, isShowOpenVip, 0);
    }

    /**
     * Case By:
     * Author: scene on 2017/4/20 19:28
     *
     * @param context          上下文
     * @param isCancelAsSubmit 是否把取消按钮当做确定按钮
     * @param message          显示的文字内容
     * @param myVipType        我的VIP类型 App.isVip
     * @param isShowOpenVip    是否进入显示开通会员的对话框
     * @param videoId          视频Id
     */
    public void showSubmitDialog(final Context context, final boolean isCancelAsSubmit, String message, final int myVipType, final boolean isShowOpenVip, final int videoId) {

        if (submitAndCancelDialog != null && submitAndCancelDialog.isShowing()) {
            submitAndCancelDialog.cancel();
        }
        submitAndCancelDialogBuilder = new SubmitAndCancelDialog.Builder(context);
        submitAndCancelDialogBuilder.setMessage(message);

        submitAndCancelDialogBuilder.setSubmitButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                submitAndCancelDialog.dismiss();
                if (isShowOpenVip) {
                    showVipDialog(context, myVipType, videoId);
                }
            }
        });

        submitAndCancelDialogBuilder.setCancelButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                submitAndCancelDialog.dismiss();
                if (isShowOpenVip && isCancelAsSubmit) {
                    showVipDialog(context, myVipType, videoId);
                }
            }
        });
        submitAndCancelDialog = submitAndCancelDialogBuilder.create();
        submitAndCancelDialog.show();
    }

    /**
     * Case By:显示某一个开通VIp的Dialog
     * Author: scene on 2017/4/20 19:26
     *
     * @param context   上下文
     * @param myVipType 我的VIP类型 App.isVip
     * @param videoId   视频Id
     */
    private void showVipDialog(Context context, int myVipType, int videoId) {
        switch (myVipType) {
            case 0://开通黄金会员
                showGoldVipDialog(context, videoId, videoId != 0 ? true : false);
                break;
            case 1://开通砖石会员
                showDiamondVipDialog(context, videoId, videoId != 0 ? true : false);
                break;
            case 2://当前VPN会员
                showVpnVipDialog(context, videoId, videoId != 0 ? true : false);
                break;
            case 3://开通海外片库
                showVpnFlimVipDialog(context, videoId, videoId != 0 ? true : false);
                break;
            case 4://升级黑金会员
                showBlackGlodVipDialog(context, videoId, videoId != 0 ? true : false);
                break;
            case 5://开通海外加速
                showAccelerationChannelVipDialog(context, videoId, videoId != 0 ? true : false);
                break;
            case 6://海外双线
                showRapidDoubletVipDialog(context, videoId, videoId != 0 ? true : false);
                break;
        }
    }

    /**
     * Case By:显示开通黄金或者砖石会员的dialog
     * Author: scene on 2017/4/20 19:27
     *
     * @param context           上下文
     * @param videoId           视频id
     * @param isVideoDetailPage 是否视频详情页进来的
     */
    public void showGoldVipDialog(Context context, int videoId, boolean isVideoDetailPage) {
        if (glodVipDialog != null && glodVipDialog.isShowing()) {
            glodVipDialog.cancel();
        }
        glodVipDialogBuilder = new GlodVipDialog.Builder(context, videoId, isVideoDetailPage);
        glodVipDialog = glodVipDialogBuilder.create();
        glodVipDialog.show();
    }

    /**
     * Case By:显示升级砖石会员的dialog
     * Author: scene on 2017/4/20 19:27
     *
     * @param context           上下文
     * @param videoId           视频id
     * @param isVideoDetailPage 是否视频详情页进来的
     */
    public void showDiamondVipDialog(Context context, int videoId, boolean isVideoDetailPage) {
        if (diamondVipDialog != null && diamondVipDialog.isShowing()) {
            diamondVipDialog.cancel();
        }
        diamondVipDialogBuilder = new DiamondVipDialog.Builder(context, videoId, isVideoDetailPage);
        diamondVipDialog = diamondVipDialogBuilder.create();
        diamondVipDialog.show();
    }

    /**
     * Case By:显示开通VPN会员的dialog
     * Author: scene on 2017/4/20 19:27
     *
     * @param context           上下文
     * @param videoId           视频id
     * @param isVideoDetailPage 是否视频详情页进来的
     */
    public void showVpnVipDialog(Context context, int videoId, boolean isVideoDetailPage) {
        if (vpnVipDialog != null && vpnVipDialog.isShowing()) {
            vpnVipDialog.cancel();
        }
        vpnVipDialogBuilder = new VpnVipDialog.Builder(context, videoId, isVideoDetailPage);
        vpnVipDialog = vpnVipDialogBuilder.create();
        vpnVipDialog.show();
    }

    /**
     * Case By:显示开通海外片库的dialog
     * Author: scene on 2017/4/20 19:27
     *
     * @param context           上下文
     * @param videoId           视频id
     * @param isVideoDetailPage 是否视频详情页进来的
     */
    public void showVpnFlimVipDialog(Context context, int videoId, boolean isVideoDetailPage) {
        if (vpnFlimVipDialog != null && vpnFlimVipDialog.isShowing()) {
            vpnFlimVipDialog.cancel();
        }
        vpnFlimVipDialogBuilder = new VpnFlimVipDialog.Builder(context, videoId, isVideoDetailPage);
        vpnFlimVipDialog = vpnFlimVipDialogBuilder.create();
        vpnFlimVipDialog.show();
    }

    /**
     * Case By:显示开通海外片库的dialog
     * Author: scene on 2017/4/20 19:27
     *
     * @param context           上下文
     * @param videoId           视频id
     * @param isVideoDetailPage 是否视频详情页进来的
     */
    public void showBlackGlodVipDialog(Context context, int videoId, boolean isVideoDetailPage) {
        if (blackGlodVipDialog != null && blackGlodVipDialog.isShowing()) {
            blackGlodVipDialog.cancel();
        }
        blackGlodVipDialogBuilder = new BlackGlodVipDialog.Builder(context, videoId, isVideoDetailPage);
        blackGlodVipDialog = blackGlodVipDialogBuilder.create();
        blackGlodVipDialog.show();
    }

    /**
     * Case By:显示开通海外片库的dialog
     * Author: scene on 2017/4/20 19:27
     *
     * @param context           上下文
     * @param videoId           视频id
     * @param isVideoDetailPage 是否视频详情页进来的
     */
    public void showAccelerationChannelVipDialog(Context context, int videoId, boolean isVideoDetailPage) {
        if (accelerationChannelVipDialog != null && accelerationChannelVipDialog.isShowing()) {
            accelerationChannelVipDialog.cancel();
        }
        accelerationChannelVipDialogBuilder = new AccelerationChannelVipDialog.Builder(context, videoId, isVideoDetailPage);
        accelerationChannelVipDialog = accelerationChannelVipDialogBuilder.create();
        accelerationChannelVipDialog.show();
    }

    /**
     * Case By:显示开通海外片库的dialog
     * Author: scene on 2017/4/20 19:27
     *
     * @param context           上下文
     * @param videoId           视频id
     * @param isVideoDetailPage 是否视频详情页进来的
     */
    public void showRapidDoubletVipDialog(Context context, int videoId, boolean isVideoDetailPage) {
        if (rapidDoubletVipDialog != null && rapidDoubletVipDialog.isShowing()) {
            rapidDoubletVipDialog.cancel();
        }
        rapidDoubletVipDialogBuilder = new RapidDoubletVipDialog.Builder(context, videoId, isVideoDetailPage);
        rapidDoubletVipDialog = rapidDoubletVipDialogBuilder.create();
        rapidDoubletVipDialog.show();
    }
}

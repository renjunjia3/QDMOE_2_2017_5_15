package com.mzhguqvn.mzhguq.util;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.mzhguqvn.mzhguq.MainActivity;
import com.mzhguqvn.mzhguq.config.PageConfig;
import com.mzhguqvn.mzhguq.pay.PayUtil;
import com.mzhguqvn.mzhguq.ui.dialog.BackGlodVipDialog;
import com.mzhguqvn.mzhguq.ui.dialog.CDNVipDialog;
import com.mzhguqvn.mzhguq.ui.dialog.CustomSubmitDialog;
import com.mzhguqvn.mzhguq.ui.dialog.DiamondVipDialog;
import com.mzhguqvn.mzhguq.ui.dialog.GlodVipDialog;
import com.mzhguqvn.mzhguq.ui.dialog.OpenVipNoticeDialog;
import com.mzhguqvn.mzhguq.ui.dialog.SubmitAndCancelDialog;
import com.mzhguqvn.mzhguq.ui.dialog.WxQRCodePayDialog;
import com.mzhguqvn.mzhguq.ui.fragment.MainFragment;

/**
 * Case By:对dialog的封装
 * package:com.hfaufhreu.hjfeuio.util
 * Author：scene on 2017/4/20 18:39
 */

public class DialogUtil {

    private static DialogUtil instance = null;

    //只有确定的对话框
    private CustomSubmitDialog customSubmitDialog;
    private CustomSubmitDialog.Builder customSubmitDialgBuiler;
    //成功开通会员的弹窗
    private OpenVipNoticeDialog openVipNoticeDialog;
    private OpenVipNoticeDialog.Builder openVipNoticeDialogBuidler;

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
    private CDNVipDialog vpnVipDialog;
    private CDNVipDialog.Builder vpnVipDialogBuilder;
    //优惠开通黄金会员
    private BackGlodVipDialog backGlodVipDialog;
    //微信二维码
    private WxQRCodePayDialog wxQRCodePayDialog;

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
    public void showSubmitDialog(Context context, boolean isCancelAsSubmit, String message, int myVipType, final boolean isShowOpenVip, int pay_position_id) {
        showSubmitDialog(context, isCancelAsSubmit, message, myVipType, isShowOpenVip, 0, pay_position_id, false);
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
    public void showSubmitDialog(Context context, boolean isCancelAsSubmit, String message,
                                 int myVipType, final boolean isShowOpenVip, int pay_position_id, boolean isOPenDiamond) {
        showSubmitDialog(context, isCancelAsSubmit, message, myVipType, isShowOpenVip, 0, pay_position_id, isOPenDiamond);
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
    public void showSubmitDialog(final Context context, final boolean isCancelAsSubmit, String message, final int myVipType, final boolean isShowOpenVip, final int videoId, final int pay_position_id, final boolean isOPenDiamond) {

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
                    showVipDialog(context, myVipType, videoId, pay_position_id, isOPenDiamond);
                }

            }
        });

        submitAndCancelDialogBuilder.setCancelButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                submitAndCancelDialog.dismiss();
                if (isShowOpenVip && isCancelAsSubmit) {
                    showVipDialog(context, myVipType, videoId, pay_position_id, isOPenDiamond);
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
    private void showVipDialog(Context context, int myVipType, int videoId, int pay_position_id, boolean isOPenDiamond) {
        if (isOPenDiamond) {
            showDiamondVipDialog(context, videoId, pay_position_id);
        } else {
            switch (myVipType) {
                case 0://开通黄金会员
                    showGoldVipDialog(context, videoId, pay_position_id);
                    break;
                case 1://开通砖石会员
                    showDiamondVipDialog(context, videoId, pay_position_id);
                    break;
                case 2://开通cdn服务
                    showCdnVipDialog(context, videoId, pay_position_id);
                    break;
            }
        }

    }

    /**
     * Case By:显示开通黄金或者砖石会员的dialog
     * Author: scene on 2017/4/20 19:27
     *
     * @param context 上下文
     * @param videoId 视频id
     */
    public void showGoldVipDialog(Context context, int videoId, int pay_position_id) {
        if (glodVipDialog != null && glodVipDialog.isShowing()) {
            glodVipDialog.cancel();
        }
        glodVipDialogBuilder = new GlodVipDialog.Builder(context, videoId, pay_position_id);
        glodVipDialog = glodVipDialogBuilder.create();
        glodVipDialog.show();
        MainFragment.clickWantPay();
        MainActivity.upLoadPageInfo(PageConfig.OPEN_GLOD_VIP_DIALOG_POSITOTN_ID, videoId, pay_position_id);
    }

    /**
     * Case By:显示升级砖石会员的dialog
     * Author: scene on 2017/4/20 19:27
     *
     * @param context 上下文
     * @param videoId 视频id
     */
    public void showDiamondVipDialog(Context context, int videoId, int pay_position_id) {
        if (diamondVipDialog != null && diamondVipDialog.isShowing()) {
            diamondVipDialog.cancel();
        }
        diamondVipDialogBuilder = new DiamondVipDialog.Builder(context, videoId, pay_position_id);
        diamondVipDialog = diamondVipDialogBuilder.create();
        diamondVipDialog.show();
        MainFragment.clickWantPay();
        MainActivity.upLoadPageInfo(PageConfig.OPEN_DIAMOND_VIP_POSITOTN_ID, videoId, pay_position_id);
    }

    /**
     * Case By:显示开通CDN会员的dialog
     * Author: scene on 2017/4/20 19:27
     *
     * @param context 上下文
     * @param videoId 视频id
     */
    public void showCdnVipDialog(Context context, int videoId, int pay_position_id) {
        if (vpnVipDialog != null && vpnVipDialog.isShowing()) {
            vpnVipDialog.cancel();
        }
        vpnVipDialogBuilder = new CDNVipDialog.Builder(context, videoId, pay_position_id);
        vpnVipDialog = vpnVipDialogBuilder.create();
        vpnVipDialog.show();
        MainFragment.clickWantPay();
        MainActivity.upLoadPageInfo(PageConfig.OPEN_CDN_POSITOTN_ID, videoId, pay_position_id);
    }


    /**
     * Case By:显示只有确定按钮的dialog
     * Author: scene on 2017/4/25 16:43
     *
     * @param context  上下文
     * @param message1 文字内容
     */
    public OpenVipNoticeDialog showOpenVipNoticeDialog(Context context, String message1, String message2) {
        if (openVipNoticeDialog != null && openVipNoticeDialog.isShowing()) {
            openVipNoticeDialog.cancel();
        }
        openVipNoticeDialogBuidler = new OpenVipNoticeDialog.Builder(context);
        openVipNoticeDialogBuidler.setMessage1(message1);
        openVipNoticeDialogBuidler.setMessage2(message2);
        openVipNoticeDialogBuidler.setButtonText(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (openVipNoticeDialog != null && openVipNoticeDialog.isShowing()) {
                    openVipNoticeDialog.cancel();
                }
            }
        });
        openVipNoticeDialog = openVipNoticeDialogBuidler.create();
        openVipNoticeDialog.show();
        return openVipNoticeDialog;
    }


    /**
     * Case By:开通会员之后的提示框
     * Author: scene on 2017/4/25 16:43
     *
     * @param context 上下文
     * @param message 文字内容
     */
    public CustomSubmitDialog showCustomSubmitDialog(Context context, String message) {
        if (customSubmitDialog != null && customSubmitDialog.isShowing()) {
            customSubmitDialog.cancel();
        }
        customSubmitDialgBuiler = new CustomSubmitDialog.Builder(context);
        customSubmitDialgBuiler.setMessage(message);
        customSubmitDialgBuiler.setButtonText("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (customSubmitDialog != null && customSubmitDialog.isShowing()) {
                    customSubmitDialog.cancel();
                }
            }
        });
        customSubmitDialog = customSubmitDialgBuiler.create();
        customSubmitDialog.show();
        return customSubmitDialog;
    }

    /**
     * 优惠开通会员
     *
     * @param context
     * @return
     */
    public BackGlodVipDialog showBackGlodVipDialog(Context context) {
        if (backGlodVipDialog != null && backGlodVipDialog.isShowing()) {
            backGlodVipDialog.cancel();
        }
        BackGlodVipDialog.Builder builder = new BackGlodVipDialog.Builder(context);
        backGlodVipDialog = builder.create();
        backGlodVipDialog.show();
        MainFragment.clickWantPay();
        MainActivity.upLoadPageInfo(PageConfig.BACK_OPEN_VIP_POSITOTN_ID, 0, 0);
        return backGlodVipDialog;
    }

    public WxQRCodePayDialog showWxQRCodePayDialog(Context context, String qrCodeUrl) {
        WxQRCodePayDialog.Builder builder = new WxQRCodePayDialog.Builder(context, qrCodeUrl);
        wxQRCodePayDialog = builder.create();
        wxQRCodePayDialog.show();
        return wxQRCodePayDialog;
    }

    /**
     * Case By:关闭所有的dialog
     * Author: scene on 2017/4/24 9:34
     */
    public void cancelAllDialog() {
        try {
            if (submitAndCancelDialog != null && submitAndCancelDialog.isShowing()) {
                submitAndCancelDialog.cancel();
            }
            if (glodVipDialog != null && glodVipDialog.isShowing()) {
                glodVipDialog.cancel();
            }
            if (diamondVipDialog != null && diamondVipDialog.isShowing()) {
                diamondVipDialog.cancel();
            }
            if (vpnVipDialog != null && vpnVipDialog.isShowing()) {
                vpnVipDialog.cancel();
            }
            if (openVipNoticeDialog != null && openVipNoticeDialog.isShowing()) {
                openVipNoticeDialog.cancel();
            }
            if (backGlodVipDialog != null && backGlodVipDialog.isShowing()) {
                backGlodVipDialog.cancel();
            }
            if (wxQRCodePayDialog != null && wxQRCodePayDialog.isShowing()) {
                wxQRCodePayDialog.cancel();
            }

        } catch (Exception e) {
            Log.e("DialogUtil", "对话框关闭异常");
        }
    }


}

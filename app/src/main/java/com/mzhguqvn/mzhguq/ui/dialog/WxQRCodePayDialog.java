package com.mzhguqvn.mzhguq.ui.dialog;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.util.DownLoadImageService;
import com.mzhguqvn.mzhguq.util.ImageDownLoadCallBack;
import com.mzhguqvn.mzhguq.util.ToastUtils;

import java.io.File;
import java.util.List;


/**
 * Case By:VPN海外会员
 * package:
 * Author：scene on 2017/4/18 13:53
 */
public class WxQRCodePayDialog extends Dialog {
    public WxQRCodePayDialog(@NonNull Context context) {
        super(context);
    }

    public WxQRCodePayDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected WxQRCodePayDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        private Context context;
        private String url;
        ImageView imageView;

        public Builder(Context context, String url) {
            this.context = context;
            this.url = url;
        }

        public WxQRCodePayDialog create() {
            LayoutInflater inflater = LayoutInflater.from(context);
            final WxQRCodePayDialog dialog = new WxQRCodePayDialog(context, R.style.Dialog);

            View layout = inflater.inflate(R.layout.dialog_wx_qr_code_pay, null);

            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            imageView = (ImageView) layout.findViewById(R.id.image);
            layout.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog != null) {
                        dialog.cancel();
                    }
                }
            });

            layout.findViewById(R.id.toWeChat).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (isWeixinAvilible(context)) {
//                            Intent intent = new Intent();
//                            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
//                            intent.setAction(Intent.ACTION_MAIN);
//                            intent.addCategory(Intent.CATEGORY_LAUNCHER);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent.setComponent(cmp);
//                            context.startActivity(intent);
                            Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
                            context.startActivity(intent);
                            if (dialog != null) {
                                dialog.cancel();
                            }
                        } else {
                            ToastUtils.getInstance(context).showToast("请先安装微信");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtils.getInstance(context).showToast("请先安装微信");
                    }

                }
            });
            Glide.with(context).load(url).asBitmap().centerCrop().into(imageView);
            DownLoadImageService service = new DownLoadImageService(context, url, new ImageDownLoadCallBack() {

                @Override
                public void onDownLoadSuccess(File file) {

                }

                @Override
                public void onDownLoadSuccess(Bitmap bitmap) {
                }

                @Override
                public void onDownLoadFailed() {

                }
            });
            //启动图片下载线程
            new Thread(service).start();
            dialog.setContentView(layout);
            dialog.setCanceledOnTouchOutside(false);
            return dialog;
        }

    }


    public static boolean isWeixinAvilible(Context context) {
        PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

}

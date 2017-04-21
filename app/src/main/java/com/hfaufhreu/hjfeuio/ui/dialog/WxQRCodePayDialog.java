package com.hfaufhreu.hjfeuio.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hfaufhreu.hjfeuio.R;
import com.hfaufhreu.hjfeuio.pay.PayUtil;
import com.hfaufhreu.hjfeuio.util.ScreenUtils;


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
        private int videoId;
        private boolean isVideoDetailPage;

        private int type = 1;

        public Builder(Context context, int videoId, boolean isVideoDetailPage) {
            this.context = context;
            this.videoId = videoId;
            this.isVideoDetailPage = isVideoDetailPage;
        }

        public WxQRCodePayDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final WxQRCodePayDialog dialog = new WxQRCodePayDialog(context, R.style.Dialog);

            View layout = inflater.inflate(R.layout.dialog_vpn_vip, null);

            ((TextView) layout.findViewById(R.id.diamond_old_price)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


            dialog.setContentView(layout);
            dialog.setCanceledOnTouchOutside(false);
            return dialog;
        }
    }

}

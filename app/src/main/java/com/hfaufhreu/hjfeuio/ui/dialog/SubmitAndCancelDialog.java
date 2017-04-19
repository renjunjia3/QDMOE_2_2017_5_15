package com.hfaufhreu.hjfeuio.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.hfaufhreu.hjfeuio.R;
import com.hfaufhreu.hjfeuio.util.ScreenUtils;
import com.hfaufhreu.hjfeuio.util.ViewUtils;


public class SubmitAndCancelDialog extends Dialog {

    public SubmitAndCancelDialog(Context context) {
        super(context);
    }

    public SubmitAndCancelDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String message;
        private String submitButtonText;
        private String cancelButtomText;
        private OnClickListener submitButtonClickListener;
        private OnClickListener cancelButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setSubmitButton(String buttonText, OnClickListener listener) {
            this.submitButtonText = buttonText;
            this.submitButtonClickListener = listener;
            return this;
        }

        public Builder setCancelButton(String buttonText, OnClickListener listener) {
            this.cancelButtomText = buttonText;
            this.cancelButtonClickListener = listener;
            return this;
        }

        public SubmitAndCancelDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final SubmitAndCancelDialog dialog = new SubmitAndCancelDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_submit_cancel, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            ((TextView) layout.findViewById(R.id.submit)).setText(submitButtonText);
            ((TextView) layout.findViewById(R.id.cancel)).setText(cancelButtomText);
            ViewUtils.setViewHeightByViewGroup(layout, (int) (ScreenUtils.instance(context).getScreenWidth() * 0.9f));

            layout.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (submitButtonClickListener != null) {
                        submitButtonClickListener.onClick(dialog,
                                DialogInterface.BUTTON_POSITIVE);
                    }
                }
            });
            layout.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (cancelButtonClickListener != null) {
                        cancelButtonClickListener.onClick(dialog,
                                DialogInterface.BUTTON_NEGATIVE);
                    }
                }
            });

            ((TextView) layout.findViewById(R.id.content)).setText(message);
            dialog.setContentView(layout);
            dialog.setCanceledOnTouchOutside(false);
            return dialog;
        }
    }
}  
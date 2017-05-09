package com.mzhguqvn.mzhguq.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.util.ScreenUtils;
import com.mzhguqvn.mzhguq.util.ViewUtils;


public class CustomSubmitDialog extends Dialog {

    public CustomSubmitDialog(Context context) {
        super(context);
    }

    public CustomSubmitDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String message;
        private String buttonText;
        private DialogInterface.OnClickListener positiveButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }


        public Builder setButtonText(String buttonText, DialogInterface.OnClickListener listener) {
            this.buttonText = buttonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public CustomSubmitDialog create() {
            LayoutInflater inflater = LayoutInflater.from(context);
            final CustomSubmitDialog dialog = new CustomSubmitDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_submit, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            ((TextView) layout.findViewById(R.id.submit)).setText(buttonText);
            ViewUtils.setViewHeightByViewGroup(layout, (int) (ScreenUtils.instance(context).getScreenWidth() * 0.9f));

            if (positiveButtonClickListener != null) {
                layout.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        positiveButtonClickListener.onClick(dialog,
                                DialogInterface.BUTTON_POSITIVE);
                    }
                });
            }
            ((TextView) layout.findViewById(R.id.content)).setText(message);
            dialog.setContentView(layout);
            dialog.setCanceledOnTouchOutside(false);
            return dialog;
        }
    }
}  
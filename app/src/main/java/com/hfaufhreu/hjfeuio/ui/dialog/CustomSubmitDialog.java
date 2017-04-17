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


public class CustomSubmitDialog extends Dialog {

    public CustomSubmitDialog(Context context) {
        super(context);
    }

    public CustomSubmitDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String message;
        private String buttonText;
        private View contentView;
        private DialogInterface.OnClickListener positiveButtonClickListener;
        private DialogInterface.OnClickListener negativeButtonClickListener;

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


        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }


        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }


        public Builder setButtonText(int buttonTextId, DialogInterface.OnClickListener listener) {
            this.buttonText = (String) context.getText(buttonTextId);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setButtonText(String buttonText, DialogInterface.OnClickListener listener) {
            this.buttonText = buttonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public CustomSubmitDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CustomSubmitDialog dialog = new CustomSubmitDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_submit, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            ((TextView) layout.findViewById(R.id.title)).setText(title);
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
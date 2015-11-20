package ingbank.com.tr.happybanking.common.ui.controls;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import ingbank.com.tr.happybanking.R;


public class ProgressHUD extends Dialog {
    public ProgressHUD(Context context) {
        super(context);
    }

    public ProgressHUD(Context context, int theme) {
        super(context, theme);
    }

    public static ProgressHUD create(Context context) {
        ProgressHUD dialog = new ProgressHUD(context, R.style.ProgressHUD);
        dialog.setTitle("");
        dialog.setContentView(R.layout.progress_hud);
        return dialog;
    }

    public static void show(Context context, CharSequence message, boolean indeterminate, boolean cancelable,
                            OnCancelListener cancelListener) {
        ProgressHUD dialog = ProgressHUD.create(context);
        if (message == null || message.length() == 0) {
            dialog.findViewById(R.id.message).setVisibility(View.GONE);
        } else {
            TextView txt = (TextView) dialog.findViewById(R.id.message);
            txt.setText(message);
        }
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.2f;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
//        return dialog;
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        ImageView imageView = (ImageView) findViewById(R.id.spinnerImageView);
        AnimationDrawable spinner = (AnimationDrawable) imageView.getBackground();
        spinner.start();
    }

    public void setMessage(CharSequence message) {
        if (message != null && message.length() > 0) {
            findViewById(R.id.message).setVisibility(View.VISIBLE);
            TextView txt = (TextView) findViewById(R.id.message);
            txt.setText(message);
            txt.invalidate();
        }
    }

    public void show(CharSequence message, boolean indeterminate, boolean cancelable,
                     OnCancelListener cancelListener) {
        if (message == null || message.length() == 0) {
            this.findViewById(R.id.message).setVisibility(View.GONE);
        } else {
            TextView txt = (TextView) this.findViewById(R.id.message);
            txt.setText(message);
        }
        this.setCancelable(cancelable);
        this.setOnCancelListener(cancelListener);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.dimAmount = 0.2f;
        this.getWindow().setAttributes(lp);
        this.show();
    }


}
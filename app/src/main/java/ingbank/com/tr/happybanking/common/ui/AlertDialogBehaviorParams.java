package ingbank.com.tr.happybanking.common.ui;


import android.content.DialogInterface;

public class AlertDialogBehaviorParams {
    private String mText;
    private DialogInterface.OnClickListener mOnClickListener;

    public AlertDialogBehaviorParams(String text, DialogInterface.OnClickListener onClickListener) {
        this.mText = text;
        this.mOnClickListener = onClickListener;

    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        this.mText = text;
    }

    public DialogInterface.OnClickListener getOnClickListener() {
        return mOnClickListener;
    }

    public void setOnClickListener(DialogInterface.OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }
}

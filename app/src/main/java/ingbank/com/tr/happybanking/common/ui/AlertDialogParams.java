package ingbank.com.tr.happybanking.common.ui;


public class AlertDialogParams {
    public static final boolean CANCELLABLE = true;
    public static final boolean NOT_CANCELLABLE = false;

    private String mTitle;
    private String mMessage;
    private AlertDialogBehaviorParams mPositiveBehavior;
    private AlertDialogBehaviorParams mNegativeBehavior;
    private boolean mCancellable;


    public AlertDialogParams(String title, String message, AlertDialogBehaviorParams positive, AlertDialogBehaviorParams negative, boolean cancellable) {
        this.mTitle = title;
        this.mMessage = message;
        this.mPositiveBehavior = positive;
        this.mNegativeBehavior = negative;
        this.mCancellable = cancellable;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getMessage() {
        return mMessage;
    }

    public AlertDialogBehaviorParams getPositiveBehavior() {
        return this.mPositiveBehavior;
    }

    public AlertDialogBehaviorParams getNegativeBehavior() {
        return this.mNegativeBehavior;
    }


    public boolean isCancellable() {
        return mCancellable;
    }

    public void setCancellable(boolean mCancellable) {
        this.mCancellable = mCancellable;
    }
}

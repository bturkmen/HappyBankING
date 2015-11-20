package ingbank.com.tr.happybanking.common.ui;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Button;

import ingbank.com.tr.happybanking.common.ui.controls.ProgressHUD;

public class BaseUIUtility {

    private Context mContext;
    private AlertDialog.Builder mAlertDialogBuilder;
    private ProgressHUD mProgressDialog;
    private AlertDialog mAlertDialog;


    public BaseUIUtility(Context context) {
        this.mContext = context;
        this.mAlertDialogBuilder = new AlertDialog.Builder(this.mContext);
        this.mProgressDialog = ProgressHUD.create(this.mContext);
    }

    public void onDestroy() {
        if (this.mAlertDialog != null) {
            this.mAlertDialog.dismiss();
            this.mAlertDialog = null;
        }
    }

    public String getResourceString(String name) {
        int nameResourceID = mContext.getResources().getIdentifier(name, "string", mContext.getApplicationInfo().packageName);
        if (nameResourceID == 0) {
            throw new IllegalArgumentException("No resource string found with name " + name);
        } else {
            return mContext.getString(nameResourceID);
        }
    }


    public String returnErrorCodeToString(int errorCode) {
        try {
            return this.getResourceString("ERROR_" + Integer.toString(errorCode));
        } catch (Exception exp) {
            return this.getResourceString("ERROR_default");
        }
    }

    /**
     * @return Network is available or not.
     */
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public String getStringResource(int resourceId) {
        return mContext.getResources().getString(resourceId);
    }

    public Drawable getDrawableResource(int resourceId) {
        return mContext.getResources().getDrawable(resourceId);
    }

    public AlertDialog.Builder getAlertDialogBuilder() {
        return mAlertDialogBuilder;
    }

    public void setAlertDialogBuilder(AlertDialog.Builder mAlertDialogBuilder) {
        this.mAlertDialogBuilder = mAlertDialogBuilder;
    }

    public AlertDialog createAlertDialog(AlertDialogParams alertDialogParams) {
        this.mAlertDialogBuilder.setTitle(alertDialogParams.getTitle());
        AlertDialog.Builder builder = this.mAlertDialogBuilder
                .setMessage(alertDialogParams.getMessage())
                .setCancelable(alertDialogParams.isCancellable());
        if (alertDialogParams.getPositiveBehavior() != null)
            builder.setPositiveButton(alertDialogParams.getPositiveBehavior().getText(), alertDialogParams.getPositiveBehavior().getOnClickListener());

        if (alertDialogParams.getNegativeBehavior() != null)
            builder.setPositiveButton(alertDialogParams.getNegativeBehavior().getText(), alertDialogParams.getNegativeBehavior().getOnClickListener());

        mAlertDialog = mAlertDialogBuilder.create();
        return mAlertDialog;
    }

    public AlertDialog createAlertDialog(String title, String message, String positiveButtonText, DialogInterface.OnClickListener positiveListener, boolean cancellable) {
        this.mAlertDialogBuilder = (new AlertDialog.Builder(this.mContext));
        AlertDialog.Builder builder = mAlertDialogBuilder.setTitle(title).setMessage(message)
                .setCancelable(cancellable).
                        setPositiveButton(positiveButtonText, positiveListener);
        mAlertDialog = builder.create();

        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positiveButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setFocusable(false);
                positiveButton.setFocusableInTouchMode(false);
            }
        });

        return mAlertDialog;
    }


    public AlertDialog createAlertDialog(int title, int message, int positiveButtonText, DialogInterface.OnClickListener positiveListener, boolean cancellable) {
        this.mAlertDialogBuilder = (new AlertDialog.Builder(this.mContext));
        AlertDialog.Builder builder = this.mAlertDialogBuilder.setTitle(title)
                .setMessage(message)
                .setCancelable(cancellable).
                        setPositiveButton(positiveButtonText, positiveListener);
        mAlertDialog = builder.create();
        return mAlertDialog;
    }

    public AlertDialog createAlertDialog(int message, int positiveButtonText, DialogInterface.OnClickListener positiveListener, boolean cancellable) {
        this.mAlertDialogBuilder = (new AlertDialog.Builder(this.mContext));
        AlertDialog.Builder builder = this.mAlertDialogBuilder
                .setMessage(message)
                .setCancelable(cancellable).
                        setPositiveButton(positiveButtonText, positiveListener);
        mAlertDialog = builder.create();
        return mAlertDialog;
    }

    public AlertDialog createAlertDialog(String title, String message, String positiveButtonText, DialogInterface.OnClickListener positiveListener, String negativeButtonText, DialogInterface.OnClickListener negativeListener, boolean cancellable) {
        this.mAlertDialogBuilder = (new AlertDialog.Builder(this.mContext));
        AlertDialog.Builder builder = this.mAlertDialogBuilder.setTitle(title)
                .setMessage(message)
                .setCancelable(cancellable).
                        setPositiveButton(positiveButtonText, positiveListener).setNegativeButton(negativeButtonText, negativeListener);
        mAlertDialog = builder.create();
        return mAlertDialog;
    }

    /*public AlertDialog createAlertDialog(String message, String positiveButtonText, DialogInterface.OnClickListener positiveListener, String negativeButtonText, DialogInterface.OnClickListener negativeListener, boolean cancellable) {
        this.mAlertDialogBuilder = (new AlertDialog.Builder(this.mContext));
        AlertDialog.Builder builder = this.mAlertDialogBuilder
                .setMessage(message)
                .setCancelable(cancellable).
                        setPositiveButton(positiveButtonText, positiveListener);

        return builder.create();
    }*/

    public AlertDialog createAlertDialog(int title, int message, int positiveButtonText, DialogInterface.OnClickListener positiveListener, int negativeButtonText, DialogInterface.OnClickListener negativeListener, boolean cancellable) {
        this.mAlertDialogBuilder = (new AlertDialog.Builder(this.mContext));
        AlertDialog.Builder builder = this.mAlertDialogBuilder.setTitle(title)
                .setMessage(message)
                .setCancelable(cancellable).
                        setPositiveButton(positiveButtonText, positiveListener).setNegativeButton(negativeButtonText, negativeListener);
        mAlertDialog = builder.create();
        return mAlertDialog;
    }

    public AlertDialog createAlertDialog(int message, int positiveButtonText, DialogInterface.OnClickListener positiveListener, int negativeButtonText, DialogInterface.OnClickListener negativeListener, boolean cancellable) {
        this.mAlertDialogBuilder = (new AlertDialog.Builder(this.mContext));
        AlertDialog.Builder builder = this.mAlertDialogBuilder
                .setMessage(message)
                .setCancelable(cancellable).
                        setPositiveButton(positiveButtonText, positiveListener).setNegativeButton(negativeButtonText, negativeListener);
        mAlertDialog = builder.create();
        return mAlertDialog;
    }


    public ProgressHUD getProgressDialog() {
        return this.mProgressDialog;
    }

    public void showProgressDialog(String message, boolean cancellable, DialogInterface.OnCancelListener cancelListener) {
        this.mProgressDialog.show(message, false, cancellable, cancelListener);
    }

    public void showProgressDialog(String message, boolean cancellable, DialogInterface.OnCancelListener cancelListener, DialogInterface.OnDismissListener dismissListener) {
        this.mProgressDialog.show(message, false, cancellable, cancelListener);
        this.mProgressDialog.setOnDismissListener(dismissListener);
    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }


}

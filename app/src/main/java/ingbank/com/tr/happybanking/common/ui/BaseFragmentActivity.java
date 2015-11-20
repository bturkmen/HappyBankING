package ingbank.com.tr.happybanking.common.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ViewGroup;
import android.view.WindowManager;

import ingbank.com.tr.happybanking.common.ui.controls.ProgressHUD;
import ingbank.com.tr.happybanking.common.util.FontHelper;


public class BaseFragmentActivity extends ActionBarActivity {
    private BaseUIUtility mUIUtility;

    @Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mUIUtility = new BaseUIUtility(this);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setFonts();
    }


    public void setFonts() {
        final ViewGroup mContainer = (ViewGroup) findViewById(
                android.R.id.content).getRootView();
        FontHelper.setAppFont(mContainer, true);
    }

//    public void setLeftIcon(int icon) {
//        ImageView mActionbarLeftIcon = (ImageView) getSupportActionBar().getCustomView().findViewById(R.id.imgActionbarLeftIcon);
//        mActionbarLeftIcon.setImageResource(icon);
//    }
//
//    public void setRightIcon(int icon) {
//        ImageView mActionbarRightIcon = (ImageView) getSupportActionBar().getCustomView().findViewById(R.id.imgActionbarRightIcon);
//        mActionbarRightIcon.setImageResource(icon);
//    }

    public ProgressHUD getProgressDialog() {
        return this.mUIUtility.getProgressDialog();
    }

    public AlertDialog createAlertDialog(AlertDialogParams alertDialogParams) {
        return this.mUIUtility.createAlertDialog(alertDialogParams);
    }

    public void showProgressDialog(String message, boolean cancellable, DialogInterface.OnCancelListener cancelListener) {
        this.mUIUtility.showProgressDialog(message, cancellable, cancelListener);
    }

    public void dismissProgressDialog() {
        this.mUIUtility.dismissProgressDialog();
    }

    public AlertDialog createAlertDialog(String title, String message, String positiveButtonText, DialogInterface.OnClickListener positiveListener, boolean cancellable) {
        return this.mUIUtility.createAlertDialog(title, message, positiveButtonText, positiveListener, cancellable);
    }

    public AlertDialog createAlertDialog(int title, int message, int positiveButtonText, DialogInterface.OnClickListener positiveListener, boolean cancellable) {
        return this.mUIUtility.createAlertDialog(title, message, positiveButtonText, positiveListener, cancellable);
    }

    public AlertDialog createAlertDialog(int message, int positiveButtonText, DialogInterface.OnClickListener positiveListener, boolean cancellable) {
        return this.mUIUtility.createAlertDialog(message, positiveButtonText, positiveListener, cancellable);
    }

    public AlertDialog createAlertDialog(String title, String message, String positiveButtonText, DialogInterface.OnClickListener positiveListener, String negativeButtonText, DialogInterface.OnClickListener negativeListener, boolean cancellable) {
        return this.mUIUtility.createAlertDialog(title, message, positiveButtonText, positiveListener, negativeButtonText, negativeListener, cancellable);
    }

    public AlertDialog createAlertDialog(int title, int message, int positiveButtonText, DialogInterface.OnClickListener positiveListener, int negativeButtonText, DialogInterface.OnClickListener negativeListener, boolean cancellable) {
        return this.mUIUtility.createAlertDialog(title, message, positiveButtonText, positiveListener, negativeButtonText, negativeListener, cancellable);
    }

    public AlertDialog createAlertDialog(int message, int positiveButtonText, DialogInterface.OnClickListener positiveListener, int negativeButtonText, DialogInterface.OnClickListener negativeListener, boolean cancellable) {
        return this.mUIUtility.createAlertDialog(message, positiveButtonText, positiveListener, negativeButtonText, negativeListener, cancellable);

    }

    public void showProgressDialog(String message, boolean cancellable, int style) {
        showProgressDialog(message, cancellable, style);
    }

    public void showProgressDialog(int title, int message, boolean cancellable, int style) {
        showProgressDialog(title, message, cancellable, style);
    }

    public void showProgressDialog(int message, boolean cancellable, int style) {
        showProgressDialog(message, cancellable, style);
    }

}

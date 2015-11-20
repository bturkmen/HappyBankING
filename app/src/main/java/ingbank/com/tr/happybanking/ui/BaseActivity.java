package ingbank.com.tr.happybanking.ui;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import ingbank.com.tr.happybanking.R;
import ingbank.com.tr.happybanking.common.ui.BaseUIUtility;
import ingbank.com.tr.happybanking.common.ui.controls.ProgressHUD;
import ingbank.com.tr.happybanking.common.util.FontHelper;
import ingbank.com.tr.happybanking.map.BaseView;


public abstract class BaseActivity extends AppCompatActivity implements BaseView {
    /**
     * Request Code for Android 10 to start activity. That enable us to clear activity stack.
     * PS:The biggest integer prime number :)...
     */
    public static final int REQUESTCODE_GINGERBREAD_MR1 = 13310;
    Fragment dropSideMenuFragment;
    private BaseUIUtility mUIUtility;
    private BroadcastReceiver broadcastReceiverForLogout;

    @Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public abstract int getContentView();

    public abstract void initViews();


    public void setFonts() {
        final ViewGroup mContainer = (ViewGroup) findViewById(
                android.R.id.content).getRootView();
        FontHelper.setAppFont(mContainer, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //mustafa: temporarily disabled
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
//                WindowManager.LayoutParams.FLAG _SECURE);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        this.mUIUtility = new BaseUIUtility(this);

        //getSupportActionBar().setDisplayShowCustomEnabled(true);
        //getSupportActionBar().setCustomView(R.layout.default_actionbar);
        setContentView(getContentView());
        if (isStatusBarTransparented()) {
            getSupportActionBar().hide();
            if (Build.VERSION.SDK_INT >= 19) {

                // Set the status bar to dark-semi-transparentish
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                // Set paddingTop of toolbar to height of status bar.
                // Fixes statusbar covers toolbar issue
                (((ViewGroup)findViewById(android.R.id.content)).getChildAt(0)).setPadding(0, getStatusBarHeight(), 0, 0);
            }
        }
        setFonts();
        initViews();


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.ingbanktr.ingmobil.common.app.ACTION_LOGOUT");
        broadcastReceiverForLogout = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        };
        registerReceiver(broadcastReceiverForLogout, intentFilter);
        //getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        try {
            TextView tvActionbarHeader = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.txtActionbarTitle);
            if (tvActionbarHeader != null) {
                tvActionbarHeader.setText(title);
            }
        } catch (Exception ignored) {

        }
    }

    public void setLeftIcon(int icon) {
        ImageView mActionbarLeftIcon = (ImageView) getSupportActionBar().getCustomView().findViewById(R.id.imgActionbarLeftIcon);
        mActionbarLeftIcon.setImageResource(icon);
    }

    public void setRightIcon(int icon) {
        ImageView mActionbarRightIcon = (ImageView) getSupportActionBar().getCustomView().findViewById(R.id.imgActionbarRightIcon);
        mActionbarRightIcon.setImageResource(icon);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiverForLogout);
        this.mUIUtility.onDestroy();
    }

    public ProgressHUD getProgressDialog() {
        return this.mUIUtility.getProgressDialog();
    }

    @Override
    public void startActivity(Intent intent) {

        try {

            if (intent != null && intent.getComponent() != null) {
                Class c = Class.forName(intent.getComponent().getClassName());

            }

            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.GINGERBREAD_MR1) {
                // only for gingerbread and newer versions
                super.startActivity(intent);
            } else {
                startActivityForResult(intent, REQUESTCODE_GINGERBREAD_MR1);
            }

        } catch (ClassNotFoundException ignored) {

        }
    }

    public void showAuthorizationMessage() {

    }

    public Fragment findFragmentById(int id) {
        return getSupportFragmentManager().findFragmentById(id);
    }

    /* UI Related Actions Start */
    public AlertDialog createAlertDialog(String title, String message, String positiveButtonText, DialogInterface.OnClickListener positiveListener, String negativeButtonText, DialogInterface.OnClickListener negativeListener, boolean cancellable) {
        return this.mUIUtility.createAlertDialog(title, message, positiveButtonText, positiveListener, negativeButtonText, negativeListener, cancellable);
    }

    public AlertDialog createAlertDialog(String title, String message, String positiveButtonText, DialogInterface.OnClickListener positiveListener, boolean cancellable) {
        return this.mUIUtility.createAlertDialog(title, message, positiveButtonText, positiveListener, cancellable);
    }

    public void showProgressDialog(String message, boolean cancellable, DialogInterface.OnCancelListener cancelListener) {
        this.mUIUtility.showProgressDialog(message, cancellable, cancelListener);
    }

    public void dismissProgressDialog() {
        this.mUIUtility.dismissProgressDialog();
    }

    public void showWaitingDialog(int res) {
        if (res == 0)
            showProgressDialog(null, false, null);
        else
            showProgressDialog(getString(res), false, null);
    }

    public Fragment getDropSideViewFragment() {
        return dropSideMenuFragment;
    }


    @Override
    public void showWaitingDialog() {
        showWaitingDialog(0);
    }

    @Override
    public void dismissWaitingDialog() {
        dismissProgressDialog();
    }

    @Override
    public void onNetworkError() {
        createAlertDialog(getString(R.string.errorHeader115), getString(R.string.error115), getString(R.string.errorClose115), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }, false).show();
    }

    // A method to find height of the status bar
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public boolean isStatusBarTransparented() {
        return false;
    }

    
}
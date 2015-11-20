package ingbank.com.tr.happybanking.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ingbank.com.tr.happybanking.R;
import ingbank.com.tr.happybanking.common.ui.BaseUIUtility;
import ingbank.com.tr.happybanking.common.util.FontHelper;
import ingbank.com.tr.happybanking.map.BaseView;


public abstract class BaseFragment extends Fragment implements BaseView {
    public static final int REQUESTCODE_GINGERBREAD_MR1 = 13310;

    private BaseUIUtility mUIUtility;
    private OnHiddenChangedListener onHiddenChangedListener;
    ///

    public abstract int getContentView();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mUIUtility = new BaseUIUtility(this.getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getContentView(), container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setFonts(view);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (onHiddenChangedListener != null) onHiddenChangedListener.onHiddenChanged(hidden);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mUIUtility.onDestroy();
    }

    public void setFonts(View view) {
        final ViewGroup mContainer = (ViewGroup) view.getRootView();
        FontHelper.setAppFont(mContainer, true);
    }

    public void showProgressDialog(String message, boolean cancellable, DialogInterface.OnCancelListener cancelListener) {
        this.mUIUtility.showProgressDialog(message, cancellable, cancelListener);
    }

    public void showProgressDialog(String message, boolean cancellable, DialogInterface.OnCancelListener cancelListener, DialogInterface.OnDismissListener dismissListener) {
        this.mUIUtility.showProgressDialog(message, cancellable, cancelListener, dismissListener);
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


    public AlertDialog createAlertDialog(String title, String message, String positiveButtonText, DialogInterface.OnClickListener positiveListener, String negativeButtonText, DialogInterface.OnClickListener negativeListener, boolean cancellable) {
        return this.mUIUtility.createAlertDialog(title, message, positiveButtonText, positiveListener, negativeButtonText, negativeListener, cancellable);
    }


    public AlertDialog createAlertDialog(int title, int message, int positiveButtonText, DialogInterface.OnClickListener positiveListener, int negativeButtonText, DialogInterface.OnClickListener negativeListener, boolean cancellable) {
        return this.mUIUtility.createAlertDialog(title, message, positiveButtonText, positiveListener, negativeButtonText, negativeListener, cancellable);
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

    public void setVisibility(int visibility) {
        FragmentManager fm = getChildFragmentManager();
        if (visibility == View.VISIBLE)
            fm.beginTransaction()
                    .show(this)
                    .commit();
        else if (visibility == View.INVISIBLE || visibility == View.GONE)
            fm.beginTransaction().hide(this).commit();

    }

    public void showWaitingDialog(int res) {
        showProgressDialog(getString(res), false, null);
    }


    @Override
    public void showWaitingDialog() {
        showProgressDialog("", false, null);
    }

    @Override
    public void dismissWaitingDialog() {
        dismissProgressDialog();
    }

    @Override
    public void onNetworkError() {

    }

    public void showAuthorizationMessage() {
        createAlertDialog("", getString(R.string.error66), getString(R.string.global_ok), null, true).show();
    }

    public void setOnHiddenChangedListener(OnHiddenChangedListener onHiddenChangedListener) {
        this.onHiddenChangedListener = onHiddenChangedListener;
    }

    public interface OnHiddenChangedListener {
        void onHiddenChanged(boolean hidden);
    }


    /**
     * Adds the listener to all given Views
     *
     * @param listener viewListener
     * @param views    related view(s)
     */
    public static void addOnClickListeners(View.OnClickListener listener, View... views) {
        for (View view : views) {
            if (view != null) {
                view.setOnClickListener(listener);
            }
        }
    }

    @Override
    public void startActivity(Intent intent) {

        try {
            Class c = Class.forName(intent.getComponent().getClassName());

            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.GINGERBREAD_MR1) {
                // only for gingerbread and newer versions
                super.startActivity(intent);
            } else {
                startActivityForResult(intent, REQUESTCODE_GINGERBREAD_MR1);
            }

        } catch (ClassNotFoundException ignored) {

        }

    }
}

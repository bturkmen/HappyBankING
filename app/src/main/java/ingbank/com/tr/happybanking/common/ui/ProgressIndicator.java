package ingbank.com.tr.happybanking.common.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.WindowManager;

import ingbank.com.tr.happybanking.R;

/**
 * Created by mustafa on 1.04.2015.
 */
public class ProgressIndicator extends ProgressDialog {

    private static ProgressIndicator mProgressIndicator;

    public ProgressIndicator(Context context) {
        super(context);
    }

    public static ProgressIndicator createProgressIndicator(Context context){
        if(null ==  mProgressIndicator){
            mProgressIndicator = new ProgressIndicator(context);
        }
        return mProgressIndicator;
    }

    public ProgressIndicator getProgressIndicator(){
        return mProgressIndicator;
    }

    public void showProgressIndicator(){
        try {
            mProgressIndicator.show();
        } catch (WindowManager.BadTokenException e){

        }

        mProgressIndicator.setCancelable(false);
        mProgressIndicator.setContentView(R.layout.progress_circle);
    }

}
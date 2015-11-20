package ingbank.com.tr.happybanking.ui;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;

import ingbank.com.tr.happybanking.R;


/**
 * Created by ibrahimyilmaz on 27/10/15.
 */
public class ProgressBar extends android.widget.ProgressBar {
    public ProgressBar(Context context) {
        super(context);
    }

    public ProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.ing_orange), PorterDuff.Mode.SRC_IN);
    }
}

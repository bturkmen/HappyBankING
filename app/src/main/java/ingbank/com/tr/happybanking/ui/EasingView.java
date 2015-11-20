package ingbank.com.tr.happybanking.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import ingbank.com.tr.happybanking.R;

/**
 * Created by ibrahimyilmaz on 14/07/15.
 */
public class EasingView extends LinearLayout {
    private int mEasingSpeed = 1;

    public EasingView(Context context) {
        super(context);
    }

    public EasingView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        final TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.EasingView);
        this.setEasingSpeed(a.getInt(R.styleable.EasingView_easingSpeed, 1));
    }

    public int getEasingSpeed() {
        return mEasingSpeed;
    }

    public void setEasingSpeed(int mEasingSpeed) {
        this.mEasingSpeed = mEasingSpeed;
    }
}

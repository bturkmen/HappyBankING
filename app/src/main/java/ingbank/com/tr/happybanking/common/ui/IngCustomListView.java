package ingbank.com.tr.happybanking.common.ui;


import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;


public class IngCustomListView extends LinearLayout {
    private ListAdapter adapter;
    private OnClickListener onClickListener = null;
    private OnTouchListener onTouchListener = null;


    public IngCustomListView(Context context) {
        super(context);
    }

    public IngCustomListView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public void bindLinearLayout() {
        removeAllViews();
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            View v = adapter.getView(i, null, null);
            v.setClickable(true);
            v.setOnClickListener(this.onClickListener);
            v.setOnTouchListener(this.onTouchListener);
            Resources r = getResources();
            float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, r.getDisplayMetrics());
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, (int) px);
            v.setLayoutParams(params);
            v.setId(i);
            addView(v);
            if (i < count - 1) {
                View devider = new View(getContext());
                addView(devider);
            }
        }
        requestLayout();
        invalidate();
    }

    /**
     * Adapter
     *
     * @return adapter
     */
    public ListAdapter getAdapter() {
        return adapter;
    }

    /**
     * @param adpater
     */
    public void setAdapter(ListAdapter adpater) {
        this.adapter = adpater;
        bindLinearLayout();
    }

    /**
     * @return
     */
    public OnClickListener getOnclickListner() {
        return onClickListener;
    }

    /**
     * @param onClickListener
     */
    public void setOnclickLinstener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public OnTouchListener getOnTouchListener() {
        return onTouchListener;
    }

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

}

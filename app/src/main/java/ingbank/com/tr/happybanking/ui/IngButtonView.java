package ingbank.com.tr.happybanking.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ingbank.com.tr.happybanking.R;


/**
 * Created by faruktoptas on 16/10/15.
 */
public class IngButtonView extends LinearLayout {

    private View view;
    private TextView textView;
    private ImageView ivLineBottom;
    private String textViewText = "";

    public IngButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.IngButtonView, 0, 0);
        try{
            textViewText = typedArray.getString(R.styleable.IngButtonView_buttonText);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            typedArray.recycle();
        }

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.view_ing_button_view, this, true);
        textView = (TextView)view.findViewById(R.id.view_ing_button_view_text);
        ivLineBottom = (ImageView) view.findViewById(R.id.ivLine2);
        textView.setText(textViewText);
    }

    public IngButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.IngButtonView, 0, 0);
        try{
            textViewText = typedArray.getString(R.styleable.IngButtonView_buttonText);
        } finally {
            typedArray.recycle();
        }

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.view_ing_button_view, this, true);
        textView = (TextView)view.findViewById(R.id.view_ing_button_view_text);
        textView.setText(textViewText);
    }

    public void hideBottomLine(){
        ivLineBottom.setVisibility(View.GONE);
    }

    public void setButtonText(String s) {
        textView.setText(s);
    }
}

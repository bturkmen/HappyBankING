package ingbank.com.tr.happybanking;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by burcuturkmen on 20/11/15.
 */
public class ActivityDemo1 extends Activity {
    private LinearLayout llBar,llHappy,llSad,llNotr;
    private RelativeLayout rlPopup;
    private TextView tvMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_demo_1);
        createComponent();
        setWeight(0.5f,0.3f,0.2f);
    }

    private void setWeight(float i, float i1, float i2) {
        llBar.removeAllViews();
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
        p.weight = i;
        p.topMargin=15;
        llHappy.setLayoutParams(p);

        LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
        p1.weight=i1;
        p1.topMargin=15;
        llNotr.setLayoutParams(p1);

        LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
        p2.weight=i2;
        p2.topMargin=15;
        llSad.setLayoutParams(p2);

        llBar.setWeightSum(1.0f);
        llBar.addView(llHappy);
        llBar.addView(llNotr);
        llBar.addView(llSad);
        rlPopup.setVisibility(View.GONE);


    }

    private void createComponent() {
        llBar=(LinearLayout)findViewById(R.id.llBar);
        llHappy=(LinearLayout)findViewById(R.id.llHappy);
        llNotr=(LinearLayout)findViewById(R.id.llNotr);
        llSad=(LinearLayout)findViewById(R.id.llSad);
        rlPopup=(RelativeLayout)findViewById(R.id.rlPopup);
        tvMessage=(TextView)findViewById(R.id.tvMessage);
    }
}

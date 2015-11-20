package ingbank.com.tr.happybanking.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ibrahimyilmaz on 13/07/15.
 */
public class VerticalViewPagerWithEasing extends VerticalViewPager {
    public VerticalViewPagerWithEasing(Context context) {
        super(context);

    }

    public VerticalViewPagerWithEasing(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init() {

        // The majority of the magic happens here
        // The easiest way to get rid of the overscroll drawing that happens on the left and right
        setOverScrollMode(OVER_SCROLL_NEVER);
        setMyScroller();
        setPageTransformer(true, new VerticalPageTransformerX());

    }

    private void setMyScroller() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this, new EasingScroller(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, true);
    }


    public class EasingScroller extends Scroller {

        public EasingScroller(Context context) {
            super(context, new DecelerateInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, 400 /*1 secs*/);
        }

    }

    public class VerticalPageTransformerX implements ViewPager.PageTransformer {

        @Override
        public void transformPage(View view, float position) {

            ViewGroup vg = (ViewGroup) view;
            List<EasingView> easingViews = new ArrayList<>();
            for (int i = 0; i < vg.getChildCount(); i++) {
                View ch = vg.getChildAt(i);
                if (ch.getClass().equals(EasingView.class))
                    easingViews.add((EasingView) ch);
            }
            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                view.setAlpha(1);

                // Counteract the default slide transition
                view.setTranslationX(view.getWidth() * -position);

                //set Y position to swipe in from top
                float yPosition = position * view.getHeight();
                view.setTranslationY(yPosition);

                for (EasingView ev : easingViews) {
                    ev.setTranslationY(view.getWidth() * position * (1 / ev.getEasingSpeed()) * 2);
                }
            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

}

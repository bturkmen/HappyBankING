package ingbank.com.tr.happybanking.ui.hamburger;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.TransitionDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.widget.ImageButton;


import java.util.ArrayList;
import java.util.List;

import ingbank.com.tr.happybanking.R;


public class HamburgerButton extends ImageButton {

    public enum HamburgerState {
        Hamburger,
        Back,
        Close
    }

    public static final int MIN_SIZE = 0;
    public static final float LINE_WIDTH = 78.0f / 232;
    public static final float LINE_HEIGHT = 4.0f / 148;
    public static final float LINE_SPACE = 22.0f / 148;

    private List<HamburgerState> mSupportStates = new ArrayList<>(); // list support state
    private int mCurrentState = 0; // current state of button
    private int mDefaultColor = 0;
    private int mCloseColor = 0;
    private int mLineWidth = (int) LINE_WIDTH * 232;
    private int mLineHeight = (int) LINE_HEIGHT * 148;
    private int mLineSpace = (int) LINE_SPACE * 148;
    private Point mCenterPoint;

    private ShapeTransformDrawable mLineTop = null;
    private ShapeTransformDrawable mLineCenter = null;
    private ShapeTransformDrawable mLineBottom = null;

    public HamburgerButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HamburgerButton);
        try {
            mDefaultColor = a.getColor(R.styleable.HamburgerButton_color_line, 0);
            mCloseColor = a.getColor(R.styleable.HamburgerButton_color_line_close, 0);
            mSupportStates.add(HamburgerState.Hamburger);
            mSupportStates.add(HamburgerState.Close);
        } finally {
            a.recycle();
        }
        mCurrentState = 0;

        // hover background
        //setBackgroundResource(R.drawable.hamburger_bg_btn);
        // 3 rectangle for hamburger

    }

    private void createLines(int width, int height) {
        mLineHeight = (int) (LINE_HEIGHT * height);
        mLineWidth = (int) (LINE_WIDTH * width);
        mLineSpace = (int) (LINE_SPACE * height);
        RectShape shape = new RectShape();
        shape.resize(mLineWidth, mLineHeight);
        mLineTop = new ShapeTransformDrawable(shape, this);
        mLineTop.getPaint().setColor(mDefaultColor);
        mLineCenter = new ShapeTransformDrawable(shape, this);
        mLineCenter.getPaint().setColor(mDefaultColor);
        mLineBottom = new ShapeTransformDrawable(shape, this);
        mLineBottom.getPaint().setColor(mDefaultColor);
    }

    @Override
    public boolean performClick() {
        switchState();
        return super.performClick();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0, height = 0;
        if (widthMode == MeasureSpec.EXACTLY)
            width = widthSize;
        else if (widthMode == MeasureSpec.AT_MOST)
            width = Math.min(MIN_SIZE, widthSize);
        else
            width = widthSize;

        if (heightMode == MeasureSpec.EXACTLY)
            height = heightSize;
        else if (heightMode == MeasureSpec.AT_MOST)
            height = Math.min(MIN_SIZE, heightSize);
        else
            height = heightSize;

        setMeasuredDimension(width, height);

        //set lines's sizes
        createLines(width, height);
        // set center point base width height and width
        mCenterPoint = new Point(width / 2, height / 2);
        int x = -mLineWidth / 2;
        int y = -mLineHeight / 2;

        mLineCenter.setPosition(x, y);
        mLineTop.setPosition(x, y - mLineSpace - mLineHeight);
        mLineBottom.setPosition(x, y + mLineSpace + mLineHeight);

        swichToState(mSupportStates.get(0), false);

//        mLineCenter.setRotatePivot(mLineWidth / 2, 0);
//        mLineTop.setRotatePivot(-x, -(y - mLineSpace - mLineHeight));
//        mLineTop.setScalePivot(mLineWidth / 2, 0);
//        mLineBottom.setRotatePivot(-x, -(y + mLineSpace + mLineHeight));
//        mLineBottom.setScalePivot(mLineWidth / 2, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int count = canvas.save();
        canvas.translate(mCenterPoint.x, mCenterPoint.y);
        if (mLineCenter != null)
            mLineCenter.draw(canvas);
        if (mLineTop != null)
            mLineTop.draw(canvas);
        if (mLineBottom != null)
            mLineBottom.draw(canvas);
        canvas.restoreToCount(count);
    }

    public void switchState() {
        mCurrentState++;
        if (mCurrentState >= mSupportStates.size())
            mCurrentState = 0;

        // animation
        swichToState(mSupportStates.get(mCurrentState), true);
    }

    private void swichToState(HamburgerState state, boolean animation) {
        if (animation) {
            mLineTop.transitionTo(getTransformStateAnimation(state, 0), 400);
            mLineCenter.transitionTo(getTransformStateAnimation(state, 1), 400);
            mLineBottom.transitionTo(getTransformStateAnimation(state, 2), 400);
            if (getBackground().getClass().getName().equals(TransitionDrawable.class.getName())) {
                TransitionDrawable transition = (TransitionDrawable) getBackground();
                if (state.equals(HamburgerState.Hamburger))
                    transition.reverseTransition(400);
                else
                    transition.startTransition(400);
            }
        } else {
            mLineTop.setState(getTransformStateAnimation(state, 0));
            mLineCenter.setState(getTransformStateAnimation(state, 1));
            mLineBottom.setState(getTransformStateAnimation(state, 2));
            if (getBackground().getClass().getName().equals(TransitionDrawable.class.getName())) {
                TransitionDrawable transition = (TransitionDrawable) getBackground();
                if (state.equals(HamburgerState.Hamburger))
                    transition.reverseTransition(0);
                else
                    transition.startTransition(0);
            }
        }
    }


    public TransformState getTransformStateAnimation(HamburgerState state, int line) {
        int x = -mLineWidth / 2;
        int y = -mLineHeight / 2;
        int rColor = Color.red(mDefaultColor);
        int gColor = Color.green(mDefaultColor);
        int bColor = Color.blue(mDefaultColor);
        switch (state) {

            case Hamburger: {
                switch (line) {
                    case 0:
                        return new TransformState(x, y - mLineSpace - mLineHeight, 1, 1, mLineWidth / 2, 0,
                                360, -mLineTop.getState().PosX, -mLineTop.getState().PosY, rColor, gColor, bColor);
                    case 1:
                        return new TransformState(x, y, 1, 1, 0, 0, 360, mLineWidth / 2, mLineHeight / 2, rColor, gColor, bColor);
                    case 2:
                        return new TransformState(x, y + mLineSpace + mLineHeight, 1, 1, mLineWidth / 2, 0,
                                360, -mLineBottom.getState().PosX, -mLineBottom.getState().PosY, rColor, gColor, bColor);
                }
            }
            case Close: {
                switch (line) {
                    case 0:
                        return new TransformState((int) ((-33f / 232) * getWidth()), 0, 1, 1, 0, 0,
                                135, mLineWidth / 2, 0, Color.red(mCloseColor), Color.green(mCloseColor), Color.blue(mCloseColor));
                    case 1:
                        return new TransformState(x, y, 0, 1, mLineWidth / 2, 0, 180, mLineWidth / 2, 0, Color.red(mCloseColor), Color.green(mCloseColor), Color.blue(mCloseColor));
                    case 2:
                        return new TransformState((int) ((-33f / 232) * getWidth()), 0, 1, 1, 0, 0,
                                45, mLineWidth / 2, 0, Color.red(mCloseColor), Color.green(mCloseColor), Color.blue(mCloseColor));
                }
            }
        }
        return null;
    }
}

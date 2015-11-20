package ingbank.com.tr.happybanking.common.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;


import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import ingbank.com.tr.happybanking.R;
import ingbank.com.tr.happybanking.common.util.FontHelper;

public class ProgressWheel extends View {

    int mTimer = 0; // the total time the ProgressWheel will complete, in seconds
    int progress = 0; // the progress of the ProgressWheel, from 0 to 360
    long startTime, endTime; //start time and end time in milliseconds
    boolean mRunning; // is the progress wheel running

    Timer progressTimer; // Timer ticks at fixed 100ms rate
    OnProgressWheelListener mProgressWheelListener;

    //layout sizes
    private int layout_height = 0;
    private int layout_width = 0;
    private int paddingTop = 5;
    private int paddingBottom = 5;
    private int paddingLeft = 5;
    private int paddingRight = 5;

    //Sizes (with defaults)
    private int barWidth = 5;
    private int circleWidth = 2;
    private int textFontSize = 20;

    //Colors (with defaults)
    private int barColor = 0xAAFF0000;
    private int barColorEnd = 0xffFFC500;
    private int circleColor = 0xAAFF0000;
    private int textColor = 0xAAFF0000;

    //Paints
    private Paint barPaint = new Paint();
    private Paint circlePaint = new Paint();
    private Paint textPaint = new Paint();

    //Rectangles
    @SuppressWarnings("unused")
    private RectF circleBounds = new RectF();

    //Animation
    private Handler spinHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            invalidate();
        }
    };

    //Other
    private String text = "";
    private String[] splitText = {};

    /**
     * The constructor for the ProgressWheel
     *
     * @param context
     * @param attrs
     */
    public ProgressWheel(Context context, AttributeSet attrs) {
        super(context, attrs);

        mProgressWheelListener = (OnProgressWheelListener) context;
        parseAttributes(context.obtainStyledAttributes(attrs, R.styleable.ProgressWheel));
    }

    //----------------------------------
    //Setting up stuff
    //----------------------------------

    /*
     * When this is called, make the view square.
     * From: http://www.jayway.com/2012/12/12/creating-custom-android-views-part-4-measuring-and-how-to-force-a-view-to-be-square/
     *
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // The first thing that happen is that we call the superclass
        // implementation of onMeasure. The reason for that is that measuring
        // can be quite a complex process and calling the super method is a
        // convenient way to get most of this complexity handled.
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // We can’t use getWidth() or getHight() here. During the measuring
        // pass the view has not gotten its final size yet (this happens first
        // at the start of the layout pass) so we have to use getMeasuredWidth()
        // and getMeasuredHeight().
        int size = 0;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int widthWithoutPadding = width - getPaddingLeft() - getPaddingRight();
        int heigthWithoutPadding = height - getPaddingTop() - getPaddingBottom();

        // Finally we have some simple logic that calculates the size of the view
        // and calls setMeasuredDimension() to set that size.
        // Before we compare the width and height of the view, we remove the padding,
        // and when we set the dimension we add it back again. Now the actual content
        // of the view will be square, but, depending on the padding, the total dimensions
        // of the view might not be.
        if (widthWithoutPadding > heigthWithoutPadding) {
            size = heigthWithoutPadding;
        } else {
            size = widthWithoutPadding;
        }

        // If you override onMeasure() you have to call setMeasuredDimension().
        // This is how you report back the measured size.  If you don’t call
        // setMeasuredDimension() the parent will throw an exception and your
        // application will crash.
        // We are calling the onMeasure() method of the superclass so we don’t
        // actually need to call setMeasuredDimension() since that takes care
        // of that. However, the purpose with overriding onMeasure() was to
        // change the default behaviour and to do that we need to call
        // setMeasuredDimension() with our own values.
        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(), size + getPaddingTop() + getPaddingBottom());
    }

    /**
     * Use onSizeChanged instead of onAttachedToWindow to get the dimensions of the view,
     * because this method is called after measuring the dimensions of MATCH_PARENT & WRAP_CONTENT.
     * Use this dimensions to setup the bounds and paints.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Share the dimensions
        layout_width = w;
        layout_height = h;

        setupBounds();
        setupPaints();
        invalidate();
    }

    /**
     * Set the properties of the paints we're using to
     * draw the progress wheel
     */
    private void setupPaints() {
        //barPaint.setColor(barColor);
        barPaint.setAntiAlias(true);
        barPaint.setStyle(Style.STROKE);
        barPaint.setStrokeWidth(barWidth);

        // the gradient is 45degrees
        float x0 = (float) (circleBounds.centerX() + circleBounds.width()/2/ Math.sqrt(2));
        float y0 = (float) (circleBounds.centerY() - circleBounds.height()/2/ Math.sqrt(2));
        float x1 = (float) (circleBounds.centerX() - circleBounds.width()/2/ Math.sqrt(2));
        float y1 = (float) (circleBounds.centerY() + circleBounds.height()/2/ Math.sqrt(2));

        //final SweepGradient sweepGradient = new SweepGradient(circleBounds.centerX(), circleBounds.centerY(), barColor, barColorEnd);
        //Matrix flipMatrix = new Matrix();
        //flipMatrix.setRotate(-90, circleBounds.centerX()/2, circleBounds.centerY()/2);
        //sweepGradient.setLocalMatrix(flipMatrix);
        //barPaint.setShader(sweepGradient);

        final LinearGradient linearGradient = new LinearGradient(
                x0, y0, x1, y1,
                barColorEnd, barColor, Shader.TileMode.CLAMP);
        barPaint.setShader(linearGradient);

        circlePaint.setColor(circleColor);
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Style.STROKE);
        circlePaint.setStrokeWidth(circleWidth);

        textPaint.setColor(textColor);
        textPaint.setStyle(Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textFontSize);
        textPaint.setTypeface(FontHelper.getTypefaceFromName(getContext(), "extra_light"));
    }

    /**
     * Set the bounds of the component
     */
    private void setupBounds() {
        // Width should equal to Height, find the min value to setup the circle
        int minValue = Math.min(layout_width, layout_height);

        // Calc the Offset if needed
        int xOffset = layout_width - minValue;
        int yOffset = layout_height - minValue;

        // Add the offset
        paddingTop = this.getPaddingTop() + (yOffset / 2);
        paddingBottom = this.getPaddingBottom() + (yOffset / 2);
        paddingLeft = this.getPaddingLeft() + (xOffset / 2);
        paddingRight = this.getPaddingRight() + (xOffset / 2);

        int width = getWidth(); //this.getLayoutParams().width;
        int height = getHeight(); //this.getLayoutParams().height;

        circleBounds = new RectF(paddingLeft + barWidth,
                paddingTop + barWidth,
                width - paddingRight - barWidth,
                height - paddingBottom - barWidth);

    }

    /**
     * Parse the attributes passed to the view from the XML
     *
     * @param a the attributes to parse
     */
    private void parseAttributes(TypedArray a) {
        barWidth = (int) a.getDimension(R.styleable.ProgressWheel_barWidth, barWidth);
        barColor = a.getColor(R.styleable.ProgressWheel_barColor, barColor);

        circleWidth = (int) a.getDimension(R.styleable.ProgressWheel_circleWidth, circleWidth);
        circleColor = a.getColor(R.styleable.ProgressWheel_circleColor, circleColor);

        textFontSize = (int) a.getDimension(R.styleable.ProgressWheel_textFontSize, textFontSize);
        textColor = a.getColor(R.styleable.ProgressWheel_textColor, textColor);

        //if the text is empty ignore it
        if (a.hasValue(R.styleable.ProgressWheel_text)) {
            setText(a.getString(R.styleable.ProgressWheel_text));
        }

        // Recycle
        a.recycle();
    }

    //----------------------------------
    //Animation stuff
    //----------------------------------

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawArc(circleBounds, 360, 360, false, circlePaint);
        canvas.drawArc(circleBounds, progress-90, 360 - progress, false, barPaint);

        //Draw the text (attempts to center it horizontally and vertically)
        float textHeight = textPaint.descent() - textPaint.ascent();
        float verticalTextOffset = (textHeight / 2) - textPaint.descent();

        for (String s : splitText) {
            float horizontalTextOffset = textPaint.measureText(s) / 2;
            canvas.drawText(s, this.getWidth() / 2 - horizontalTextOffset,
                    this.getHeight() / 2 + verticalTextOffset, textPaint);
        }
    }


    /**
     * Increment the progress by 1 (of 360)
     */
    public void incrementProgress() {
        if(getTimer() <= 0)
        {
            // timer must be set before
            progressTimer.cancel();
            return;
        }

        Date todayDate = new Date();
        todayDate.getTime();
        long currentTime = todayDate.getTime();

        long elapsed = currentTime - startTime;

        progress = (int) (360 * elapsed / (getTimer() *1000));

        int remaining = Math.round(((float) (getTimer() - elapsed / 1000)));
        if(remaining <= 0){
            remaining = 0;
            progressTimer.cancel();
            mRunning = false;

            //timed out
            mProgressWheelListener.onProgressWheelTimeout();
        }

        setText(String.valueOf(remaining));
        spinHandler.sendEmptyMessage(0);
    }


    /**
     *
     * @return end time of the progress wheel
     */
    public long startProgress(){
        if(getTimer() <= 0)
        {
            // timer must be set before and it should be bigger than 0
            return -1;
        }

        Date todayDate = new Date();
        todayDate.getTime();
        startTime = todayDate.getTime();
        endTime = startTime + getTimer()*1000;

        progressTimer = new Timer();
        progressTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                incrementProgress();
            }
        }, 10, 10);
        mRunning = true;

        return endTime;
    }

    public void stopProgress(){
        progressTimer.cancel();
        progressTimer = null;
        mRunning = false;
    }

    public long pauseProgress(){
        if(false == mRunning){
            return -1;
        }

        progressTimer.cancel();
        mRunning = false;

        Date todayDate = new Date();
        todayDate.getTime();
        long currentTime = todayDate.getTime();
        long elapsed = currentTime - startTime;

        return elapsed;
    }

    /**
     *
     * @return end time of the progress wheel
     */
    public long resumeProgress(long elapsed){
        if(true == mRunning){
            return -1;
        } else if (elapsed < 0) {
            return -1;
        } else if(getTimer()*1000 < elapsed) {
            // timer must be set before and it should be bigger than elapsed
            return -1;
        }

        Date todayDate = new Date();
        todayDate.getTime();
        startTime = todayDate.getTime() - elapsed;
        endTime = startTime + getTimer()*1000;

        progressTimer = new Timer();
        progressTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                incrementProgress();
            }
        }, 10, 10);
        mRunning = true;

        return endTime;
    }


    //----------------------------------
    //Getters + setters
    //----------------------------------
    public int getTimer() {
        return mTimer;
    }

    public void setTimer(int timer) {
        this.mTimer = timer;
    }

    /**
     * Set the text in the progress bar
     * Doesn't invalidate the view
     *
     * @param text the text to show ('\n' constitutes a new line)
     */
    public void setText(String text) {
        this.text = text;
        splitText = this.text.split("\n");
    }

    public int getBarWidth() {
        return barWidth;
    }

    public void setBarWidth(int barWidth) {
        this.barWidth = barWidth;
    }

    public int getTextFontSize() {
        return textFontSize;
    }

    public void setTextFontSize(int textFontSize) {
        this.textFontSize = textFontSize;
    }

    public int getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    public int getPaddingBottom() {
        return paddingBottom;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    public int getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public int getPaddingRight() {
        return paddingRight;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    public int getBarColor() {
        return barColor;
    }

    public void setBarColor(int barColor) {
        this.barColor = barColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }


    /**
     * Listener class that informs the calling class about the timeout of ProgressWheel
     */
    public interface OnProgressWheelListener{

        void onProgressWheelTimeout();
    }
}
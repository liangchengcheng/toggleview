package mddemo.library.com.activityanimation_master;

import android.content.Context;
import android.content.res.TypedArray;
import android.gesture.GestureOverlayView;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Author:  梁铖城
 * Email:   1038127753@qq.com
 * Date:    2015年11月21日11:32:24
 * Description:  开关
 */
public class SwitchView extends View implements GestureDetector.OnGestureListener {

    private Paint paint = new Paint();
    private GestureDetector mGestureDetector;
    private OnSwitchChangedListener onSwitchChangedListener;
    public static int OFF = 0;
    public static int ON = 1;
    private int value = OFF;//当前状态默认是关状态
    //属性
    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;

    private int thumbMarginLeft;
    private int thumbMarginTop;
    private int thumbMarginRight;
    private int thumbMarginBottom;
    private int cornerRadius;
    private int onColor;
    //运行时状态
    private int thumbLeft = this.paddingLeft + this.thumbMarginLeft;//滑块的位置
    private int thumbDownLeft ;//down事件发生时，滑块的位置
    private boolean scrolling = false;//是否正在拖动


    public SwitchView(Context context) {
        super(context);
        mGestureDetector = new GestureDetector(this);
    }
    public SwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(this);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwitchView);
        paddingLeft = (int)a.getDimension(R.styleable.SwitchView_paddingLeft, 0);
        paddingTop = (int)a.getDimension(R.styleable.SwitchView_paddingTop, 0);
        paddingRight = (int)a.getDimension(R.styleable.SwitchView_paddingRight, 0);
        paddingBottom = (int)a.getDimension(R.styleable.SwitchView_paddingBottom, 0);

        thumbMarginLeft = (int)a.getDimension(R.styleable.SwitchView_thumbMarginLeft, dip2px(this.getContext(), 2));
        thumbMarginTop = (int)a.getDimension(R.styleable.SwitchView_thumbMarginTop, dip2px(this.getContext(), 1));
        thumbMarginRight = (int)a.getDimension(R.styleable.SwitchView_thumbMarginRight, dip2px(this.getContext(), 2));
        thumbMarginBottom = (int)a.getDimension(R.styleable.SwitchView_thumbMarginBottom, dip2px(this.getContext(), 1));

        cornerRadius = (int)a.getDimension(R.styleable.SwitchView_cornerRadius, dip2px(this.getContext(), 4));

        onColor = (int)a.getColor(R.styleable.SwitchView_onColor, 0);
        if(onColor == 0)
            onColor = a.getResourceId(R.styleable.SwitchView_onColor, 0);
        if(onColor == 0)
            onColor = 0xFFD9434B;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    /**
     * 计算组件宽度
     */
    private int measureWidth(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = getDefaultWidth();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    /**
     * 计算组件高度
     */
    private int measureHeight(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = getDefaultHeight();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    /**
     * 计算默认宽度
     */
    private int getDefaultWidth(){
        return dip2px(this.getContext(), 90);
    }

    /**
     * 计算默认宽度
     */
    private int getDefaultHeight(){
        return dip2px(this.getContext(), 30);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawColor(0xFF00FF33);
        drawBackground(canvas);
        drawThumb(canvas);
    }

    /**
     * 画背景
     */
    private void drawBackground(Canvas canvas){
        int backgroundColor = 0xFF989898;
        if(this.value == ON){
            backgroundColor = onColor;
        }
        RectF rectF = new RectF();
        rectF.left = this.paddingLeft;
        rectF.top = this.paddingTop;
        rectF.right = this.getWidth() - this.paddingRight;
        rectF.bottom = this.getHeight() - this.paddingBottom;
        paint.setAntiAlias(true);
        paint.setColor(backgroundColor);
        canvas.drawRoundRect(rectF, this.cornerRadius, this.cornerRadius, paint);
    }

    /**
     * 画滑块
     */
    private void drawThumb(Canvas canvas){
        paint.setColor(0xFFFFFFFF);
        RectF rectF = new RectF();
        if(scrolling)
            rectF.left = this.thumbLeft;
        else{
            rectF.left = getMinThumbLeft();
            if(this.value ==ON){
                rectF.left = getMaxThumbLeft();
            }
        }
        rectF.top = this.paddingTop + this.thumbMarginTop;
        rectF.right = rectF.left + geThumbWidth();
        rectF.bottom = this.getHeight() - this.paddingBottom - this.thumbMarginBottom;
        paint.setAntiAlias(true);
        canvas.drawRoundRect(rectF, this.cornerRadius, this.cornerRadius, paint);

    }

    /**
     * 设置状态，或值
     */
    public void setValue(int value){
        if(value != OFF && value != ON)
            return ;
        this.value = value;
        this.invalidate();
    }

    /**
     * 获得minThumbLeft
     */
    private int getMinThumbLeft(){
        return this.paddingLeft + this.thumbMarginLeft;
    }

    /**
     * 获得maxThumbLeft
     */
    private int getMaxThumbLeft(){
        return this.getWidth() - this.paddingRight - this.thumbMarginRight - geThumbWidth();
    }
    /**
     * 获得滑块宽度
     */
    private int geThumbWidth(){
        return (this.getWidth() - (this.paddingLeft + this.paddingRight + this.thumbMarginLeft + this.thumbMarginRight))/2;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 触碰事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            mGestureDetector.onTouchEvent(event);
            if(event.getAction() == MotionEvent.ACTION_UP){
                upEventHandler((int)event.getX(), (int)event.getY());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public interface OnSwitchChangedListener {
        public void onSwitchChanged(View view, int value);
    }
    //手势监听
    @Override
    public boolean onDown(MotionEvent event) {
        this.thumbLeft = getMinThumbLeft();
        if(this.value ==ON){
            this.thumbLeft = getMaxThumbLeft();
        }
        this.thumbDownLeft = this.thumbLeft;
        this.scrolling = true;
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, float arg2, float arg3) {
        flingEventHandler((int)event1.getX(), (int)event2.getX());
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
    }

    @Override
    public boolean onScroll(MotionEvent event1, MotionEvent event2, float arg2, float arg3) {
        scrollEventHandler((int)event1.getX(), (int)event2.getX());
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        clickEventHandler((int)event.getX(), (int)event.getY());
        return true;
    }

    /**
     * 滑动事件处理
     */
    private void scrollEventHandler(int x1, int x2){
        this.thumbLeft = this.thumbDownLeft + (x2 - x1);
        int minThumbLeft = getMinThumbLeft();
        int maxThumbLeft = getMaxThumbLeft();
        if(this.thumbLeft < minThumbLeft)
            this.thumbLeft = minThumbLeft;
        if(this.thumbLeft > maxThumbLeft)
            this.thumbLeft = maxThumbLeft;
        this.invalidate();
    }
    /**
     * 单击事件处理
     */
    private void clickEventHandler(int x, int y){
        this.thumbLeft = getMaxThumbLeft();
        int minThumbLeft = getMinThumbLeft();
        int thumbWidth = geThumbWidth();
        if(x <= (minThumbLeft + thumbWidth))
            this.thumbLeft = getMinThumbLeft();
    }
    /**
     * fling事件处理
     */
    private void flingEventHandler(int x1, int x2){
        if(x1 == x2)
            return ;
        this.thumbLeft = getMaxThumbLeft();
        if(x2 < x1){//向左滑动
            this.thumbLeft = getMinThumbLeft();
        }
    }

    /**
     * up事件处理
     */
    private void upEventHandler(int x, int y){
        this.scrolling = false;
        int minThumbLeft = getMinThumbLeft();
        int thumbWidth = geThumbWidth();
        int value = ON;
        if(this.thumbLeft <= (minThumbLeft + thumbWidth - thumbWidth/2))
            value = OFF;
        int oldValue = this.value;
        this.setValue(value);
        if(this.onSwitchChangedListener != null && oldValue != value)
            this.onSwitchChangedListener.onSwitchChanged(this, value);
    }

    /**
     * 对外提供接口
     */
    public void setOnSwitchChangedListener(
            OnSwitchChangedListener onSwitchChangedListener) {
        this.onSwitchChangedListener = onSwitchChangedListener;
    }
    /**
     * 为控件设置值
     */
    public int getPaddingLeft() {
        return paddingLeft;
    }
    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }
    public int getPaddingTop() {
        return paddingTop;
    }
    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }
    public int getPaddingRight() {
        return paddingRight;
    }
    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }
    public int getPaddingBottom() {
        return paddingBottom;
    }
    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
    }
    public int getThumbMarginLeft() {
        return thumbMarginLeft;
    }
    public void setThumbMarginLeft(int thumbMarginLeft) {
        this.thumbMarginLeft = thumbMarginLeft;
    }
    public int getThumbMarginTop() {
        return thumbMarginTop;
    }
    public void setThumbMarginTop(int thumbMarginTop) {
        this.thumbMarginTop = thumbMarginTop;
    }
    public int getThumbMarginRight() {
        return thumbMarginRight;
    }
    public void setThumbMarginRight(int thumbMarginRight) {
        this.thumbMarginRight = thumbMarginRight;
    }
    public int getThumbMarginBottom() {
        return thumbMarginBottom;
    }
    public void setThumbMarginBottom(int thumbMarginBottom) {
        this.thumbMarginBottom = thumbMarginBottom;
    }
    public int getCornerRadius() {
        return cornerRadius;
    }
    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
    }
    public int getOnColor() {
        return onColor;
    }
    public void setOnColor(int onColor) {
        this.onColor = onColor;
    }
    public int getValue() {
        return value;
    }

}

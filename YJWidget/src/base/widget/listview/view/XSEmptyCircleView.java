package base.widget.listview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import base.util.ui.XSDisplayUtil;

import com.example.demoxswidget.R;


/**
 * 空心圆
 * 
 * @author zWX202297
 * 
 */
public class XSEmptyCircleView extends View {
    private Paint mTopPaint, mBottomPaint;
    private RectF mOval;
    private float mTopStartAngle = 188;
    private float mBottomStartAngle = 8;
    private float mSweepAngle = 0;

    private float SWEEP_INC = 2;
    private static final float MAX_SWEEP_ANGLE = 164;

    public XSEmptyCircleView(Context context) {
        this(context, null);
    }

    public XSEmptyCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public XSEmptyCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    public void init() {
        mTopPaint = new Paint();
        mTopPaint.setAntiAlias(true);
        mTopPaint.setStyle(Paint.Style.STROKE);
        mTopPaint.setStrokeWidth(2);
        mTopPaint.setColor(getContext().getResources().getColor(R.color.title_bar_bg_color));

        mBottomPaint = new Paint(mTopPaint);

        float width = XSDisplayUtil.dip2px(getContext(), 25);
        mOval = new RectF(2, 2, width - 2, width - 2);
    }

    public void setActualAngle(float actualAngle) {
        if (0 == actualAngle) {
            return;
        }

        SWEEP_INC = MAX_SWEEP_ANGLE / actualAngle;
    }

    public void initSweepAngle() {
        mSweepAngle = 0;
    }

    private void drawArcs(Canvas canvas) {
        canvas.drawArc(mOval, mTopStartAngle, mSweepAngle, false, mTopPaint);
        canvas.drawArc(mOval, mBottomStartAngle, mSweepAngle, false, mBottomPaint);
    }

    public void updateDrawAngle(float angle) {
        mSweepAngle = angle * SWEEP_INC;

        if (mSweepAngle < 0) {
            mSweepAngle = 0;
        }

        if (mSweepAngle > MAX_SWEEP_ANGLE) {
            mSweepAngle = MAX_SWEEP_ANGLE;
        }

        invalidate();
    }

    public void increaseDrawAngle() {
        mSweepAngle += SWEEP_INC;
        if (mSweepAngle > MAX_SWEEP_ANGLE) {
            mSweepAngle = MAX_SWEEP_ANGLE;
            return;
        }
        
        invalidate();
    }

    public void decreaseDrawAngle() {
        mSweepAngle -= SWEEP_INC;
        if (mSweepAngle < 0) {
            mSweepAngle = 0;
            return;
        }
        
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);

        drawArcs(canvas);

        super.onDraw(canvas);
    }
}

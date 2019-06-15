package com.haksoy.pertem.tools;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;



public class MyDoubleArcProgress extends View {
    private RectF oval = new RectF();
    private Paint paint = new Paint();
    int sweepAngle1 = 220;
    int sweepAngle2 = 150;
    int startAngle1 = 120;
    int startAngle2 = 360;
    private float insideRadius;
    private float outsideRadius;
    private int insideArcColor;
    private int outsideArcColor;
    private Runnable animate = new Runnable() {
        public void run() {
            if (MyDoubleArcProgress.this.startAngle1 < 360) {
                MyDoubleArcProgress.this.startAngle1 += 4;
            } else {
                MyDoubleArcProgress.this.startAngle1 = 0;
            }

            if (MyDoubleArcProgress.this.startAngle2 > 0) {
                MyDoubleArcProgress.this.startAngle2 -= 8;
            } else {
                MyDoubleArcProgress.this.startAngle2 = 360;
            }

            MyDoubleArcProgress.this.invalidate();
            MyDoubleArcProgress.this.postDelayed(this, 24l);
        }
    };

    public MyDoubleArcProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.paint.setStyle(Paint.Style.STROKE);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, com.developers.coolprogressviews.R.styleable.DoubleArcProgress, 0, 0);

        try {
            this.insideRadius = array.getDimension(com.developers.coolprogressviews.R.styleable.DoubleArcProgress_insideArcRadius, 50.0F);
            this.outsideRadius = array.getDimension(com.developers.coolprogressviews.R.styleable.DoubleArcProgress_outsideArcRadius, 100.0F);
            this.insideArcColor = array.getColor(com.developers.coolprogressviews.R.styleable.DoubleArcProgress_insideArcColor, Color.parseColor("#99009688"));
            this.outsideArcColor = array.getColor(com.developers.coolprogressviews.R.styleable.DoubleArcProgress_outsideArcColor, Color.parseColor("#009688"));
        } catch (Exception var8) {
            var8.printStackTrace();
        } finally {
            array.recycle();
        }

        this.post(this.animate);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.paint.setColor(this.outsideArcColor);
        this.paint.setStrokeWidth(12.0F);
        this.oval.set((float)(this.getWidth() / 2) - this.outsideRadius, (float)(this.getHeight() / 2) - this.outsideRadius, (float)(this.getWidth() / 2) + this.outsideRadius, (float)(this.getHeight() / 2) + this.outsideRadius);
        canvas.drawArc(this.oval, (float)this.startAngle1, (float)this.sweepAngle1, false, this.paint);
        this.paint.setColor(this.insideArcColor);
        this.paint.setStrokeWidth(7.0F);
        this.oval.set((float)(this.getWidth() / 2) - this.insideRadius, (float)(this.getHeight() / 2) - this.insideRadius, (float)(this.getWidth() / 2) + this.insideRadius, (float)(this.getHeight() / 2) + this.insideRadius);
        canvas.drawArc(this.oval, (float)this.startAngle2, (float)this.sweepAngle2, false, this.paint);
    }
}

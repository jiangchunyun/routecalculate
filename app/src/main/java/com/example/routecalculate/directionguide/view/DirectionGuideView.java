package com.example.routecalculate.directionguide.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * this is a view to draw direction guide view.
 *
 * @author daodao
 * @date 2019.1.31
 * @version 1.0
 * @since Android 4.0+
 */

public class DirectionGuideView extends View {
    private Paint mScalePaint;//刻度绘制笔
    private Paint mCirclePaint;//绘制指南针的圆盘笔
    private int x,y;//指南针的圆形
    private int outerRadius;//外圆半径
    private int innerRadius;//内圆半径
    private Path trianglePath;//三角形路径
    private int deviationAngle;//指南针偏移角度


    public DirectionGuideView(Context context) {
        super(context);
        init();
    }

    public DirectionGuideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DirectionGuideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public DirectionGuideView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /*
        设置圆心坐标x，y，外圆，内圆半径，画笔宽度
         */
        y=getMeasuredHeight()/2;
        x=getMeasuredWidth()/2;
        outerRadius=Math.min(getMeasuredHeight(),getMeasuredWidth())/2;
        innerRadius= (int) (outerRadius*0.8);
        mScalePaint.setStrokeWidth(outerRadius/1000);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*
        绘制中间度数，还有内圆和外圆
         */
        canvas.drawText(-deviationAngle+"°",x,y,mScalePaint);
        canvas.rotate(deviationAngle,x,y);
        canvas.drawCircle(x,y,outerRadius,mCirclePaint);
        canvas.drawCircle(x,y,innerRadius,mCirclePaint);

        /*
        绘制三角形指示
         */
        trianglePath=new Path();
        trianglePath.moveTo(x, (float) (outerRadius*0.1));
        trianglePath.lineTo((float) (x*0.95),(float) (outerRadius*0.15));
        trianglePath.lineTo((float) (x*1.05),(float) (outerRadius*0.15));
        trianglePath.close();
        canvas.drawPath(trianglePath,mScalePaint);

        /*
        绘制指南针的刻度和数字以及标识
         */
        for(int i=0;i<360;i=i+2)
        {
            if(i%30==0&&i%90!=0)
            {
                canvas.drawText(String.valueOf(i),x, (float) (outerRadius*0.4),mScalePaint);
            }
            else if(i==0)
            {
                canvas.drawText("N",x, (float) (outerRadius*0.4),mScalePaint);
            }
            else if(i==90)
            {
                canvas.drawText("E",x, (float) (outerRadius*0.4),mScalePaint);
            }
            else if(i==180)
            {
                canvas.drawText("S",x, (float) (outerRadius*0.4),mScalePaint);
            }
            else if(i==270)
            {
                canvas.drawText("W",x, (float) (outerRadius*0.4),mScalePaint);
            }
            canvas.drawLine(x,(float) (outerRadius*0.22),x, (float) (outerRadius*0.3),mScalePaint);
            canvas.rotate(2,x,y);
        }


    }

    /**
     * 初始化数据
     */
    private void init()
    {
        //初始化画笔
        mCirclePaint=new Paint();
        mCirclePaint.setColor(Color.BLUE);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mScalePaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mScalePaint.setColor(Color.RED);

        //初始化偏移角度
        deviationAngle=0;

    }

    /**
     * 设置指南针偏移的角度
     */
    public void setDeviationAngle(int deviationAngle)
    {
        this.deviationAngle=deviationAngle;
    }

    /**
     * 给调用者提供接口来实现获取偏转角度。
     * @return
     */
    public int getDeviationAngle()
    {
        return deviationAngle;
    }
}

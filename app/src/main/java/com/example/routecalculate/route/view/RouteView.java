package com.example.routecalculate.route.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

/**
 * this is a view class to show route.
 *
 * @author daodao
 * @date 2019.2.6
 * @version 1.0
 * @since Android 4.0+
 */

public class RouteView extends View {
    private List<Point> routePoints;//路径绘制点
    private Paint routePaint;//路径画笔
    private Point startPoint;//绘制路径开始坐标点
    private int deviationAngle;//路径绘制的角度

    public RouteView(Context context) {
        super(context);
        init();
    }

    public RouteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RouteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public RouteView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制起始点
        canvas.drawPoint(startPoint.x,startPoint.y,routePaint);
        /*
        将所有的路径点都绘制出来
         */
        for(int i=0;i<routePoints.size();i++)
        {
            canvas.drawPoint(routePoints.get(i).x,routePoints.get(i).y,routePaint);
        }
    }

    /**
     * 初始化
     */
    private void init()
    {
        /*
        初始化各种参数
         */
        routePoints=new ArrayList<Point>();
        startPoint=new Point(500,500);
        routePaint=new Paint();
        routePaint.setColor(Color.BLACK);
        routePaint.setStrokeWidth(10);
        setDeviationAngle(0);
    }

    /**
     * 添加路径
     * @param point An point to add a point
     */
    public void addRoutePoint(Point point)
    {
        routePoints.add(point);
    }

    /**
     * 获取所有的路径点
     * @return the List<Point> to describe path.
     */
    public List<Point> getRoutePoints()
    {
        return routePoints;
    }

    /**
     * 获取view的路径起点
     * @return an Point to describe start Point.
     */
    public Point getStartPoint()
    {
        return startPoint;
    }

    /**
     * 获取路径偏移角度
     * @return an int to return angle.
     */
    public int getDeviationAngle() {
        return deviationAngle;
    }

    /**
     * 设置路径偏移角度
     * @param deviationAngle an int to set Angle.
     */
    public void setDeviationAngle(int deviationAngle) {
        this.deviationAngle = deviationAngle;
    }
}

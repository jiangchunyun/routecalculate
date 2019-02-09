package com.example.routecalculate.directionguide.untils;

import java.util.Stack;

/**
 * this is  a until class to help Sensor.
 *
 * @author daodao
 * @date 2017.2.8
 * @version 1.0
 * @since Android 4.0+
 */

public class SensorUntil {
    private static final int SENSE = 10; // 方向差值灵敏度
    private static final int STOP_COUNT = 2; // 停止次数
    private int initialOrient = -1; // 初始方向
    private int endOrient = -1; // 转动停止方向
    private boolean isRotating = false; // 是否正在转动
    private int lastDOrient = 0; // 上次方向与初始方向差值
    private Stack<Integer> dOrientStack = new Stack<>(); // 历史方向与初始方向的差值栈
    private static volatile SensorUntil instance;//单例变量

    /**
     * 构造函数
     */
    private SensorUntil()
    {

    }

    /**
     * 单例获取函数
     * @return an SensorUntil 返回单例
     */
    public static SensorUntil getInstance()
    {
        if(instance==null)
        {
            synchronized (SensorUntil.class)
            {
                if(instance==null)
                {
                    instance=new SensorUntil();
                }
            }
        }
        return instance;
    }



    /**
     * 获取手机转动停止的方向
     * @param orient 手机实时方向
     */
    public int getRotateEndOrient(int orient) {
        if (initialOrient == -1) {
            // 初始化转动
            endOrient = initialOrient = orient;
        }

        int currentDOrient = Math.abs(orient - initialOrient); // 当前方向与初始方向差值
        if (!isRotating) {
            // 检测是否开始转动
            lastDOrient = currentDOrient;
            if (lastDOrient >= SENSE) {
                // 开始转动
                isRotating = true;
            }
        } else {
            // 检测是否停止转动
            if (currentDOrient <= lastDOrient) {
                // 至少累计STOP_COUNT次出现当前方向差小于上次方向差
                int size = dOrientStack.size();
                if (size >= STOP_COUNT) {
                    // 只有以前SENSE次方向差距与当前差距的差值都小于等于SENSE，才判断为停止
                    for (int i = 0; i < size; i++) {
                        if (Math.abs(currentDOrient - dOrientStack.pop()) >= SENSE) {
                            isRotating = true;
                            break;
                        }
                        isRotating = false;
                    }
                }

                if (!isRotating) {
                    // 停止转动
                    dOrientStack.clear();
                    initialOrient = -1;
                    endOrient = orient;

                } else {
                    // 正在转动，把当前方向与初始方向差值入栈
                    dOrientStack.push(currentDOrient);
                }
            } else {
                lastDOrient = currentDOrient;
            }
        }
        return endOrient;
    }
}

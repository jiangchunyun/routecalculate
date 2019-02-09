package com.example.routecalculate.directionguide.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.example.routecalculate.main.activity.UpdateUiCallback;

/**
 * this is a servie to guide direction.
 *
 * @author daodao
 * @date 2019.2.6
 * @version 1.0
 * @since Android 4.0+
 */

public class DirectionGuideServie extends Service implements SensorEventListener{
    private DirectionGuideBinder mDirectionGuideBinder=new DirectionGuideBinder();//给调用者返回方便调用
    private SensorManager mSensorManager;//传感器管理器
    private Sensor mDirectionSensor;//方向传感器
    private UpdateUiCallback mupdateStepCallBack;//更新UI回调。
    int deviationAngle;//指南针角度

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(){
            @Override
            public void run() {
                super.run();
                addDirectionGuideListener();
            }
        }.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mDirectionGuideBinder;
    }

    /**
     * 添加方向监听器
     */
    public void addDirectionGuideListener()
    {
        mSensorManager= (SensorManager) getSystemService(SENSOR_SERVICE);
        mDirectionSensor=mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mSensorManager.registerListener(this,mDirectionSensor,SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * 注册更新view的回调函数
     * @param updateStepCallBack the UpdateUiCallback to register to stepService.
     */
    public void registerUpdateUiCallback(UpdateUiCallback updateStepCallBack)
    {
        this.mupdateStepCallBack =updateStepCallBack;
    }

    /**
     * 更新UI
     */
    public void updateUi()
    {
        if(mupdateStepCallBack!=null) {
            mupdateStepCallBack.updateUi(deviationAngle);
        }
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        deviationAngle= -(int) sensorEvent.values[0];
        updateUi();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    /**
     * this is a inner class binder.
     */
    public class DirectionGuideBinder extends Binder{
        public DirectionGuideServie getService()
        {
            return DirectionGuideServie.this;
        }
    }
}

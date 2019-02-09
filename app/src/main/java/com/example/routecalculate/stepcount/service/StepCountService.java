package com.example.routecalculate.stepcount.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.routecalculate.main.activity.UpdateUiCallback;

/**
 * this class is a service to calculate step counts.
 *
 * @author daodao
 * @version 1.0
 * @sing Android 4.0+
 */

public class StepCountService extends Service implements SensorEventListener {
    private String TAG = "StepCountService";//标识tag
    private final StepBinder mBinder = new StepBinder();//返回给调用者的binder，方便通信。
    private UpdateUiCallback mupdateStepCallBack;//更新UI回调。
    private int step = 0;//步数
    private SensorManager msensorManager;//计步管理器

    @Override
    public void onCreate() {
        super.onCreate();
        /*
        开启一个线程完成监听事件
         */
        new Thread() {
            @Override
            public void run() {
                super.run();
                addCountStepListener();
            }
        }.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, ":onbind");
        return mBinder;
    }

    /**
     * 注册更新view的回调函数
     *
     * @param updateStepCallBack the UpdateUiCallback to register to stepService.
     */
    public void registerUpdateUiCallback(UpdateUiCallback updateStepCallBack) {
        this.mupdateStepCallBack = updateStepCallBack;
    }

    /**
     * 更新UI
     */
    public void updateUi() {
        if (mupdateStepCallBack != null) {
            mupdateStepCallBack.updateUi(step);
        }
    }

    /**
     * 添加计步器监听事件
     */
    public void addCountStepListener() {
        msensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor countSensor = msensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        msensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.values[0] == 1.0)
            step++;
        updateUi();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    /**
     * this is an inner class to provide called getService.
     */
    public class StepBinder extends Binder {
        /**
         * 给调用者返回service。
         *
         * @return service 对象
         */
        public StepCountService getService() {
            return StepCountService.this;
        }
    }
}

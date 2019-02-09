package com.example.routecalculate.main.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Point;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.routecalculate.R;
import com.example.routecalculate.directionguide.service.DirectionGuideServie;
import com.example.routecalculate.directionguide.untils.SensorUntil;
import com.example.routecalculate.directionguide.view.DirectionGuideView;
import com.example.routecalculate.route.view.RouteView;
import com.example.routecalculate.stepcount.service.StepCountService;

/**
 * this is a activity to show step.
 *
 * @author daodao
 * @version 1.0
 * @since Android 4.0+
 */
public class MainActivity extends AppCompatActivity {
    private TextView mShowStepTV;//显示步数
    private Button mStartServiceBTN;//开启计步服务
    private StepCountService mstepCountService;//计步服务索引
    private DirectionGuideServie mDirectionGuideServie;//指南针服务
    private DirectionGuideView mDirectionGuideView;//指南针view
    private RouteView mRouteView;//显示路径
    private int oldX;//路径前一个点的x坐标
    private int oldY;//路径前一个点的y坐标

    /*
      service更新uihandler
     */
    private Handler serviceUpdateUiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1: {
                    int currentStep = msg.arg1;
                    int currentDeviationAngle=msg.arg2;
                    int dx= (int) (50 * Math.sin(Math.toRadians(currentDeviationAngle)));
                    int dy= (int) (50 * Math.cos(Math.toRadians(currentDeviationAngle)));
                    int x = (int) (oldX+dx);
                    int y = (int) (oldY+dy);
                    Log.i("test","x:"+x+","+"y:"+y+"   ");
                    mRouteView.addRoutePoint(new Point(x, y));
                    oldY=y;
                    oldX=x;
                    mRouteView.invalidate();
                    mShowStepTV.setText( "步数: " + currentStep);

                    break;
                }
                case 2: {
                    int deviationAngle = msg.arg1;
                    mRouteView.setDeviationAngle(SensorUntil.getInstance().getRotateEndOrient(-deviationAngle));;
                    mDirectionGuideView.setDeviationAngle(deviationAngle);
                    mDirectionGuideView.invalidate();
                    break;
                }
            }

        }
    };

    /*
      stepService连接器
     */
    private ServiceConnection mstepCountServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mstepCountService = ((StepCountService.StepBinder) iBinder).getService();
            mstepCountService.registerUpdateUiCallback(new UpdateUiCallback() {
                @Override
                public void updateUi(int value) {
                    Message updateUiMessage = Message.obtain();
                    updateUiMessage.what = 1;
                    updateUiMessage.arg1 = value;
                    updateUiMessage.arg2= mRouteView.getDeviationAngle();
                    serviceUpdateUiHandler.sendMessage(updateUiMessage);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    /*
        DirectionGuideService连接器
     */
    private ServiceConnection mDirectionGuideServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mDirectionGuideServie = ((DirectionGuideServie.DirectionGuideBinder) iBinder).getService();
            mDirectionGuideServie.registerUpdateUiCallback(new UpdateUiCallback() {
                @Override
                public void updateUi(int value) {
                    Message updateUiMessage = Message.obtain();
                    updateUiMessage.what = 2;
                    updateUiMessage.arg1 = value;
                    serviceUpdateUiHandler.sendMessage(updateUiMessage);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setLitener();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent stepService = new Intent(this, StepCountService.class);
        Intent startDirectionGuideIntent = new Intent(this, DirectionGuideServie.class);
        stopService(stepService);
        stopService(startDirectionGuideIntent);
    }

    /**
     * 初始化界面
     */
    public void init() {
        mShowStepTV = (TextView) findViewById(R.id.tv_stepcount_showstep);
        mStartServiceBTN = (Button) findViewById(R.id.btn_stepcount_startservice);
        mRouteView = (RouteView) findViewById(R.id.routeview_stepcount_show);
        oldX=mRouteView.getStartPoint().x;
        oldY=mRouteView.getStartPoint().y;
        mDirectionGuideView = (DirectionGuideView) findViewById(R.id.directionGuideView_directionguide_show);
        Intent startDirectionGuideIntent = new Intent(this, DirectionGuideServie.class);
        bindService(startDirectionGuideIntent, mDirectionGuideServiceConnection, Context.BIND_AUTO_CREATE);

    }

    /**
     * 设置监听事件
     */
    public void setLitener() {
        mStartServiceBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startStepService();
            }
        });
    }

    /**
     * 开启计步服务
     */
    public void startStepService() {
        Intent startStepService = new Intent(this, StepCountService.class);
        bindService(startStepService, mstepCountServiceConnection, Context.BIND_AUTO_CREATE);
    }

}

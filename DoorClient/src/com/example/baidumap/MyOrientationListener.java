package com.example.baidumap;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MyOrientationListener implements SensorEventListener
{

	private Context context;
	private SensorManager sensorManager;
	private Sensor sensor;
	
	private float lastX ; 
	
	private OnOrientationListener onOrientationListener ; 

	public MyOrientationListener(Context context)
	{
		this.context = context;
	}

	// �??�??
	@SuppressWarnings("deprecation")
	public void start()
	{
		// 获得传感器管理器
		sensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		if (sensorManager != null)
		{
			// 获得方向传感�??
			sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		}
		// 注册
		if (sensor != null)
		{//SensorManager.SENSOR_DELAY_UI
			sensorManager.registerListener(this, sensor,
					SensorManager.SENSOR_DELAY_UI);
		}

	}

	// 停止�??�??
	public void stop()
	{
		sensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onSensorChanged(SensorEvent event)
	{
		// 接受方向感应器的类型  
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION)  
        {  
            // 这里我们可以得到数据，然后根据需要来处理  
            float x = event.values[SensorManager.DATA_X];  
            
            if( Math.abs(x- lastX) > 0.5 )
            {
            	onOrientationListener.onOrientationChanged(x);
            }

            lastX = x ; 
            
        }  
	}
	
	public void setOnOrientationListener(OnOrientationListener onOrientationListener)
	{
		this.onOrientationListener = onOrientationListener ;
	}
	
	
	public interface OnOrientationListener 
	{
		void onOrientationChanged(float x);
	}

}

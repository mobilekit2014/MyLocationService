package com.test.servicetest;

import java.io.File;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

public class service extends Service{

	 String string;
	 int time =1;
	 
	 public void onCreate(){
		super.onCreate();
		Toast.makeText(getBaseContext(), "Service is Created", 0).show();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
	
		Toast.makeText(getBaseContext(), "Service is Started", 0).show();

		Threadtest thread = new Threadtest(this, mHandler);
		thread.start();
		return super.onStartCommand(intent, flags, startId);
	}

	
	class Threadtest extends Thread{
		service mParent;
		Handler mHandler;
		public Threadtest(service mParent, Handler mHandler){
			this.mParent = mParent;
			this.mHandler = mHandler;
		}
		public void run(){
				int i;
				for(i=0;i<10;i++){
					Message msg = new Message();
					mHandler.sendMessage(msg);
				}
				try{
					Thread.sleep(5000);
					time+=5;
				}catch(Exception e){;};
		}
	}
	
	Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			startLocationService();
		}
	};
	
	Runnable task = new Runnable(){
		public void run(){
			try{
				Toast.makeText(getBaseContext(), "thread start", 0).show();
				//startLocationService();
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Toast.makeText(getBaseContext(), "Service is destroyed", 0).show();
		super.onDestroy();
	}
	
	 private class MyLocationListener implements LocationListener 
	 {
	        @Override
	        public void onLocationChanged(Location loc) {
	        	Double latitude = loc.getLatitude();
				Double longitude = loc.getLongitude();

				String msg = "���� : "+ latitude + "\n�浵:"+ longitude;
				//Log.i("GPSListener", msg);
				
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	        }

			@Override
			public void onProviderDisabled(String arg0) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onProviderEnabled(String arg0) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
				// TODO Auto-generated method stub	
			}
	 }

	 
	 private void startLocationService() {
	    	// ��ġ ������ ��ü ����
	    	LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

			// ��ġ ������ ���� ������ ����
	    	MyLocationListener gpsListener = new MyLocationListener();
			long minTime = 10000;
			float minDistance = 0;

			// GPS�� �̿��� ��ġ ��û
			manager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER,
						minTime,
						minDistance,
						gpsListener);

			// ��Ʈ��ũ�� �̿��� ��ġ ��û
			manager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER,
					minTime,
					minDistance,
					gpsListener);

			// ��ġ Ȯ���� �ȵǴ� ��쿡�� �ֱٿ� Ȯ�ε� ��ġ ���� ���� Ȯ��
			try {
				Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if (lastLocation != null) {
					Double latitude = lastLocation.getLatitude();
					Double longitude = lastLocation.getLongitude();

					Toast.makeText(getApplicationContext(),  "Last Latitude : "+ latitude + "\nLast Longitude:"+ longitude, Toast.LENGTH_LONG).show();
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			}

			Toast.makeText(getApplicationContext(), "��ġ Ȯ���� ���۵Ǿ����ϴ�.", Toast.LENGTH_SHORT).show();

	    }
}

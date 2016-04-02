package com.example.wealther.app.service;

import com.example.wealther.app.receiver.AutoUpdateReceiver;
import com.example.wealther.app.util.HttpCallbackListener;
import com.example.wealther.app.util.HttpUtil;
import com.example.wealther.app.util.Utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

public class AutoUpdateService extends Service{

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				updatewealther();
			}
		}).start();
		AlarmManager manager=(AlarmManager) getSystemService(ALARM_SERVICE);
		int anHour=8*60*60*1000;
		long triggerAtTime=SystemClock.elapsedRealtime() + anHour;
		Intent i=new Intent(this,AutoUpdateReceiver.class);
		PendingIntent pi=PendingIntent.getBroadcast(this,0,i,0);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
		return super.onStartCommand(intent, flags, startId);
	}
	protected void updatewealther() {
		// TODO Auto-generated method stub
		SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
		String wealtherCode=prefs.getString("wealther_code","");
		String address="http://www.weather.com.cn/data/cityinfo/" + wealtherCode + ".html";
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				Utility.handleWealtherResponse(AutoUpdateService.this, response);
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				e.printStackTrace();
			}
		});
	}

}

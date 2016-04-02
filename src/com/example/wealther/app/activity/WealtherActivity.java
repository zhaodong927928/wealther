package com.example.wealther.app.activity;


import com.example.wealther.R;
import com.example.wealther.app.util.HttpCallbackListener;
import com.example.wealther.app.util.HttpUtil;
import com.example.wealther.app.util.Utility;

import android.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WealtherActivity extends Activity implements OnClickListener{
private LinearLayout wealtherInfoLayout;
private TextView cityNameText;
private TextView publishText;
private TextView wealtherDespText;
private TextView temp1Text;
private TextView temp2Text;
private TextView currentDataText;
private Button switchCity,refreshWeather;
@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.wealther_layout);
		wealtherInfoLayout=(LinearLayout) findViewById(R.id.wealther_info_layout);
		cityNameText=(TextView) findViewById(R.id.city_name);
		publishText=(TextView) findViewById(R.id.publish_text);
		wealtherDespText=(TextView) findViewById(R.id.wealther_desp);
		temp1Text=(TextView) findViewById(R.id.temp1);
		temp2Text=(TextView) findViewById(R.id.temp2);
		currentDataText=(TextView) findViewById(R.id.current_date);
		switchCity=(Button) findViewById(R.id.switch_city);
		refreshWeather=(Button) findViewById(R.id.refresh_wealther);
		switchCity.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);
		String countyCode=getIntent().getStringExtra("county_code");
		if(!TextUtils.isEmpty(countyCode)){
			publishText.setText("同步中。。。");
			wealtherInfoLayout.setVisibility(View.INVISIBLE);
			queryWealtherCode(countyCode);
		}else{
			showWealther();
		}
	}
private void showWealther() {
	// TODO Auto-generated method stub
	SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
	cityNameText.setText(prefs.getString("city_name",""));
	temp1Text.setText(prefs.getString("temp1",""));
	temp2Text.setText(prefs.getString("temp2",""));
	wealtherDespText.setText(prefs.getString("wealther_desp",""));
	publishText.setText(prefs.getString("publish_time",""));
	currentDataText.setText(prefs.getString("current_data", ""));
	wealtherInfoLayout.setVisibility(View.VISIBLE);
	cityNameText.setVisibility(View.VISIBLE);
}
private void queryWealtherCode(String countyCode) {
	// TODO Auto-generated method stub
	String address="http://wwww.weather.com.cn/data/list3/city" + countyCode + ".xml";
	queryFromServer(address,"countyCode");
}
private void queryWealtherInfo(String wealtherCode) {
	// TODO Auto-generated method stub
String address="http://www.weather.com.cn/data/cityinfo/" + wealtherCode +".html";
queryFromServer(address, "wealtherCode");
}
private void queryFromServer(final String address,final String type) {
	// TODO Auto-generated method stub
	HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
		
		@Override
		public void onFinish(final String response) {
			// TODO Auto-generated method stub
			if("countyCode".equals(type)){
				if(!TextUtils.isEmpty(response)){
					String[] array=response.split("\\|");
					if(array!=null&&array.length==2){
						String wealtherCode=array[1];
						queryWealtherInfo(wealtherCode);
					}
				}
			}else if("wealtherCode".equals(type)){
				Utility.handleWealtherResponse(WealtherActivity.this, response);
				runOnUiThread(new Runnable(){
					@Override
					public void run() {
						// TODO Auto-generated method stub
					showWealther();	
					}
				});
			}
		}
		
		@Override
		public void onError(Exception e) {
			// TODO Auto-generated method stub
			runOnUiThread(new Runnable(){
				public void run(){
					publishText.setText("同步失败");
				}
			});
		}
	});
}
@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	switch (v.getId()){
	case R.id.switch_city:
		Intent intent=new Intent(this,ChooseAreaActivity.class);
		intent.putExtra("from_wealther_activity", true);
		startActivity(intent);
		finish();
		break;
		case R.id.refresh_wealther:
			publishText.setText("同步中。。。");
			SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
			String wealtherCode=prefs.getString("wealther_code","");
			if(!TextUtils.isEmpty(wealtherCode)){
				queryWealtherInfo(wealtherCode);
			}break;
			default:
				break;
	}
}
}
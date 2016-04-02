package com.example.wealther.app.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.wealther.R;
import com.example.wealther.app.model.city;
import com.example.wealther.app.model.county;
import com.example.wealther.app.model.province;
import com.example.wealther.app.util.HttpCallbackListener;
import com.example.wealther.app.util.HttpUtil;
import com.example.wealther.app.util.Utility;


import android.app.Activity;
import android.app.DownloadManager.Query;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity{
public static final int LEVEL_PROVINCE=0;
public static final int LEVEL_CIRY=1;
public static final int LEVEL_COUNTY=2;
protected static final int index = 0;
private ProgressDialog progressDialog;
private TextView titleText;
private ListView listView;
private ArrayAdapter<String> adapter;
private com.example.wealther.app.db.wealtherDB wealtherDB;
private List<String> dataList=new ArrayList<String>();
private List<province> provinceList;
private List<city> cityList;
private List<county> countyList;
private province selectedProvince;
private boolean isFromWealtherActivity;
private int currentLevel;
private city selectedCity;



@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	isFromWealtherActivity=getIntent().getBooleanExtra("from_wealther_activity", false);
	SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
	if(prefs.getBoolean("city_selected",false&&!isFromWealtherActivity)){
		Intent intent =new Intent(this,WealtherActivity.class);
		startActivity(intent);
		finish();
		return;
	}
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.choose_area);
	listView=(ListView) findViewById(R.id.list_view);
	titleText=(TextView) findViewById(R.id.title_text);
	adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataList);
	listView.setAdapter(adapter);
	wealtherDB=wealtherDB.getInstance(this);
	listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
					if(currentLevel==LEVEL_PROVINCE){
						selectedProvince=provinceList.get(index);
						queryCities();
					}else if(currentLevel==LEVEL_CIRY){
						selectedCity=cityList.get(index);
						queryCounties();
					}else if(currentLevel==LEVEL_COUNTY){
						String countyCode=countyList.get(index).getCountyCode();
						Intent intent=new Intent(ChooseAreaActivity.this,WealtherActivity.class);
						intent.putExtra("county_code",countyCode);
						startActivity(intent);
						finish();
					}
					}
	});queryProvinces();
}

private void queryProvinces() {
	// TODO Auto-generated method stub
	provinceList=wealtherDB.loadProvinces();
	if(provinceList.size()>0){
		dataList.clear();
		for(province province:provinceList){
			dataList.add(province.getProvincename());
		}
		adapter.notifyDataSetChanged();
		listView.setSelection(0);
		titleText.setText("中国");
		currentLevel=LEVEL_PROVINCE;
	}else{queryFromServer(null,"province");}
}



protected void queryCities() {
	// TODO Auto-generated method stub
	cityList=wealtherDB.loadCities(selectedProvince.getId());
	if(cityList.size()>0){
		dataList.clear();
		for(city city:cityList){
			dataList.add(city.getCityName());
		}adapter.notifyDataSetChanged();
		listView.setSelection(0);
		titleText.setText(selectedProvince.getProvincename());
		currentLevel=LEVEL_CIRY;
	}else{
		queryFromServer(selectedProvince.getProvincecode(),"city");
	}
}


private String getProvinceCode() {
	// TODO Auto-generated method stub
	return null;
}

protected void queryCounties() {
	// TODO Auto-generated method stub
	countyList=wealtherDB.loadCounties(selectedCity.getId());
	if(countyList.size()>0){
		dataList.clear();
		for(county county:countyList){
			dataList.add(county.getCountyName());
		}
		adapter.notifyDataSetChanged();
		listView.setSelection(0);
		titleText.setText(selectedCity.getCityName());
		currentLevel=LEVEL_COUNTY;
	}else{
		queryFromServer(selectedCity.getCityCode(),"county");
	}
}

private void queryFromServer(final String code,final String type) {
	// TODO Auto-generated method stub
	String address;
	if(!TextUtils.isEmpty(code)){
		address="http://www.weather.com.cn/data/list3/city" + code +".xml";
	}else{address="http://www.weather.com.cn/data/list3/city.xml";}

showProgressDialog();
HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
	@Override
	public void onFinish(String response) {
		// TODO Auto-generated method stub
boolean result=false;
if("province".equals(type)){
	result=Utility.handleprovinceResponse(wealtherDB,response);
}else if("city".equals(type)){
	result=Utility.handlecityResponse(wealtherDB,response,selectedProvince.getId());
}else if("county".equals(type)){
	result=Utility.handlecountyResponse(wealtherDB,response,selectedCity.getId());
}if(result){
	runOnUiThread(new Runnable(){
		@Override
		public void run() {
			closeProgressDialog();
			if("province".equals(type)){
				queryProvinces();
			}else if("city".equals(type)){
				queryCities();
			}else if("county".equals(type)){
				queryCounties();
			}
		}
	});
}
	}
	@Override
	public void onError(Exception e) {
		// TODO Auto-generated method stub
runOnUiThread(new Runnable(){
	@Override
	public void run() {
		closeProgressDialog();
		Toast.makeText(ChooseAreaActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
	}
});
	}
});
}
private void showProgressDialog(){
	if(progressDialog==null){progressDialog=new ProgressDialog(this);
	progressDialog.setMessage("正在加载中。。。");
		progressDialog.setCanceledOnTouchOutside(false);
	}
	progressDialog.show();
}
private void closeProgressDialog() {
	// TODO Auto-generated method stub
if(progressDialog!=null){
	progressDialog.dismiss();
}
}

@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(currentLevel==LEVEL_COUNTY){
			queryCities();
		}else if(currentLevel==LEVEL_CIRY){
			queryProvinces();
		}else{
			if(isFromWealtherActivity){
				Intent intent=new Intent(this,WealtherActivity.class);
				startActivity(intent);
			}finish();}
	}
}
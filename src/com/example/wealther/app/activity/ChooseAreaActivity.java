package com.example.wealther.app.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.wealther.R;
import com.example.wealther.app.model.city;
import com.example.wealther.app.model.county;
import com.example.wealther.app.model.province;


import android.app.Activity;
import android.app.DownloadManager.Query;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ChooseAreaActivity extends Activity{
public static final int LEVEL_PROVINCE=0;
public static final int LEVEL_CIRY=1;
public static final int LEVEL_COUNTY=2;
private ProgressDialog progressDialog;
private TextView titleText;
private ListView listView;
private ArrayAdapter<String> adapter;
private com.example.wealther.app.db.wealtherDB wealtherDB;
private List<String> dataList=new ArrayList<String>();
private List<province> provinceList;
private List<city> cityList;
private List<county> countyList;
private province selelctedProvince;
private city selelctedCity;
private int currentLevel;
protected int index;
private city selectedCity;

@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
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
						selelctedProvince=provinceList.get(index);
						queryCities();
					}else if(currentLevel==LEVEL_CIRY){
						selelctedCity=cityList.get(index);
						queryCounties();
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
		titleText.setText("ÖÐ¹ú");
		currentLevel=LEVEL_PROVINCE;
	}else{queryFromServer(null,"province", null);}
}



protected void queryCities() {
	// TODO Auto-generated method stub
	cityList=wealtherDB.loadCities(selelctedProvince.getId());
	if(cityList.size()>0){
		dataList.clear();
		for(city city:cityList){
			dataList.add(city.getCityName());
		}adapter.notifyDataSetChanged();
		listView.setSelection(0);
		titleText.setText(selelctedProvince.getProvincename());
		currentLevel=LEVEL_CIRY;
	}else{
		queryFromServer(selelctedProvince, getProvinceCode(),"city");
	}
}


protected void queryCounties() {
	// TODO Auto-generated method stub
	countyList=wealtherDB.loadCounties(selelctedCity.getId());
	if(countyList.size()>0){
		dataList.clear();
		for(county county:countyList){
			dataList.add(county.getCountyName());
		}
		adapter.notifyDataSetChanged();
		listView.setSelection(0);
		titleText.setText(selelctedCity.getCityName());
		currentLevel=LEVEL_COUNTY;
	}else{
		queryFromServer(selectedCity.getCityCode(),"county");
	}
}

private void queryFromServer(String cityCode, String string) {
	// TODO Auto-generated method stub
	
}

private void queryFromServer(province selelctedProvince, String cityCode, String string) {
	// TODO Auto-generated method stub
	
}
}

package com.example.wealther.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.SynchronousQueue;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.wealther.app.db.wealtherDB;
import com.example.wealther.app.model.city;
import com.example.wealther.app.model.county;
import com.example.wealther.app.model.province;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;


public class Utility {
public synchronized static boolean handleprovinceResponse(wealtherDB wealtherDB,String response){
	if(!TextUtils.isEmpty(response)){
		String[] allProvince=response.split(",");
		if (allProvince!=null&&allProvince.length>0){
			for(String p:allProvince){
				String[] array=p.split("\\|");
				province province=new province();
				province.setProvincecode(array[0]);
				province.setProvincename(array[1]);
				wealtherDB.saveprovince(province);
			}return true;
		}
	}
	return false;}
public synchronized static boolean handlecityResponse(wealtherDB wealtherDB,String response, int provinceid){
	if(!TextUtils.isEmpty(response)){
		String[] allCity=response.split(",");
		if (allCity!=null&&allCity.length>0){
			for(String p:allCity){
				String[] array=p.split("\\|");
				city city=new city();
				city.setCityCode(array[0]);
				city.setCityName(array[1]);
				city.setProvinceid(provinceid);
				wealtherDB.savecity(city);
			}return true;
		}
	}
	return false;}
public synchronized static boolean handlecountyResponse(wealtherDB wealtherDB,String response, int cityid){
	if(!TextUtils.isEmpty(response)){
		String[] allCounty=response.split(",");
		if (allCounty!=null&&allCounty.length>0){
			for(String p:allCounty){
				String[]array=p.split("\\|");
				county county=new county();
				county.setCountyCode(array[0]);
				county.setCountyName(array[1]);
				county.setCityid(cityid);
				wealtherDB.savecounty(county);
			}return true;
		}
	}
	return false;}
public static void handleWealtherResponse(Context context,String response) {
	// TODO Auto-generated method stub
try{JSONObject jsonobject=new JSONObject(response);
JSONObject wealtherInfo=jsonobject.getJSONObject("wealtherinfo");
String cityName=wealtherInfo.getString("city");
String wealtherCode=wealtherInfo.getString("cityid");
String temp1=wealtherInfo.getString("temp1");
String temp2=wealtherInfo.getString("temp2");
String wealtherDesp=wealtherInfo.getString("wealther");
String publishTime=wealtherInfo.getString("ptime");
saveWealtherInfo(context,cityName,wealtherCode,temp1,temp2,wealtherDesp,publishTime);}catch(JSONException e){
	e.printStackTrace();
}
}
public static void saveWealtherInfo(Context context, String cityName,
		String wealtherCode, String temp1, String temp2, String wealtherDesp,
		String publishTime) {
	// TODO Auto-generated method stub
	SimpleDateFormat sdf=new SimpleDateFormat("yyyyƒÍM‘¬d»’",Locale.CHINA);
	SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(context).edit();
	editor.putBoolean("city_selected",true);
	editor.putString("city_name",cityName);
	editor.putString("wealther_code",wealtherCode);
	editor.putString("temp1", temp1);
	editor.putString("temp2",temp2);
	editor.putString("wealtherDesp", wealtherDesp);
	editor.putString("publish_time",publishTime);
	editor.putString("current_daye",sdf.format(new Date()));
	editor.commit();
}
}

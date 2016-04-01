package com.example.wealther.app.util;

import java.util.concurrent.SynchronousQueue;

import com.example.wealther.app.db.wealtherDB;
import com.example.wealther.app.model.city;
import com.example.wealther.app.model.county;
import com.example.wealther.app.model.province;
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
}

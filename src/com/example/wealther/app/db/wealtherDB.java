package com.example.wealther.app.db;

import java.util.ArrayList;
import java.util.List;

import com.example.wealther.app.model.city;
import com.example.wealther.app.model.county;
import com.example.wealther.app.model.province;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class wealtherDB {
public static final String DB_NAME="wealther";
public static final int VERSION=1;
private static wealtherDB wealtherDB;
private SQLiteDatabase db;


private wealtherDB(Context context){
	wealtherOpenhelper dbhelper=new wealtherOpenhelper(context, DB_NAME, null, VERSION);
	db=dbhelper.getWritableDatabase();
}
public synchronized static wealtherDB getInstance(Context context) {
	if(wealtherDB==null){
		wealtherDB=new wealtherDB(context);
	}
	return wealtherDB;
}
public void saveprovince(province province) {
	// TODO Auto-generated method stub
if (province!=null){
	ContentValues values=new ContentValues();
	values.put("province_name",province.getProvincename());
	values.put("province_code",province.getProvincecode());
	db.insert("Province", null, values);
}
}
public List<province> loadProvinces(){
	List<province> list=new ArrayList<province>();
	Cursor cursor=db.query("Province",null,null,null,null,null,null);
	if(cursor.moveToFirst()){
		do{
			province province=new province();
			province.setId(cursor.getInt(cursor.getColumnIndex("id")));
			province.setProvincename(cursor.getString(cursor.getColumnIndex("province_name")));
			province.setProvincecode(cursor.getString(cursor.getColumnIndex("province_code")));
			list.add(province);
		}while(cursor.moveToNext());
	}if (cursor!=null){
		cursor.close();
	}return list;
}
public void savecity(city city) {
	// TODO Auto-generated method stub
if (city!=null){
	ContentValues values=new ContentValues();
	values.put("city_name",city.getCityName());
	values.put("city_code",city.getCityCode());
	values.put("province_id",city.getProvinceid());
	db.insert("city", null, values);
}
}
public List<city> loadCities(int provinceId){
	List<city> list=new ArrayList<city>();
	Cursor cursor=db.query("City",null,"province_id=?",new String[]{String.valueOf(provinceId)},null,null,null);
	if(cursor.moveToFirst()){
		do{
			city city=new city();
			city.setId(cursor.getInt(cursor.getColumnIndex("id")));
			city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
			city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
			list.add(city);
		}while(cursor.moveToNext());
	}if (cursor!=null){
		cursor.close();
	}return list;
}
public void savecounty(county county) {
	// TODO Auto-generated method stub
if (county!=null){
	ContentValues values=new ContentValues();
	values.put("county_name",county.getCountyName());
	values.put("county_code",county.getCountyCode());
	values.put("city_id",county.getCityid());
	db.insert("county", null, values);
}
}
public List<county> loadCounties(int cityId){
	List<county> list=new ArrayList<county>();
	Cursor cursor=db.query("County",null,"city_id=?",new String[]{String.valueOf(cityId)},null,null,null);
	if(cursor.moveToFirst()){
		do{
			county county=new county();
			county.setId(cursor.getInt(cursor.getColumnIndex("id")));
			county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
			county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
			list.add(county);
		}while(cursor.moveToNext());
	}if (cursor!=null){
		cursor.close();
	}return list;
}


}

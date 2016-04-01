package com.example.wealther.app.model;

public class city {
private int id;
private String city_Name;
private String city_Code;
private int province_id;
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getCityName() {
	return city_Name;
}
public void setCityName(String cityName) {
	this.city_Name = cityName;
}
public String getCityCode() {
	return city_Code;
}
public void setCityCode(String cityCode) {
	this.city_Code = cityCode;
}
public int getProvinceid() {
	return province_id;
}
public void setProvinceid(int provinceid) {
	this.province_id = provinceid;
}
}

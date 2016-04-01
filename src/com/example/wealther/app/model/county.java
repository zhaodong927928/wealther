package com.example.wealther.app.model;

public class county {
private int id;
private String county_Name;
private String county_Code;
private int city_id;
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getCountyName() {
	return county_Name;
}
public void setCountyName(String countyName) {
	this.county_Name = countyName;
}
public String getCountyCode() {
	return county_Code;
}
public void setCountyCode(String countyCode) {
	this.county_Code = countyCode;
}
public int getCityid() {
	return city_id;
}
public void setCityid(int cityid) {
	this.city_id = cityid;
}
}

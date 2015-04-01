package com.example.zf_pad.entity;

public class AdressEntity {
 
//    "id": 70,
//    "city_parent_name": "甘肃�?",
//    "city_name": "定西�?",
//    "customerId": 80,
//    "receiver": "四十�?",
//    "isDefault": 1,
//    "address": "电饭锅给红红火火红红火火",
//    "cityId": 244,
//    "city_parent_id": 21,
//    "zipCode": "3456666",
//    "moblephone": "17887986789",
//    "city": "甘肃省定西市"
	private Boolean Ischeck;
	
	public Boolean getIscheck() {
		return Ischeck;
	}
	public void setIscheck(Boolean ischeck) {
		Ischeck = ischeck;
	}
	private int id;
	private String city_name;
	private int customerId;
	private String receiver;
	private int isDefault;
	private String address;
	private int cityId;
	private int city_parent_id;
	private String zipCode;
	private String moblephone;
	private String city;
	private String city_parent_name;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public int getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(int isDefault) {
		this.isDefault = isDefault;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public int getCity_parent_id() {
		return city_parent_id;
	}
	public void setCity_parent_id(int city_parent_id) {
		this.city_parent_id = city_parent_id;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getMoblephone() {
		return moblephone;
	}
	public void setMoblephone(String moblephone) {
		this.moblephone = moblephone;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCity_parent_name() {
		return city_parent_name;
	}
	public void setCity_parent_name(String city_parent_name) {
		this.city_parent_name = city_parent_name;
	}
	
	
	
	
	
	
}

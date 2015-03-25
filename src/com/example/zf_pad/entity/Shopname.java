package com.example.zf_pad.entity;

public class Shopname {
private long id;
private String shopname;
public Shopname(long id,String shopname){
	super();
	this.id=id;
	this.shopname=shopname;
}
public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
public String getShopname() {
	return shopname;
}
public void setShopname(String shopname) {
	this.shopname = shopname;
}
@Override
public String toString() {
	return "Shopname [id=" + id + ", shopname=" + shopname + "]";
}

}

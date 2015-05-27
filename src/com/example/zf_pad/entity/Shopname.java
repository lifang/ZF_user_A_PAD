package com.example.zf_pad.entity;

public class Shopname {
private long id;
private String shopname;
private String title;
private String legal_person_name;
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String getLegal_person_name() {
	return legal_person_name;
}
public void setLegal_person_name(String legal_person_name) {
	this.legal_person_name = legal_person_name;
}
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

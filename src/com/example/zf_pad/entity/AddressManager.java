package com.example.zf_pad.entity;

public class AddressManager {
private long id;
private String consignee;
private String area;
private String detailadress;
private String zipcode;
private String phone;
private String isdefau;
public AddressManager(long id,String consignee,String area,String detailadress,
		String zipcode,String phone,String isdefau){
	super();
	this.id=id;
	this.consignee=consignee;
	this.area=area;
	this.detailadress=detailadress;
	this.zipcode=zipcode;
	this.phone=phone;
	this.isdefau=isdefau;
}
public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
public String getConsignee() {
	return consignee;
}
public void setConsignee(String consignee) {
	this.consignee = consignee;
}
public String getArea() {
	return area;
}
public void setArea(String area) {
	this.area = area;
}
public String getDetailadress() {
	return detailadress;
}
public void setDetailadress(String detailadress) {
	this.detailadress = detailadress;
}
public String getZipcode() {
	return zipcode;
}
public void setZipcode(String zipcode) {
	this.zipcode = zipcode;
}
public String getPhone() {
	return phone;
}
public void setPhone(String phone) {
	this.phone = phone;
}
public String getIsdefau() {
	return isdefau;
}
public void setIsdefau(String isdefau) {
	this.isdefau = isdefau;
}
@Override
public String toString() {
	return "AddressManager [id=" + id + ", consignee=" + consignee + ", area="
			+ area + ", detailadress=" + detailadress + ", zipcode=" + zipcode
			+ ", phone=" + phone + ", isdefau=" + isdefau + "]";
}

}

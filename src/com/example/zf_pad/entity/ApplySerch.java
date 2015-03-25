package com.example.zf_pad.entity;

public class ApplySerch {
private long id;
private String ternumber;
private String consumption;
private String conmoney;
private String traaccounts;
private String tramoney;
private String repayment;
private String repmoney;
public ApplySerch(long id,String ternumber,String consumption,String conmoney,
		String traaccounts,String tramoney,String repayment,String repmoney){
	super();
	this.id=id;
	this.ternumber=ternumber;
	this.consumption=consumption;
	this.conmoney=conmoney;
	this.traaccounts=traaccounts;
	this.tramoney=tramoney;
	this.repayment=repayment;
	this.repmoney=repmoney;
}
public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
public String getTernumber() {
	return ternumber;
}
public void setTernumber(String ternumber) {
	this.ternumber = ternumber;
}
public String getConsumption() {
	return consumption;
}
public void setConsumption(String consumption) {
	this.consumption = consumption;
}
public String getConmoney() {
	return conmoney;
}
public void setConmoney(String conmoney) {
	this.conmoney = conmoney;
}
public String getTraaccounts() {
	return traaccounts;
}
public void setTraaccounts(String traaccounts) {
	this.traaccounts = traaccounts;
}
public String getTramoney() {
	return tramoney;
}
public void setTramoney(String tramoney) {
	this.tramoney = tramoney;
}
public String getRepayment() {
	return repayment;
}
public void setRepayment(String repayment) {
	this.repayment = repayment;
}
public String getRepmoney() {
	return repmoney;
}
public void setRepmoney(String repmoney) {
	this.repmoney = repmoney;
}
@Override
public String toString() {
	return "ApplySerch [id=" + id + ", ternumber=" + ternumber
			+ ", consumption=" + consumption + ", conmoney=" + conmoney
			+ ", traaccounts=" + traaccounts + ", tramoney=" + tramoney
			+ ", repayment=" + repayment + ", repmoney=" + repmoney + "]";
}

}

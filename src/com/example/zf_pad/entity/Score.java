package com.example.zf_pad.entity;

public class Score {
private long id;
private String number;
private String time;
private String money;
private String gotscore;
private String scoretype;
public Score(long id,String number,String time,String money,
		String gotscore,String scoretype){
	super();
	this.id=id;
	this.number=number;
	this.time=time;
	this.money=money;
	this.gotscore=gotscore;
	this.scoretype=scoretype;
}
public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
public String getNumber() {
	return number;
}
public void setNumber(String number) {
	this.number = number;
}
public String getTime() {
	return time;
}
public void setTime(String time) {
	this.time = time;
}
public String getMoney() {
	return money;
}
public void setMoney(String money) {
	this.money = money;
}
public String getGotscore() {
	return gotscore;
}
public void setGotscore(String gotscore) {
	this.gotscore = gotscore;
}
public String getScoretype() {
	return scoretype;
}
public void setScoretype(String scoretype) {
	this.scoretype = scoretype;
}
@Override
public String toString() {
	return "Score [id=" + id + ", number=" + number + ", time=" + time
			+ ", money=" + money + ", gotscore=" + gotscore + ", scoretype="
			+ scoretype + "]";
}

}

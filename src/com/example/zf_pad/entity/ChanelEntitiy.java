package com.example.zf_pad.entity;

public class ChanelEntitiy {
//    "id": 1,
//    "service_rate": 30,
//    "description": "说明pcbc",
//    "name": "T+1"e 
	private int id;
	private int service_rate; 
	private String description;
	private String name;
	private int standard_rate;
	public int getStandard_rate() {
		return standard_rate;
	}
	public void setStandard_rate(int standard_rate) {
		this.standard_rate = standard_rate;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getService_rate() {
		return service_rate;
	}
	public void setService_rate(int service_rate) {
		this.service_rate = service_rate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
}

package com.example.zf_pad.entity;

import java.util.ArrayList;
import java.util.List;

public class category {
	private int id;
	private String value;
	private List<category> son=new ArrayList<category>();
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public List<category> getClist() {
		return son;
	}
	public void setClist(List<category> son) {
		this.son = son;
	}

}

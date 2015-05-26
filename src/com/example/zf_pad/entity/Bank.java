package com.example.zf_pad.entity;

import java.io.Serializable;

public class Bank implements Serializable {

	private static final long serialVersionUID = -8939113064763131465L;

	private String no;

	private String name;

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

package com.example.zf_pad.trade.entity;

import java.io.Serializable;


public class ApplyChooseItem implements Serializable {

	private int id;
	private String title;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}

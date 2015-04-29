package com.example.zf_pad.trade.entity;

import java.io.Serializable;


public class City implements Serializable {
	private int id;
	private String name;
	private int parentId;
	private String pinyin;

	private Province province;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	@Override
	public boolean equals(Object o) {
		if (null == o)
			return false;
		if (!(o instanceof City))
			return false;
		City that = (City) o;
		return that.getId() == this.getId();
	}

	@Override
	public String toString() {
		return name;
	}
}

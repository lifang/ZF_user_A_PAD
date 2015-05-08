package com.example.zf_pad.trade.common;

import java.util.List;

public class PageMerchane<T> {

	private int total;

	private List<T> merchaneList;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<T> getList() {
		return merchaneList;
	}

	public void setList(List<T> merchaneList) {
		this.merchaneList = merchaneList;
	}


}

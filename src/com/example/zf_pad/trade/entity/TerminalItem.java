package com.example.zf_pad.trade.entity;

import com.google.gson.annotations.SerializedName;


public class TerminalItem {

	private int id;
	@SerializedName("serial_num")
	private String terminalNumber;
	private int status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTerminalNumber() {
		return terminalNumber;
	}

	public void setTerminalNumber(String terminalNumber) {
		this.terminalNumber = terminalNumber;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}

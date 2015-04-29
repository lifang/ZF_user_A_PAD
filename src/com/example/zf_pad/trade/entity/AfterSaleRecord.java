package com.example.zf_pad.trade.entity;

import com.google.gson.annotations.SerializedName;


public class AfterSaleRecord {

	private int id;

	@SerializedName("apply_num")
	private String applyNum;

	@SerializedName("create_time")
	private String createTime;

	@SerializedName("terminal_num")
	private String terminalNum;

	private String status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getApplyNum() {
		return applyNum;
	}

	public void setApplyNum(String applyNum) {
		this.applyNum = applyNum;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getTerminalNum() {
		return terminalNum;
	}

	public void setTerminalNum(String terminalNum) {
		this.terminalNum = terminalNum;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}

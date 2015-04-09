package com.example.zf_pad.entity;

import com.google.gson.annotations.SerializedName;

public class TerminalManagerEntity {


	private int id;
	
	@SerializedName("serial_num")
	private String posPortID;// 终端号

	@SerializedName("brandsName")
	private String pos;// POS机

	@SerializedName("model_number")
	private String posname;// POS机
	
	@SerializedName("channelName")
	private String payChannel;// 支付通道

	@SerializedName("status")
	private int openState;// 开通状态

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPosname() {
		return posname;
	}

	public void setPosname(String posname) {
		this.posname = posname;
	}

	public String getPosPortID() {
		return posPortID;
	}

	public void setPosPortID(String posPortID) {
		this.posPortID = posPortID;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public String getPayChannel() {
		return payChannel;
	}

	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
	}

	public int getOpenState() {
		return openState;
	}

	public void setOpenState(int openState) {
		this.openState = openState;
	}

}

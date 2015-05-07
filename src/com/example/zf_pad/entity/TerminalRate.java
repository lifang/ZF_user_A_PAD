package com.example.zf_pad.entity;

import java.text.DecimalFormat;

import android.R.integer;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Leo on 2015/3/4.
 */
public class TerminalRate {

	private int id;

	@SerializedName("trade_value")
	private String type;

	@SerializedName("terminal_rate")
	private float terminalRate;

	@SerializedName("service_rate")
	private float serviceRate;

	@SerializedName("base_rate")
	private float baseRate;

	private int status;

	private int trade_type;

	public int getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(int trade_type) {
		this.trade_type = trade_type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public float getTerminalRate() {
		return terminalRate / 10.0f;
	}

	public void setTerminalRate(float terminalRate) {
		this.terminalRate = terminalRate;
	}

	public float getServiceRate() {
		return serviceRate / 10.0f;
	}

	public void setServiceRate(float serviceRate) {
		this.serviceRate = serviceRate;
	}

	public float getBaseRate() {
		return baseRate / 10.0f;
	}

	public void setBaseRate(float baseRate) {
		this.baseRate = baseRate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}

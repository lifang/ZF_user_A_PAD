package com.example.zf_pad.trade.entity;

import android.content.res.Resources;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AfterSaleDetailReturn extends AfterSaleDetail {

	@SerializedName("return_price")
	private int returnPrice;

	@SerializedName("bank_name")
	private String bankName;

	@SerializedName("bank_account")
	private String bankAccount;

	private String reason;

	public int getReturnPrice() {
		return returnPrice;
	}

	public void setReturnPrice(int returnPrice) {
		this.returnPrice = returnPrice;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}

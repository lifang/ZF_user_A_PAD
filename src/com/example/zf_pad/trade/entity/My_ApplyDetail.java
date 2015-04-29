package com.example.zf_pad.trade.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Leo on 2015/3/6.
 */
public class My_ApplyDetail {

	@SerializedName("applyDetails")
	private MyApplyTerminalDetail terminalDetail;

	@SerializedName("materialName")
	private List<MyApplyMaterial> materials;

	@SerializedName("merchants")
	private List<ApplyChooseItem> merchants;

	@SerializedName("applyFor")
	private List<MyApplyCustomerDetail> customerDetails;
	
	@SerializedName("openingInfos")
	private OpeningInfos openingInfos;

	public OpeningInfos getOpeningInfos() {
		return openingInfos;
	}

	public void setOpeningInfos(OpeningInfos openingInfos) {
		this.openingInfos = openingInfos;
	}

	public MyApplyTerminalDetail getTerminalDetail() {
		return terminalDetail;
	}

	public void setTerminalDetail(MyApplyTerminalDetail terminalDetail) {
		this.terminalDetail = terminalDetail;
	}

	public List<MyApplyMaterial> getMaterials() {
		return materials;
	}

	public void setMaterials(List<MyApplyMaterial> materials) {
		this.materials = materials;
	}

	public List<ApplyChooseItem> getMerchants() {
		return merchants;
	}

	public void setMerchants(List<ApplyChooseItem> merchants) {
		this.merchants = merchants;
	}

	public List<MyApplyCustomerDetail> getCustomerDetails() {
		return customerDetails;
	}

	public void setCustomerDetails(List<MyApplyCustomerDetail> customerDetails) {
		this.customerDetails = customerDetails;
	}
}

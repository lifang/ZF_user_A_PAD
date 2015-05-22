package com.example.zf_pad.trade.entity;

import com.google.gson.annotations.SerializedName;


public class AfterSaleDetailMaintain extends AfterSaleDetail {

    @SerializedName("receiver_addr")
    private String receiverAddr;

    @SerializedName("repair_price")
    private String repairPrice;

    private String description;
	private String change_reason;
	
    public String getChange_reason() {
		return change_reason;
	}

	public void setChange_reason(String change_reason) {
		this.change_reason = change_reason;
	}

	public String getReceiverAddr() {
        return receiverAddr;
    }

    public void setReceiverAddr(String receiverAddr) {
        this.receiverAddr = receiverAddr;
    }

    public String getRepairPrice() {
        return repairPrice;
    }

    public void setRepairPrice(String repairPrice) {
        this.repairPrice = repairPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

package com.example.zf_pad.entity;
import java.util.List;

public class PosSelectEntity {
	/**
	 * pos Ʒ��
	 */
	private List<PosItem> brands;
	/**
	 * pos ����
	 */
	private List<PosItem> category;
	public List<PosItem> getCategory() {
		return category;
	}

	public void setCategory(List<PosItem> category) {
		this.category = category;
	}

	/**
	 * pos ǩ����
	 */
	private List<PosItem> sale_slip;
	/**
	 * pos ֧���� ����
	 */
	private List<PosItem> pay_card;
	/**
	 * pos ֧��ͨ��
	 */
	private List<PosItem> pay_channel;
	/**
	 * pos ��������
	 */

	private List<PosItem> trade_type;
	/**
	 * pos ��������
	 */
	private List<PosItem> tDate;

	public List<PosItem> getBrands() {
		return brands;
	}

	public void setBrands(List<PosItem> brands) {
		this.brands = brands;
	}

	public List<PosItem> getSale_slip() {
		return sale_slip;
	}

	public void setSale_slip(List<PosItem> sale_slip) {
		this.sale_slip = sale_slip;
	}

	public List<PosItem> getPay_card() {
		return pay_card;
	}

	public void setPay_card(List<PosItem> pay_card) {
		this.pay_card = pay_card;
	}

	public List<PosItem> getPay_channel() {
		return pay_channel;
	}

	public void setPay_channel(List<PosItem> pay_channel) {
		this.pay_channel = pay_channel;
	}

	public List<PosItem> getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(List<PosItem> trade_type) {
		this.trade_type = trade_type;
	}

	public List<PosItem> gettDate() {
		return tDate;
	}

	public void settDate(List<PosItem> tDate) {
		this.tDate = tDate;
	}

}

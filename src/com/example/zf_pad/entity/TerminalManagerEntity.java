package com.example.zf_pad.entity;

public class TerminalManagerEntity {

	private int posPortID;// �ն˺�
	private String pos;// POS��
	private String payChannel;// ֧��ͨ��
	private int openState;// ��ͨ״̬

	public int getPosPortID() {
		return posPortID;
	}

	public void setPosPortID(int posPortID) {
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

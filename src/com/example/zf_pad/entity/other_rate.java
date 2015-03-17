package com.example.zf_pad.entity;
/*"other_rate": [
               {
                   "trade_value": "ฯ๛ทั",
                   "description": "2",
                   "terminal_rate": 2
               }*/
public class other_rate {
	private String trade_value;
	private String description;
	private int terminal_rate;
	public String getTrade_value() {
		return trade_value;
	}
	public void setTrade_value(String trade_value) {
		this.trade_value = trade_value;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getTerminal_rate() {
		return terminal_rate;
	}
	public void setTerminal_rate(int terminal_rate) {
		this.terminal_rate = terminal_rate;
	}

}

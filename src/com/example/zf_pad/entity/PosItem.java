package com.example.zf_pad.entity;

import java.util.List;

public class PosItem {
	 
		public Boolean isCheck=false;
		
		public Boolean getIsCheck() {
			return isCheck;
		}
		public void setIsCheck(Boolean isCheck) {
			this.isCheck = isCheck;
		}
		public  int id;
		public String value;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		private List<PortSon> son;

		public List<PortSon> getSon() {
			return son;
		}
		public void setSon(List<PortSon> son) {
			this.son = son;
		}
		
		 
	 
}

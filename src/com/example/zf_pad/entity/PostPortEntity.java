package com.example.zf_pad.entity;

import java.util.ArrayList;
import java.util.List;

import android.content.ClipData.Item;
import android.widget.ListView;

public class PostPortEntity {
	public PostPortEntity() {
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public List<PosItem> getChildlist() {
		return childlist;
	}
	public void setChildlist(List<PosItem> childlist) {
		this.childlist = childlist;
	}
	private String Title;
	private List<PosItem> childlist=new ArrayList<PosItem>();
	private boolean isSeleck=false;
	public boolean isSeleck() {
		return isSeleck;
	}
	public void setSeleck(boolean isSeleck) {
		this.isSeleck = isSeleck;
	}
}

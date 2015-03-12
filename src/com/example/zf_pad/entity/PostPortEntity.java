package com.example.zf_pad.entity;

import java.util.ArrayList;
import java.util.List;

import android.widget.ListView;

public class PostPortEntity {
	public PostPortEntity() {
		
		for(int i=0;i<9;i++){
			PosPortChild ppc=new PosPortChild();
			ppc.setTitile("µЪ"+i+"По");
			childlist.add(ppc);
		}
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public List<PosPortChild> getChildlist() {
		return childlist;
	}
	public void setChildlist(List<PosPortChild> childlist) {
		this.childlist = childlist;
	}
	private String Title;
	private List<PosPortChild> childlist=new ArrayList<PosPortChild>();
	private boolean isSeleck=false;
	public boolean isSeleck() {
		return isSeleck;
	}
	public void setSeleck(boolean isSeleck) {
		this.isSeleck = isSeleck;
	}
}

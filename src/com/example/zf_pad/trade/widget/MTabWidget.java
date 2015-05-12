package com.example.zf_pad.trade.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TabWidget;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;

import com.example.zf_pad.R;


public class MTabWidget extends TabWidget implements View.OnClickListener {
 
    protected final List<TextView> mTabViews = new ArrayList<TextView>();
    protected Context mContext;
    protected Resources resources;
    protected int mBaseColor;
    OnTabOnclik onc;
    public MTabWidget(Context context) {
        super(context);
        init(context);
    }

    public MTabWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MTabWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    public void setonTabLintener(OnTabOnclik onc){
    	this.onc=onc;
    }

    @SuppressLint({ "NewApi", "ResourceAsColor" }) protected void init(Context context) {
        this.mContext = context;
        resources = mContext.getResources();
        mBaseColor = resources.getColor(R.color.bgtitle);
        //setBackgroundDrawable(resources.getDrawable(R.drawable.tab_widget_bg));
        //setBackgroundColor(R.color.touming);
       // setDividerDrawable(R.drawable.divider);
        //setDividerPadding(0);
        
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
    }


    @SuppressLint("ResourceAsColor") public void addTab(String tab) {

        TextView tv = new TextView(mContext);
        tv.setText(tab);
        //tv.setTextColor(R.color.touming);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        mTabViews.add(tv);
        addView(tv);
        tv.setOnClickListener(this);
      
    }


    @Override
    public void onClick(View v) {
        if (mTabViews.size() == 0) return;
        TextView tv = (TextView) v;
        int position = mTabViews.indexOf(tv);
        updateTabs(position);
      
        if(onc!=null)
        	onc.chang(position);
        
    }

    @SuppressLint("ResourceAsColor") public void updateTabs(int position) {

        for (TextView tv : mTabViews) {
            tv.setBackgroundColor(getResources().getColor(R.color.background_flow_tab));
        	//tv.setBackgroundDrawable(resources.getDrawable(R.drawable.tab_bg));
            tv.setTextColor(resources.getColor(R.color.text292929));
        }

        TextView tv = mTabViews.get(position);
        tv.setTextColor(resources.getColor(R.color.text292929));
        if (mTabViews.indexOf(tv) == 0) {
            tv.setBackgroundDrawable(resources.getDrawable(R.drawable.tab_bg));
        } else if (mTabViews.indexOf(tv) == mTabViews.size() - 1) {
            tv.setBackgroundDrawable(resources.getDrawable(R.drawable.tab_bg));
        } else {
            //tv.setBackgroundColor(mBaseColor);
        	 tv.setBackgroundDrawable(resources.getDrawable(R.drawable.tab_bg));
        }
    }
    
    public interface OnTabOnclik{  
    void chang(int index); 
    } 

}

package com.example.zf_pad.fragment;


import java.util.ArrayList;
import java.util.List;
import com.example.zf_pad.R;
import com.example.zf_pad.aadpter.MessageAdapter;
import com.example.zf_pad.activity.MainActivity;
import com.example.zf_pad.entity.TestEntitiy;
import com.example.zf_pad.util.Tools;
import com.example.zf_pad.util.XListView;
import com.example.zf_pad.util.XListView.IXListViewListener;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class m_wdxx extends Fragment implements OnClickListener,IXListViewListener{
	private View view;
	private XListView Xlistview;
	private int page=1;
	//private int rows=Config.ROWS;
	private LinearLayout eva_nodata;
	private boolean onRefresh_number = true;
	private MessageAdapter myAdapter;
	private TextView next_sure;
	private MainActivity mActivity;
	List<TestEntitiy>  myList = new ArrayList<TestEntitiy>();
	List<TestEntitiy>  moreList = new ArrayList<TestEntitiy>();
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				onLoad( );
				
				if(myList.size()==0){
				//	norecord_text_to.setText("您没有相关的商品");
					Xlistview.setVisibility(View.GONE);
					eva_nodata.setVisibility(View.VISIBLE);
				}
				onRefresh_number = true; 
			 	myAdapter.notifyDataSetChanged();
				break;
			case 1:
				Toast.makeText(mActivity, (String) msg.obj,
						Toast.LENGTH_SHORT).show();
			 
				break;
			case 2: // 网络有问题
				Toast.makeText(mActivity, "no 3g or wifi content",
						Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(mActivity,  " refresh too much",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
			//view = inflater.inflate(R.layout.f_main,container,false);
			
		
		
		if (view != null) {
	        ViewGroup parent = (ViewGroup) view.getParent();
	        if (parent != null)
	            parent.removeView(view);
	    }
	    try {
	        view = inflater.inflate(R.layout.m_wdxx, container, false);
	        initView();
			getData();
	    } catch (InflateException e) {
	        
	    }
	    return view;
	}
	private void initView() {
		// TODO Auto-generated method stub
		//next_sure=(TextView) findViewById(R.id.next_sure);
		//next_sure.setVisibility(View.VISIBLE);
		//next_sure.setText("编辑");
		//new TitleMenuUtil(MyMessage.this, "系统公告").show();
		myAdapter=new MessageAdapter(mActivity, myList);
		eva_nodata=(LinearLayout) view.findViewById(R.id.eva_nodata);
		Xlistview=(XListView) view.findViewById(R.id.x_listview);
		// refund_listview.getmFooterView().getmHintView().setText("已经没有数据了");
		Xlistview.setPullLoadEnable(true);
		Xlistview.setXListViewListener(this);
		Xlistview.setDivider(null);

		Xlistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
//				Intent i = new Intent(MyMessage.this, OrderDetail.class);
//				startActivity(i);
			}
		});
		Xlistview.setAdapter(myAdapter);
		
		
		/*
		next_sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(MyApplication.getIsSelect()){
					//遍历数组删除操作
					next_sure.setText("删除");
					MyApplication.setIsSelect(false);
					myAdapter.notifyDataSetChanged();
					for(int i=0;i<myList.size();i++){
						 
						if(myList.get(i).getIscheck()){
							System.out.println("第---"+i+"条被选中");
						}
					}
				}else{
					next_sure.setText("编辑");
					MyApplication.setIsSelect(true);
					myAdapter.notifyDataSetChanged();
				}
			}
		});*/
	}

	@Override
	public void onAttach(Activity activity) {
		mActivity = (MainActivity) activity;
		super.onAttach(activity);
	}
	private void onLoad() {
		Xlistview.stopRefresh();
		Xlistview.stopLoadMore();
		Xlistview.setRefreshTime(Tools.getHourAndMin());
	}

	public void buttonClick() {
		page = 1;
		myList.clear();
		//getData();
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onRefresh() {
		page = 1;
		 System.out.println("onRefresh1");
		myList.clear();
		 System.out.println("onRefresh2");
		getData();
		
	}
	@Override
	public void onLoadMore() {
		if (onRefresh_number) {
			page = page+1;
			
			onRefresh_number = false;
			getData();
			
//			if (Tools.isConnect(getApplicationContext())) {
//				onRefresh_number = false;
//				getData();
//			} else {
//				onRefresh_number = true;
//				handler.sendEmptyMessage(2);
//			}
		}
		else {
			handler.sendEmptyMessage(3);
		}
	}
	/*
	 * 请求数据
	 */
	private void getData() {
		// TODO Auto-generated method stub
		 
	 
		 TestEntitiy te=new TestEntitiy();
		 te.setContent("---这里是标题1---"+page+page);
		 myList.add(te);
		 TestEntitiy te2=new TestEntitiy();
		 te2.setContent("---这里是标题2---"+page+page);
		 myList.add(te2);
		 TestEntitiy te22=new TestEntitiy();
		 te22.setContent("---标题3---"+page+page);
		 myList.add(te22);
		
		System.out.println("getData");
		handler.sendEmptyMessage(0);
	}	
}

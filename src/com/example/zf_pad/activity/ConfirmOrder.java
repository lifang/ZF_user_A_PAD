package com.example.zf_pad.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.zf_pad.BaseActivity;
import com.example.zf_pad.R;
import com.example.zf_pad.entity.TestEntitiy;
import com.example.zf_pad.util.ScrollViewWithListView;
import com.example.zf_pad.util.TitleMenuUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class ConfirmOrder extends BaseActivity implements OnClickListener {
	private ScrollViewWithListView pos_lv, his_lv;
	List<TestEntitiy> poslist = new ArrayList<TestEntitiy>();
	private Button btn_pay;
	// private OrderDetail_PosAdapter posAdapter;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				// posAdapter.notifyDataSetChanged();

				break;
			case 1:
				Toast.makeText(getApplicationContext(), (String) msg.obj,
						Toast.LENGTH_SHORT).show();

				break;
			case 2: // 网络有问题
				Toast.makeText(getApplicationContext(),
						"no 3g or wifi content", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(getApplicationContext(), " refresh too much",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_comfirm);

		initView();
		getData();
	}

	private void getData() {
		// TODO Auto-generated method stub
		TestEntitiy te = new TestEntitiy();
		te.setContent("泰山旗舰900");
		poslist.add(te);
		TestEntitiy te1 = new TestEntitiy();
		te1.setContent("泰山旗舰1000");
		poslist.add(te1);
		TestEntitiy te22 = new TestEntitiy();
		te22.setContent("泰山旗舰1100");
		poslist.add(te22);
		handler.sendEmptyMessage(0);
	}

	private void initView() {

		new TitleMenuUtil(this, "订单确认").show();
		pos_lv = (ScrollViewWithListView) findViewById(R.id.pos_lv1);
		// posAdapter=new OrderDetail_PosAdapter(ConfirmOrder.this, poslist);
		// pos_lv.setAdapter(posAdapter);
		btn_pay = (Button) findViewById(R.id.btn_pay);
		btn_pay.setOnClickListener(this);
		Spinner sp = (Spinner) findViewById(R.id.spinner);
		final String arr[] = new String[] { "星期一", "星期二", "星期三", "星期四", "星期五",
				"星期六", "星期日" };

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arr);
        sp.setAdapter(arrayAdapter);  
        Toast.makeText(getApplicationContext(), "main Thread"+sp.getItemIdAtPosition(sp.getSelectedItemPosition()), Toast.LENGTH_LONG).show();  
          
        //注册事件  
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {  
  
            @Override  
            public void onItemSelected(AdapterView<?> parent, View view,  
                    int position, long id) {  
                Spinner spinner=(Spinner) parent;  
                Toast.makeText(getApplicationContext(), "xxxx"+spinner.getItemAtPosition(position), Toast.LENGTH_LONG).show();  
            }  
  
            @Override  
            public void onNothingSelected(AdapterView<?> parent) {  
                Toast.makeText(getApplicationContext(), "没有改变的处理", Toast.LENGTH_LONG).show();  
            }  
  
        });  
    } 
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_pay:
			Intent i = new Intent(ConfirmOrder.this, PayFromCar.class);
			startActivity(i);
			break;
		default:
			break;
		}
	}
}

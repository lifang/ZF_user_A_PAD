package com.example.zf_pad.activity;
import com.example.zf_pad.Config;
import com.example.zf_pad.R;
import com.example.zf_pad.aadpter.HuilvAdapter;
import com.example.zf_pad.util.ScrollViewWithListView;


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class GoodDeatilMore extends Activity{
	private HuilvAdapter lvAdapter1,lvAdapter2,lvAdapter3;
	private ScrollViewWithListView  pos_lv1,pos_lv2,pos_lv3;
	private TextView ppxx;
	private TextView wkxx;
	private TextView tv_qgd;
	private TextView tv_jm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goodmore);
		initView();
	}

	private void initView() {
		ppxx = (TextView) findViewById(R.id.ppxx);
		wkxx = (TextView) findViewById(R.id.wkxx);
		TextView dcxx=(TextView) findViewById(R.id.dcxx);
		tv_qgd = (TextView) findViewById(R.id.tv_qgd);
		tv_jm = (TextView) findViewById(R.id.tv_jm);
		pos_lv1=(ScrollViewWithListView) findViewById(R.id.pos_lv1);
		pos_lv2=(ScrollViewWithListView) findViewById(R.id.pos_lv2);
		pos_lv3=(ScrollViewWithListView) findViewById(R.id.pos_lv3);
		lvAdapter1 = new HuilvAdapter(this,Config.celist);
		lvAdapter2 = new HuilvAdapter(this,Config.tDates,0);
		lvAdapter3 = new HuilvAdapter(this,Config.other_rate,1,"1");
		pos_lv1.setAdapter(lvAdapter1);
		pos_lv2.setAdapter(lvAdapter2);
		pos_lv3.setAdapter(lvAdapter3);
		ppxx.setText(Config.gfe.getModel_number() );
		wkxx.setText(Config.gfe.getShell_material() );
		dcxx.setText(Config.gfe.getBattery_info());
		tv_qgd.setText(Config.gfe.getSign_order_way());
		tv_jm.setText(Config.gfe.getEncrypt_card_way());
		
	}

}

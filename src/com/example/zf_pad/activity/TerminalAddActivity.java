package com.example.zf_pad.activity;

import static com.example.zf_pad.fragment.Constants.TerminalIntent.CHANNEL_ID;
import static com.example.zf_pad.fragment.Constants.TerminalIntent.CHANNEL_NAME;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zf_pad.R;
import com.example.zf_pad.entity.TerminalChannel;
import com.example.zf_pad.trade.API;
import com.example.zf_pad.trade.common.HttpCallback;
import com.example.zf_pad.trade.common.TextWatcherAdapter;
import com.example.zf_pad.util.StringUtil;
import com.google.gson.reflect.TypeToken;

/**
 * Created by Leo on 2015/3/4.
 */
public class TerminalAddActivity extends Activity implements
		View.OnClickListener {

	private View mChooseChannel;
	private TextView mPayChannel;
	private int mChannelId;
	private String mChannelName;

	private EditText mTerminalNumber;
	private EditText mShopName;
	private Button mSubmitBtn;

	private String terminalNum, shopName;

	private ArrayAdapter<String> adapter;

	private Spinner spinner;
	private Button close;

	private List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
	final List<String> list = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_terminal_add);

		spinner = (Spinner) findViewById(R.id.spinner);
		items = new ArrayList<Map<String, Object>>();

		API.getChannelList(this, new HttpCallback<List<TerminalChannel>>(this) {

			@Override
			public void onSuccess(List<TerminalChannel> data) {
				for (TerminalChannel channel : data) {
					Map<String, Object> item = new HashMap<String, Object>();
					item.put("id", channel.getId());
					item.put("name", channel.getName());
					list.add(channel.getName());
					items.add(item);
				}
				myHandler.sendEmptyMessage(1);

			}

			@Override
			public TypeToken<List<TerminalChannel>> getTypeToken() {
				return new TypeToken<List<TerminalChannel>>() {
				};
			}
		});

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		spinner.setAdapter(adapter);

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				mChannelId = (Integer) items.get(arg2).get("id");

			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});

		// mChooseChannel.setOnClickListener(this);

		mTerminalNumber = (EditText) findViewById(R.id.terminal_num);
		mShopName = (EditText) findViewById(R.id.shop_name);
		mSubmitBtn = (Button) findViewById(R.id.terminal_submit);

		mTerminalNumber.addTextChangedListener(mTextWatcher);
		mShopName.addTextChangedListener(mTextWatcher);
		mSubmitBtn.setOnClickListener(this);

		close = (Button) findViewById(R.id.close);
		close.setOnClickListener(this);
	}

	private Handler myHandler = new Handler() {

		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 1:

				adapter.notifyDataSetChanged();

				break;

			}

		};

	};

	private final TextWatcher mTextWatcher = new TextWatcherAdapter() {

		public void afterTextChanged(final Editable gitDirEditText) {
			updateUIWithValidation();
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		updateUIWithValidation();
	}

	private void updateUIWithValidation() {
		final boolean enabled = mChannelId > 0 && mTerminalNumber.length() > 0
				&& mShopName.length() > 0;
		mSubmitBtn.setEnabled(enabled);
	}

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// super.onActivityResult(requestCode, resultCode, data);
	// if (resultCode == RESULT_OK) {
	// mChannelId = data.getIntExtra(CHANNEL_ID, 0);
	// mChannelName = data.getStringExtra(CHANNEL_NAME);
	// mPayChannel.setText(mChannelName);
	// }
	// }

	@Override
	public void onClick(View view) {
		switch (view.getId()) {

		case R.id.terminal_submit:

			// if (check()) {
			API.addTerminal(TerminalAddActivity.this, 80, mChannelId,
					mTerminalNumber.getText().toString(), mShopName.getText()
							.toString(), new HttpCallback(
							TerminalAddActivity.this) {
						@Override
						public void onSuccess(Object data) {
							setResult(RESULT_OK);
							finish();
						}

						@Override
						public TypeToken getTypeToken() {
							return null;
						}
					});
			// }
			break;
		case R.id.close:
			this.finish();
			break;

		}
	}

	// private boolean check() {
	// terminalNum = StringUtil.replaceBlank(mTerminalNumber.getText()
	// .toString());
	// if (terminalNum.length() == 0) {
	// Toast.makeText(getApplicationContext(), "用户名不能为空",
	// Toast.LENGTH_SHORT).show();
	// return false;
	// }
	// shopName = StringUtil.replaceBlank(mShopName.getText().toString());
	// if (shopName.length() == 0) {
	// Toast.makeText(getApplicationContext(), "请输入用户密码",
	// Toast.LENGTH_SHORT).show();
	// return false;
	// }
	// return true;
	// }
}

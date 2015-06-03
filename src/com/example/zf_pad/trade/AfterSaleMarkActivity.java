package com.example.zf_pad.trade;

import static com.example.zf_pad.fragment.Constants.AfterSaleIntent.RECORD_ID;
import static com.example.zf_pad.fragment.Constants.AfterSaleIntent.RECORD_TYPE;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;



import com.example.zf_pad.BaseActivity;
import com.example.zf_pad.MyApplication;
import com.epalmpay.userPad.R;
import com.example.zf_pad.trade.common.HttpCallback;
import com.example.zf_pad.trade.common.TextWatcherAdapter;
import com.example.zf_pad.util.TitleMenuUtil;
import com.google.gson.reflect.TypeToken;


public class AfterSaleMarkActivity extends BaseActivity {

	private int mRecordType;
	private int mRecordId;

	private EditText mCompanyEdit;
	private EditText mNumberEdit;
	private Button mSubmitBtn,close;

	private final TextWatcherAdapter mTextWatcher = new TextWatcherAdapter() {

		public void afterTextChanged(final Editable gitDirEditText) {
			updateUIWithValidation();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mRecordType = getIntent().getIntExtra(RECORD_TYPE, 0);
		mRecordId = getIntent().getIntExtra(RECORD_ID, 0);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_after_sale_mark);
		//new TitleMenuUtil(this, getString(R.string.title_after_sale_mark)).show();

		close = (Button) findViewById(R.id.close);
		mCompanyEdit = (EditText) findViewById(R.id.after_sale_mark_company);
		mNumberEdit = (EditText) findViewById(R.id.after_sale_mark_number);
		mSubmitBtn = (Button) findViewById(R.id.after_sale_mark_submit);

		close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mCompanyEdit.addTextChangedListener(mTextWatcher);
		mNumberEdit.addTextChangedListener(mTextWatcher);
		mSubmitBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				API.addMark(AfterSaleMarkActivity.this, mRecordType, mRecordId, MyApplication.NewUser.getId(),
						mCompanyEdit.getText().toString(), mNumberEdit.getText().toString(),
						new HttpCallback(AfterSaleMarkActivity.this) {
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
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateUIWithValidation();
	}

	private void updateUIWithValidation() {
		final boolean enabled = mCompanyEdit.length() > 0 && mNumberEdit.length() > 0;
		mSubmitBtn.setEnabled(enabled);
	}
}

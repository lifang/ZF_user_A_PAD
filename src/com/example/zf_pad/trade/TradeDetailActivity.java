package com.example.zf_pad.trade;



import static com.example.zf_pad.fragment.Constants.TradeIntent.TRADE_RECORD_ID;
import static com.example.zf_pad.fragment.Constants.TradeIntent.TRADE_TYPE;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zf_pad.BaseActivity;
import com.epalmpay.userPad.R;
import com.example.zf_pad.trade.common.HttpCallback;
import com.example.zf_pad.trade.entity.TradeDetail;
import com.example.zf_pad.util.StringUtil;
import com.example.zf_pad.util.TitleMenuUtil;
import com.google.gson.reflect.TypeToken;

public class TradeDetailActivity extends BaseActivity {

    private LinearLayout mCommercialKeyContainer;
    private LinearLayout mCommercialValueContainer;

    private LinearLayout mBankKeyContainer;
    private LinearLayout mBankValueContainer;

    private TextView mTradeStatus;
    private TextView mTradeAmount;
    private TextView mTradePoundage;
    private TextView mTradeTime;
    private int typeId;
    private String[] bankKeys;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_item);
        new TitleMenuUtil(this, getString(R.string.title_trade_detail)).show();

         typeId = getIntent().getIntExtra(TRADE_TYPE, 0);
        int recordId = getIntent().getIntExtra(TRADE_RECORD_ID, 0);
        
        initialViews();
        API.getTradeRecordDetail(this, typeId, recordId, new HttpCallback<TradeDetail>(this) {
        
            @Override
            public void onSuccess(TradeDetail data) {
            	DecimalFormat df = (DecimalFormat)NumberFormat.getInstance();
    			df.applyPattern("0.00");
                Resources resources = getResources();
                String[] tradeStatuses = resources.getStringArray(R.array.trade_status);
                mTradeStatus.setText(tradeStatuses[data.getTradedStatus()]);
                mTradeAmount.setText(getString(R.string.notation_yuan) + df.format(data.getAmount()*1.0f/100));
                mTradePoundage.setText(getString(R.string.notation_yuan) + df.format(data.getPoundage()*1.0f/100));
                mTradeTime.setText(data.getTradedTimeStr());

                String[] commercialKeys = resources.getStringArray(R.array.trade_item_commercial);
               // String[] bankKeys = resources.getStringArray(R.array.trade_item_bank);

                for (int i = 0; i < commercialKeys.length; i++) {
                    TextView value = new TextView(TradeDetailActivity.this);
                    value.setGravity(Gravity.LEFT);
                    value.setPadding(0, 5, 0, 5);
                    value.setTextColor(getResources().getColor(R.color.text6c6c6c6));
                    value.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                    value.setText(i == 0 ? data.getMerchantNumber()
                            : i == 1 ? data.getAgentName() + ""
                            : i == 2 ? data.getMerchantName()
                            : "");
                    mCommercialValueContainer.addView(value);
                }
                if (typeId == 2 || typeId == 3){ // 转锟斤拷 锟斤拷锟斤拷锟斤拷 
					bankKeys = resources.getStringArray(R.array.trade_item_bank_transfer);
					for (int i = 0; i < bankKeys.length; i++) {
						TextView value = new TextView(TradeDetailActivity.this);
						value.setGravity(Gravity.LEFT);
						value.setPadding(0, 5, 0, 5);
						value.setTextColor(resources.getColor(R.color.text6c6c6c6));
						value.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
						value.setText(i == 0 ? data.getTerminalNumber()//锟斤拷 锟斤拷 锟斤拷
								: i == 1 ? StringUtil.replaceNum(data.getPayFromAccount())//锟斤拷锟斤拷锟剿猴拷
								: i == 2 ? StringUtil.replaceNum(data.getPayIntoAccount())//转锟斤拷锟剿猴拷||锟秸匡拷锟剿号ｏ拷
								: i == 3 ? data.getPayChannelName()//支锟斤拷通锟斤拷
								: i == 4 ? getString(R.string.notation_yuan)+df.format(data.getAmount()*1.0f/100)//锟斤拷锟阶斤拷锟�
								: i == 5 ? data.getTradedTimeStr()//锟斤拷锟斤拷时锟斤拷
								: i == 6 ? tradeStatuses[data.getTradedStatus()]//锟斤拷锟斤拷状态
								: i == 7 ? data.getBatchNumber()//锟斤拷锟斤拷锟斤拷锟轿猴拷
								: i == 8 ? data.getTradeNumber()//锟斤拷锟斤拷锟斤拷水锟斤拷
								: "");
						mBankValueContainer.addView(value);
					}

				}else if (typeId == 1) {//锟斤拷锟斤拷
					bankKeys = resources.getStringArray(R.array.trade_item_bank_consume);
					for (int i = 0; i < bankKeys.length; i++) {
						TextView value = new TextView(TradeDetailActivity.this);
						value.setGravity(Gravity.LEFT);
						value.setPadding(0, 5, 0, 5);
						value.setTextColor(resources.getColor(R.color.text6c6c6c6));
						value.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
						value.setText(i == 0 ? data.getTerminalNumber()//锟斤拷 锟斤拷 锟斤拷
								: i == 1 ? getString(R.string.notation_yuan) + df.format(data.getPoundage()*1.0f/100)//锟斤拷锟斤拷锟斤拷
								: i == 2 ? data.getPayChannelName()//支锟斤拷通锟斤拷
								: i == 3 ? getString(R.string.notation_yuan)+df.format(data.getAmount()*1.0f/100)//锟斤拷锟阶斤拷锟�
								: i == 4 ? data.getTradedTimeStr()//锟斤拷锟斤拷时锟斤拷
								: i == 5 ? tradeStatuses[data.getTradedStatus()]//锟斤拷锟斤拷状态
								: i == 6 ? data.getBatchNumber()//锟斤拷锟斤拷锟斤拷锟轿猴拷
								: i == 7 ? data.getTradeNumber()//锟斤拷锟斤拷锟斤拷水锟斤拷
								: "");
						mBankValueContainer.addView(value);
					}
				}else if (typeId == 5) {//锟斤拷锟斤拷锟街�
					bankKeys = resources.getStringArray(R.array.trade_item_bank_life_pay);
					for (int i = 0; i < bankKeys.length; i++) {
						TextView value = new TextView(TradeDetailActivity.this);
						value.setGravity(Gravity.LEFT);
						value.setPadding(0, 5, 0, 5);
						value.setTextColor(resources.getColor(R.color.text6c6c6c6));
						value.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
						value.setText(i == 0 ? data.getTerminalNumber()//锟斤拷 锟斤拷 锟斤拷
								: i == 1 ? StringUtil.replaceName(data.getAccount_name())//锟剿伙拷锟斤拷
								: i == 2 ? StringUtil.replaceNum(data.getAccount_number())//锟剿伙拷锟斤拷锟斤拷
								: i == 3 ? data.getPayChannelName()//支锟斤拷通锟斤拷
								: i == 4 ? getString(R.string.notation_yuan)+df.format(data.getAmount()*1.0f/100)//锟斤拷锟阶斤拷锟�
								: i == 5 ? data.getTradedTimeStr()//锟斤拷锟斤拷时锟斤拷
								: i == 6 ? tradeStatuses[data.getTradedStatus()]//锟斤拷锟斤拷状态
								: i == 7 ? data.getBatchNumber()//锟斤拷锟斤拷锟斤拷锟轿猴拷
								: i == 8 ? data.getTradeNumber()//锟斤拷锟斤拷锟斤拷水锟斤拷
								: "");
						mBankValueContainer.addView(value);
					}
				}else if (typeId == 4) {//锟斤拷锟窖筹拷值
					bankKeys = resources.getStringArray(R.array.trade_item_bank_phone_pay);
					for (int i = 0; i < bankKeys.length; i++) {
						TextView value = new TextView(TradeDetailActivity.this);
						value.setGravity(Gravity.LEFT);
						value.setPadding(0, 5, 0, 5);
						value.setTextColor(resources.getColor(R.color.text6c6c6c6));
						value.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
						value.setText(i == 0 ? data.getTerminalNumber()//锟斤拷 锟斤拷 锟斤拷
								: i == 1 ? StringUtil.replaceNum(data.getPhone())//锟街伙拷锟斤拷锟斤拷
								: i == 2 ? data.getPayChannelName()//支锟斤拷通锟斤拷
								: i == 3 ? getString(R.string.notation_yuan)+df.format(data.getAmount()*1.0f/100)//锟斤拷锟阶斤拷锟�
								: i == 4 ? data.getTradedTimeStr()//锟斤拷锟斤拷时锟斤拷
								: i == 5 ? tradeStatuses[data.getTradedStatus()]//锟斤拷锟斤拷状态
								: i == 6 ? data.getBatchNumber()//锟斤拷锟斤拷锟斤拷锟轿猴拷
								: i == 7 ? data.getTradeNumber()//锟斤拷锟斤拷锟斤拷水锟斤拷
								: "");
						mBankValueContainer.addView(value);
					}
				}
//                for (int i = 0; i < bankKeys.length; i++) {
//                    TextView value = new TextView(TradeDetailActivity.this);
//                    value.setGravity(Gravity.LEFT);
//                    value.setPadding(0, 5, 0, 5);
//                    value.setTextColor(resources.getColor(R.color.text292929));
//                    value.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
//                    value.setText(i == 0 ? data.getTerminalNumber()
//                            : i == 1 ? data.getPayFromAccount()
//                            : i == 2 ? data.getPayIntoAccount()
//                            : i == 3 ? data.getPayChannelName()
//                            : i == 4 ? getString(R.string.notation_yuan)+df.format(data.getProfitPrice()*1.0f/100)
//                            : i == 5 ?getString(R.string.notation_yuan)+df.format(data.getAmount()*1.0f/100)
//                            : i == 6 ? data.getTradedTimeStr()
//                            : i == 7 ? tradeStatuses[data.getTradedStatus()]
//                            : i == 8 ? data.getBatchNumber()
//                            : i == 9 ? data.getTradeNumber()
//                            : "");
//                    mBankValueContainer.addView(value);
//                }
            }

            @Override
            public TypeToken<TradeDetail> getTypeToken() {
                return new TypeToken<TradeDetail>() {
                };
            }
        });
    }

    private void initialViews() {
        mTradeStatus = (TextView) findViewById(R.id.trade_detail_status);
        mTradeAmount = (TextView) findViewById(R.id.trade_detail_amount);
        mTradePoundage = (TextView) findViewById(R.id.trade_detail_poundage);
        mTradeTime = (TextView) findViewById(R.id.trade_detail_time);

        mCommercialKeyContainer = (LinearLayout) findViewById(R.id.trade_commercial_key_container);
        mCommercialValueContainer = (LinearLayout) findViewById(R.id.trade_commercial_value_container);
        mBankKeyContainer = (LinearLayout) findViewById(R.id.trade_bank_key_container);
        mBankValueContainer = (LinearLayout) findViewById(R.id.trade_bank_value_container);

        Resources resources = getResources();
        String[] commercialKeys = resources.getStringArray(R.array.trade_item_commercial);
       // String[] bankKeys = resources.getStringArray(R.array.trade_item_bank);

        for (int i = 0; i < commercialKeys.length; i++) {
            TextView key = new TextView(this);
            key.setGravity(Gravity.RIGHT);
            key.setPadding(0, 5, 0, 5);
            key.setTextColor(resources.getColor(R.color.text6c6c6c6));
            key.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            key.setText(commercialKeys[i]);
            mCommercialKeyContainer.addView(key);
        }

    	if (typeId == 2 || typeId == 3){  // 转账 。还款 
			bankKeys = resources.getStringArray(R.array.trade_item_bank_transfer);
		}else if (typeId == 1) {//消费
			bankKeys = resources.getStringArray(R.array.trade_item_bank_consume);
		}else if (typeId == 5) {//生活充值
			bankKeys = resources.getStringArray(R.array.trade_item_bank_life_pay);
		}else if (typeId == 4) {//话费充值
			bankKeys = resources.getStringArray(R.array.trade_item_bank_phone_pay);
		}
		for (int i = 0; i < bankKeys.length; i++) {
			TextView key = new TextView(this);
			key.setGravity(Gravity.RIGHT);
			key.setPadding(0, 5, 0, 5);
			key.setTextColor(resources.getColor(R.color.text6c6c6c6));
			key.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
			key.setText(bankKeys[i]);
			mBankKeyContainer.addView(key);
		}
        
        
    }
}

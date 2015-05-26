package com.example.zf_pad.video;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bairuitech.anychat.AnyChatBaseEvent;
import com.bairuitech.anychat.AnyChatCoreSDK;
import com.bairuitech.anychat.AnyChatDefine;
import com.example.zf_pad.BaseActivity;
import com.example.zf_pad.Config;
import com.example.zf_pad.MyApplication;
import com.epalmpay.userPad.R;
import com.example.zf_pad.fragment.Constants.TerminalIntent;
import com.example.zf_pad.trade.API;
import com.example.zf_pad.video.config.ConfigEntity;
import com.example.zf_pad.video.config.ConfigService;

public class VideoActivity extends BaseActivity implements AnyChatBaseEvent {
	private static final String TAG = VideoActivity.class.getName();

	private final int UPDATEVIDEOBITDELAYMILLIS = 200; //������Ƶ��Ƶ�����ʵļ��ˢ��ʱ�䣨���룩

	int userID = 0;
	boolean bOnPaused = false;
	private boolean bSelfVideoOpened = false; // ������Ƶ�Ƿ��Ѵ�
	private boolean bOtherVideoOpened = false; // �Է���Ƶ�Ƿ��Ѵ�
	private Boolean mFirstGetVideoBitrate = false; //"��һ��"�����Ƶ���ʵı���
	private Boolean mFirstGetAudioBitrate = false; //"��һ��"�����Ƶ���ʵı���

	private SurfaceView mOtherView;
	private SurfaceView mMyView;
	private ImageButton mImgSwitchVideo;
	private Button mEndCallBtn;
	private TextView wait_TextView;
	private ImageButton mBtnCameraCtrl; // ������Ƶ�İ�ť
	private ImageButton mBtnSpeakCtrl; // ������Ƶ�İ�ť

	public AnyChatCoreSDK anychatSDK;

	private String mStrIP;
	private String mStrName;
	private int mSPort;
	private int mSRoomID;

	private Handler handlerWait = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				wait_TextView.setVisibility(View.GONE);
				break;
			}
		}
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		mSRoomID = intent.getIntExtra(TerminalIntent.TERMINAL_ID, 0);
		mStrIP = Config.VIDEO_SERVER_IP;
		mStrName = MyApplication.NewUser.getId()+"";
		mSPort = Config.VIDEO_SERVER_PORT;
		InitSDK();
		InitLayout();
		
		// ��ʼ����½��������
		ApplyVideoConfig();
		// ע��㲥
		//		registerBoradcastReceiver();

		// �����Ƶ�������ˣ���ѱ������ó�͸����
		handler.postDelayed(runnable, UPDATEVIDEOBITDELAYMILLIS);
	}

	private void InitSDK() {
		if (anychatSDK == null) {
			anychatSDK = AnyChatCoreSDK.getInstance(this);
			anychatSDK.SetBaseEvent(this);
			anychatSDK.InitSDK(android.os.Build.VERSION.SDK_INT, 0);
			AnyChatCoreSDK.SetSDKOptionInt(
					AnyChatDefine.BRAC_SO_LOCALVIDEO_AUTOROTATION, 1);
		}
		anychatSDK.mSensorHelper.InitSensor(this);
		AnyChatCoreSDK.mCameraHelper.SetContext(this);

		anychatSDK.Connect(mStrIP, mSPort);
	}

	private void InitLayout() {
		this.setContentView(R.layout.video_frame);
		this.setTitle("�� \"" + anychatSDK.GetUserName(userID) + "\" �Ի���");
		mMyView = (SurfaceView) findViewById(R.id.surface_local);
		mOtherView = (SurfaceView) findViewById(R.id.surface_remote);
		mImgSwitchVideo = (ImageButton) findViewById(R.id.ImgSwichVideo);
		mEndCallBtn = (Button) findViewById(R.id.endCall);
		wait_TextView = (TextView) findViewById(R.id.wait_TextView);
		mBtnSpeakCtrl = (ImageButton) findViewById(R.id.btn_speakControl);
		mBtnCameraCtrl = (ImageButton) findViewById(R.id.btn_cameraControl);
		mBtnSpeakCtrl.setOnClickListener(onClickListener);
		mBtnCameraCtrl.setOnClickListener(onClickListener);
		mImgSwitchVideo.setOnClickListener(onClickListener);
		mEndCallBtn.setOnClickListener(onClickListener);

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(5000);
					handlerWait.sendEmptyMessage(0);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} 
			}
		}).start();

		// ����ǲ���Java��Ƶ�ɼ�������Ҫ����Surface��CallBack
		if (AnyChatCoreSDK
				.GetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_CAPDRIVER) == AnyChatDefine.VIDEOCAP_DRIVER_JAVA) {
			mMyView.getHolder().addCallback(AnyChatCoreSDK.mCameraHelper);
		}

		// ����ǲ���Java��Ƶ��ʾ������Ҫ����Surface��CallBack
		if (AnyChatCoreSDK
				.GetSDKOptionInt(AnyChatDefine.BRAC_SO_VIDEOSHOW_DRIVERCTRL) == AnyChatDefine.VIDEOSHOW_DRIVER_JAVA) {
			int index = anychatSDK.mVideoHelper.bindVideo(mOtherView
					.getHolder());
			anychatSDK.mVideoHelper.SetVideoUser(index, userID);
		}

		//mMyView.setZOrderOnTop(true);

		anychatSDK.UserCameraControl(userID, 1);
		anychatSDK.UserSpeakControl(userID, 1);

		// �ж��Ƿ���ʾ��������ͷ�л�ͼ��
		if (AnyChatCoreSDK
				.GetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_CAPDRIVER) == AnyChatDefine.VIDEOCAP_DRIVER_JAVA) {
			if (AnyChatCoreSDK.mCameraHelper.GetCameraNumber() > 1) {
				// Ĭ�ϴ�ǰ������ͷ
				AnyChatCoreSDK.mCameraHelper
				.SelectVideoCapture(AnyChatCoreSDK.mCameraHelper.CAMERA_FACING_FRONT);
			}
		} else {
			String[] strVideoCaptures = anychatSDK.EnumVideoCapture();
			if (strVideoCaptures != null && strVideoCaptures.length > 1) {
				// Ĭ�ϴ�ǰ������ͷ
				for (int i = 0; i < strVideoCaptures.length; i++) {
					String strDevices = strVideoCaptures[i];
					if (strDevices.indexOf("Front") >= 0) {
						anychatSDK.SelectVideoCapture(strDevices);
						break;
					}
				}
			}
		}

		// ������Ļ����ı䱾��surfaceview�Ŀ�߱�
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			adjustLocalVideo(true);
		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			adjustLocalVideo(false);
		}

	}

	private void ApplyVideoConfig() {
		ConfigEntity configEntity = ConfigService.LoadConfig(this);
		if (configEntity.mConfigMode == 1) // �Զ�����Ƶ��������
		{
			// ���ñ�����Ƶ��������ʣ��������Ϊ0�����ʾʹ����������ģʽ��
			AnyChatCoreSDK.SetSDKOptionInt(
					AnyChatDefine.BRAC_SO_LOCALVIDEO_BITRATECTRL,
					configEntity.mVideoBitrate);
			//			if (configEntity.mVideoBitrate == 0) {
			// ���ñ�����Ƶ���������
			AnyChatCoreSDK.SetSDKOptionInt(
					AnyChatDefine.BRAC_SO_LOCALVIDEO_QUALITYCTRL,
					configEntity.mVideoQuality);
			//			}
			// ���ñ�����Ƶ�����֡��
			AnyChatCoreSDK.SetSDKOptionInt(
					AnyChatDefine.BRAC_SO_LOCALVIDEO_FPSCTRL,
					configEntity.mVideoFps);
			// ���ñ�����Ƶ����Ĺؼ�֡���
			AnyChatCoreSDK.SetSDKOptionInt(
					AnyChatDefine.BRAC_SO_LOCALVIDEO_GOPCTRL,
					configEntity.mVideoFps * 4);
			// ���ñ�����Ƶ�ɼ��ֱ���
			AnyChatCoreSDK.SetSDKOptionInt(
					AnyChatDefine.BRAC_SO_LOCALVIDEO_WIDTHCTRL,
					configEntity.mResolutionWidth);
			AnyChatCoreSDK.SetSDKOptionInt(
					AnyChatDefine.BRAC_SO_LOCALVIDEO_HEIGHTCTRL,
					configEntity.mResolutionHeight);
			// ������Ƶ����Ԥ�������ֵԽ�󣬱�������Խ�ߣ�ռ��CPU��ԴҲ��Խ�ߣ�
			AnyChatCoreSDK.SetSDKOptionInt(
					AnyChatDefine.BRAC_SO_LOCALVIDEO_PRESETCTRL,
					configEntity.mVideoPreset);
		}
		// ����Ƶ������Ч
		AnyChatCoreSDK.SetSDKOptionInt(
				AnyChatDefine.BRAC_SO_LOCALVIDEO_APPLYPARAM,
				configEntity.mConfigMode);
		// P2P����
		AnyChatCoreSDK.SetSDKOptionInt(
				AnyChatDefine.BRAC_SO_NETWORK_P2PPOLITIC,
				configEntity.mEnableP2P);
		// ������ƵOverlayģʽ����
		AnyChatCoreSDK.SetSDKOptionInt(
				AnyChatDefine.BRAC_SO_LOCALVIDEO_OVERLAY,
				configEntity.mVideoOverlay);
		// ������������
		AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_AUDIO_ECHOCTRL,
				configEntity.mEnableAEC);
		// ƽ̨Ӳ����������
		AnyChatCoreSDK.SetSDKOptionInt(
				AnyChatDefine.BRAC_SO_CORESDK_USEHWCODEC,
				configEntity.mUseHWCodec);
		// ��Ƶ��תģʽ����
		AnyChatCoreSDK.SetSDKOptionInt(
				AnyChatDefine.BRAC_SO_LOCALVIDEO_ROTATECTRL,
				configEntity.mVideoRotateMode);
		// ������Ƶ�ɼ�ƫɫ��������
		AnyChatCoreSDK.SetSDKOptionInt(
				AnyChatDefine.BRAC_SO_LOCALVIDEO_FIXCOLORDEVIA,
				configEntity.mFixColorDeviation);
		// ��ƵGPU��Ⱦ����
		AnyChatCoreSDK.SetSDKOptionInt(
				AnyChatDefine.BRAC_SO_VIDEOSHOW_GPUDIRECTRENDER,
				configEntity.mVideoShowGPURender);
		// ������Ƶ�Զ���ת����
		AnyChatCoreSDK.SetSDKOptionInt(
				AnyChatDefine.BRAC_SO_LOCALVIDEO_AUTOROTATION,
				configEntity.mVideoAutoRotation);
	}

	Handler handler = new Handler();
	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			try {
				int videoBitrate = anychatSDK.QueryUserStateInt(userID,
						AnyChatDefine.BRAC_USERSTATE_VIDEOBITRATE);
				int audioBitrate = anychatSDK.QueryUserStateInt(userID,
						AnyChatDefine.BRAC_USERSTATE_AUDIOBITRATE);
				if (videoBitrate > 0)
				{
					//handler.removeCallbacks(runnable);
					mFirstGetVideoBitrate = true;
					mOtherView.setBackgroundColor(Color.TRANSPARENT);
				}

				if(audioBitrate > 0){
					mFirstGetAudioBitrate = true;
				}

				if (mFirstGetVideoBitrate)
				{
					if (videoBitrate <= 0){						
						Toast.makeText(VideoActivity.this, "�Է���Ƶ�ж���!", Toast.LENGTH_SHORT).show();
						// �����£�����Է��˳��ˣ��н�ȥ�˵����
						mFirstGetVideoBitrate = false;
					}
				}

				if (mFirstGetAudioBitrate){
					if (audioBitrate <= 0){
						Toast.makeText(VideoActivity.this, "�Է���Ƶ�ж���!", Toast.LENGTH_SHORT).show();
						// �����£�����Է��˳��ˣ��н�ȥ�˵����
						mFirstGetAudioBitrate = false;
					}
				}

				handler.postDelayed(runnable, UPDATEVIDEOBITDELAYMILLIS);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case (R.id.ImgSwichVideo): {

				// ����ǲ���Java��Ƶ�ɼ�������Java���������ͷ�л�
				if (AnyChatCoreSDK
						.GetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_CAPDRIVER) == AnyChatDefine.VIDEOCAP_DRIVER_JAVA) {
					AnyChatCoreSDK.mCameraHelper.SwitchCamera();
					return;
				}

				String strVideoCaptures[] = anychatSDK.EnumVideoCapture();
				String temp = anychatSDK.GetCurVideoCapture();
				for (int i = 0; i < strVideoCaptures.length; i++) {
					if (!temp.equals(strVideoCaptures[i])) {
						anychatSDK.UserCameraControl(-1, 0);
						bSelfVideoOpened = false;
						anychatSDK.SelectVideoCapture(strVideoCaptures[i]);
						anychatSDK.UserCameraControl(-1, 1);
						break;
					}
				}
			}
			break;
			case (R.id.endCall): {
				exitVideoDialog();
			}
			case R.id.btn_speakControl:
				if ((anychatSDK.GetSpeakState(-1) == 1)) {
					mBtnSpeakCtrl.setImageResource(R.drawable.speak_off);
					anychatSDK.UserSpeakControl(-1, 0);
				} else {
					mBtnSpeakCtrl.setImageResource(R.drawable.speak_on);
					anychatSDK.UserSpeakControl(-1, 1);
				}

				break;
			case R.id.btn_cameraControl:
				if ((anychatSDK.GetCameraState(-1) == 2)) {
					mBtnCameraCtrl.setImageResource(R.drawable.camera_off);
					anychatSDK.UserCameraControl(-1, 0);
				} else {
					mBtnCameraCtrl.setImageResource(R.drawable.camera_on);
					anychatSDK.UserCameraControl(-1, 1);
				}
				break;
			default:
				break;
			}
		}
	};

	private void refreshAV() {
		anychatSDK.UserCameraControl(userID, 1);
		anychatSDK.UserSpeakControl(userID, 1);
		anychatSDK.UserCameraControl(-1, 1);
		anychatSDK.UserSpeakControl(-1, 1);
		mBtnSpeakCtrl.setImageResource(R.drawable.speak_on);
		mBtnCameraCtrl.setImageResource(R.drawable.camera_on);
		bOtherVideoOpened = false;
		bSelfVideoOpened = false;
	}

	private void exitVideoDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("��ȷ���˳��� ?")
		.setCancelable(false)
		.setPositiveButton("��",
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				destroyCurActivity();
			}
		})
		.setNegativeButton("��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		}).show();
	}

	private void destroyCurActivity() {
		onPause();
		onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitVideoDialog();
		}

		return super.onKeyDown(keyCode, event);
	}

	protected void onRestart() {
		super.onRestart();
		// ����ǲ���Java��Ƶ��ʾ������Ҫ����Surface��CallBack
		if (AnyChatCoreSDK
				.GetSDKOptionInt(AnyChatDefine.BRAC_SO_VIDEOSHOW_DRIVERCTRL) == AnyChatDefine.VIDEOSHOW_DRIVER_JAVA) {
			int index = anychatSDK.mVideoHelper.bindVideo(mOtherView
					.getHolder());
			anychatSDK.mVideoHelper.SetVideoUser(index, userID);
		}

		refreshAV();
		bOnPaused = false;
	}

	protected void onResume() {
		super.onResume();
	}

	protected void onPause() {
		super.onPause();
		bOnPaused = true;
		anychatSDK.UserCameraControl(userID, 0);
		anychatSDK.UserSpeakControl(userID, 0);
		anychatSDK.UserCameraControl(-1, 0);
		anychatSDK.UserSpeakControl(-1, 0);
	}

	protected void onDestroy() {
		super.onDestroy();
		anychatSDK.LeaveRoom(-1);
		anychatSDK.Logout();
		handler.removeCallbacks(runnable);
		anychatSDK.mSensorHelper.DestroySensor();
		finish();
	}

	public void adjustLocalVideo(boolean bLandScape) {
//		float width;
//		float height = 0;
//		DisplayMetrics dMetrics = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
//		width = (float) dMetrics.widthPixels / 4;
//		LinearLayout layoutLocal = (LinearLayout) this
//				.findViewById(R.id.frame_local_area);
//		FrameLayout.LayoutParams layoutParams = (android.widget.FrameLayout.LayoutParams) layoutLocal
//				.getLayoutParams();
//		if (bLandScape) {
//
//			if (AnyChatCoreSDK
//					.GetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_WIDTHCTRL) != 0)
//				height = width
//				* AnyChatCoreSDK
//				.GetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_HEIGHTCTRL)
//				/ AnyChatCoreSDK
//				.GetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_WIDTHCTRL)
//				+ 5;
//			else
//				height = (float) 3 / 4 * width + 5;
//		} else {
//
//			if (AnyChatCoreSDK
//					.GetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_HEIGHTCTRL) != 0)
//				height = width
//				* AnyChatCoreSDK
//				.GetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_WIDTHCTRL)
//				/ AnyChatCoreSDK
//				.GetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_HEIGHTCTRL)
//				+ 5;
//			else
//				height = (float) 4 / 3 * width + 5;
//		}
//		layoutParams.width = (int) width;
//		layoutParams.height = (int) height;
//		layoutLocal.setLayoutParams(layoutParams);//�ҵ�ͷ����ʾȫ��
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			adjustLocalVideo(true);
			AnyChatCoreSDK.mCameraHelper.setCameraDisplayOrientation();
		} else {
			adjustLocalVideo(false);
			AnyChatCoreSDK.mCameraHelper.setCameraDisplayOrientation();
		}

	}

	@Override
	public void OnAnyChatConnectMessage(boolean bSuccess) {
		Log.i(TAG, "OnAnyChatConnectMessage:" + "bSuccess-" + bSuccess);
		anychatSDK.Login(mStrName, "x");
	}

	@Override
	public void OnAnyChatLoginMessage(int dwUserId, int dwErrorCode) {
		Log.i(TAG, "OnAnyChatLoginMessage:" + "dwUserId-" + dwUserId + " dwErrorCode-"+dwErrorCode);
		if (dwErrorCode == 0) {
			anychatSDK.EnterRoom(mSRoomID, "");
			//			UserselfID = dwUserId;
		}
	}

	@Override
	public void OnAnyChatEnterRoomMessage(int dwRoomId, int dwErrorCode) {
		Log.i(TAG, "OnAnyChatEnterRoomMessage:" + "dwRoomId-" + dwRoomId + " dwErrorCode-"+dwErrorCode);
		int userId = videoOther();
		if(userId == 0){
			API.noticeVideo(VideoActivity.this, dwRoomId);
		}
		anychatSDK.UserCameraControl(-1, 1);// -1��ʾ�Ա�����Ƶ���п��ƣ��򿪱�����Ƶ
		anychatSDK.UserSpeakControl(-1, 1);// -1��ʾ�Ա�����Ƶ���п��ƣ��򿪱�����Ƶ
	}

	@Override
	public void OnAnyChatOnlineUserMessage(int dwUserNum, int dwRoomId) {
		Log.i(TAG, "OnAnyChatOnlineUserMessage:" + "dwUserNum" + dwUserNum + " dwRoomId-"+dwRoomId);
	}

	@Override
	public void OnAnyChatUserAtRoomMessage(int dwUserId, boolean bEnter) {
		Log.i(TAG, "OnAnyChatUserAtRoomMessage:" + "dwUserId" + dwUserId + " bEnter-"+bEnter);
		if (!bEnter) {
			if (dwUserId == userID) {
				Toast.makeText(VideoActivity.this, "�Է����뿪��", Toast.LENGTH_SHORT).show();
				userID = 0;
				anychatSDK.UserCameraControl(dwUserId, 0);
				anychatSDK.UserSpeakControl(dwUserId, 0);
				bOtherVideoOpened = false;
			}

		} 
		if (userID != 0)
			return;
		videoOther();
	}

	@Override
	public void OnAnyChatLinkCloseMessage(int dwErrorCode) {
		Log.i(TAG, "OnAnyChatLinkCloseMessage:" + "dwErrorCode" + dwErrorCode);
		// �������ӶϿ�֮���ϲ���Ҫ�����ر��Ѿ��򿪵�����Ƶ�豸
		if (bOtherVideoOpened) {
			anychatSDK.UserCameraControl(userID, 0);
			anychatSDK.UserSpeakControl(userID, 0);
			bOtherVideoOpened = false;
		}
		if (bSelfVideoOpened) {
			anychatSDK.UserCameraControl(-1, 0);
			anychatSDK.UserSpeakControl(-1, 0);
			bSelfVideoOpened = false;
		}

		finish();
		//		destroyCurActivity();
		//		Intent mIntent = new Intent("VideoActivity");
		// ���͹㲥
		//		sendBroadcast(mIntent);
	}

	private int videoOther(){
		int[] ids = anychatSDK.GetOnlineUser();
		userID = 0;
		for (int id : ids) {
			userID = id;
		}
		int index = anychatSDK.mVideoHelper.bindVideo(mOtherView
				.getHolder());
		anychatSDK.mVideoHelper.SetVideoUser(index, userID);

		anychatSDK.UserCameraControl(userID, 1);
		anychatSDK.UserSpeakControl(userID, 1);
		return userID;
	}
}

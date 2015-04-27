package com.example.zf_pad.video.config;

public class ConfigEntity {
	public static final int VIDEO_MODE_SERVERCONFIG = 0;	// ��������Ƶ��������
	public static final int VIDEO_MODE_CUSTOMCONFIG = 1;	// �Զ�����Ƶ��������
	
	public static final int VIDEO_QUALITY_NORMAL = 2;		// ��ͨ��Ƶ����
	public static final int VIDEO_QUALITY_GOOD = 3;			// �е���Ƶ����
	public static final int VIDEO_QUALITY_BEST = 4;			// �Ϻ���Ƶ����
	
	public int mConfigMode = VIDEO_MODE_SERVERCONFIG;
	public int mResolutionWidth = 0;
	public int mResolutionHeight = 0;
	
	public int mVideoBitrate = 150*1000;					// ������Ƶ����
	public int mVideoFps = 10;								// ������Ƶ֡��
	public int mVideoQuality = VIDEO_QUALITY_GOOD;
	public int mVideoPreset = 1;
	public int mVideoOverlay = 1;							// ������Ƶ�Ƿ����Overlayģʽ
	public int mVideoRotateMode = 0;						// ������Ƶ��תģʽ
	public int mFixColorDeviation = 0;						// ����������Ƶ�ɼ�ƫɫ��0 �ر�(Ĭ�ϣ��� 1 ����
	public int mVideoShowGPURender = 0;						// ��Ƶ����ͨ��GPUֱ����Ⱦ��0  �ر�(Ĭ��)�� 1 ����
	public int mVideoAutoRotation = 1;						// ������Ƶ�Զ���ת���ƣ�����Ϊint�ͣ� 0��ʾ�رգ� 1 ����[Ĭ��]����Ƶ��תʱ��Ҫ�ο�������Ƶ�豸���������
	
	public int mEnableP2P = 1;
	public int mUseARMv6Lib = 0;							// �Ƿ�ǿ��ʹ��ARMv6ָ���Ĭ�����ں��Զ��ж�
	public int mEnableAEC = 1;								// �Ƿ�ʹ�û�����������
	public int mUseHWCodec = 0;								// �Ƿ�ʹ��ƽ̨����Ӳ���������
}

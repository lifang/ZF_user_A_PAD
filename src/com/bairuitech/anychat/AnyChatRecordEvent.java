package com.bairuitech.anychat;

// AnyChat��Ƶ¼���¼�֪ͨ�ӿ�
public interface AnyChatRecordEvent {
    // ��Ƶ¼������¼�
	public void OnAnyChatRecordEvent(int dwUserId, String lpFileName, int dwElapse, int dwFlags, int dwParam, String lpUserStr);
	// ͼ��ץ������¼�
	public void OnAnyChatSnapShotEvent(int dwUserId, String lpFileName, int dwFlags, int dwParam, String lpUserStr);
}
package com.bairuitech.anychat;

// AnyChat��Ƶ�����¼�֪ͨ�ӿ�
public interface AnyChatVideoCallEvent {
    public void OnAnyChatVideoCallEvent(int dwEventType, int dwUserId, int dwErrorCode, int dwFlags, int dwParam, String userStr);
}
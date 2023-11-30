package com.example.simplechattingservice.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    public enum MessageType {
        ENTER, QUIT, TALK
    }

    private MessageType type; // 메세지 타입
    private String roomId; // 채팅방 아이디
    private String sender; // 메세지 보낸 사람
    private String message; // 메세지
}

package com.example.simplechattingservice.config;

import com.example.simplechattingservice.entity.ChatMessage;
import com.example.simplechattingservice.entity.ChatRoom;
import com.example.simplechattingservice.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Set;


// WebSocket Handler 작성
// 소켓 통신은 서버와 클라이언트가 1:n으로 관계를 맺는다. 따라서 한 서버에 여러 클라이언트 접속 가능
// 서버에는 여러 클라이언트가 발송한 메세지를 받아 처리해줄 핸들러가 필요
// TextWebSocketHandler를 상속받아 핸들러 작성
// 클라이언트로 받은 메세지를 로그로 출력하고 클라이언트로 환영 메세지를 보내줌

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper; // json 파싱
    private final ChatService chatService; // 채팅방 관련 서비스

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload(); // 클라이언트로부터 받은 메세지

        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class); // 메세지 파싱
        ChatRoom chatRoom = chatService.findRoomById(chatMessage.getChatRoomId()); // 채팅방 조회

        Set<WebSocketSession> chatRoomSessions = chatRoom.getSessions(); // 채팅방에 접속한 클라이언트 목록

        if (chatMessage.getType().equals(ChatMessage.MessageType.ENTER)) { // 채팅방 입장 시
            chatRoomSessions.add(session); // 채팅방에 접속한 클라이언트 목록 초기화
            chatMessage.setMessage(chatMessage.getSender() + "님이 입장하셨습니다.");
            sendToEachSocket(chatRoomSessions, new TextMessage(objectMapper.writeValueAsString(chatMessage))); // 채팅방에 입장 메세지 전송
        } else if (chatMessage.getType().equals(ChatMessage.MessageType.QUIT)) {
            chatRoomSessions.remove(session); // 채팅방에서 나간 클라이언트 제거
            chatMessage.setMessage(chatMessage.getSender() + "님이 퇴장하셨습니다.");
            sendToEachSocket(chatRoomSessions, new TextMessage(objectMapper.writeValueAsString(chatMessage))); // 채팅방에 퇴장 메세지 전송
        } else { // 채팅 메세지 전송 시
            sendToEachSocket(chatRoomSessions, message); // 채팅방에 메세지 전송
        }
    }

    private void sendToEachSocket(Set<WebSocketSession> chatRoomSessions, TextMessage message) {
        chatRoomSessions.parallelStream().forEach(session -> {
            try {
                session.sendMessage(message); // 채팅방에 메세지 전송
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    }
}

package com.example.simplechattingservice.chatroom;

import com.example.simplechattingservice.entity.ChatRoom;
import com.example.simplechattingservice.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ChatRoomTest {

    private ChatService chatService;

    @BeforeEach
    void init() {
        ObjectMapper objectMapper = new ObjectMapper();
        chatService = new ChatService(objectMapper);
        chatService.init();
    }

    @Test
    void createChatRoomAndFindById() {
        // 채팅방 생성
        String roomName = "Test Room";
        ChatRoom createdRoom = chatService.createChatRoom(roomName);

        // 생성된 채팅방 검증
        assertNotNull(createdRoom);
        assertEquals(roomName, createdRoom.getName());

        // ID로 채팅방 조회
        ChatRoom foundRoom = chatService.findRoomById(createdRoom.getRoomId());
        assertNotNull(foundRoom);
        assertEquals(createdRoom.getRoomId(), foundRoom.getRoomId());
        assertEquals(createdRoom.getName(), foundRoom.getName());
    }
}

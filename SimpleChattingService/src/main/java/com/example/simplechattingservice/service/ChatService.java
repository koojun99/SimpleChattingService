package com.example.simplechattingservice.service;

import com.example.simplechattingservice.entity.ChatRoom;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

    private final ObjectMapper objectMapper;
    private Map<String, ChatRoom> chatRooms; // 채팅방 목록

    @PostConstruct
    public void init() {
        chatRooms = new LinkedHashMap<>(); // 채팅방 목록 초기화
    }

    public List<ChatRoom> findAllRoom() {
        return new ArrayList<>(chatRooms.values()); // 채팅방 목록 반환
    }

    public ChatRoom findRoomById(String roomId) {
        return chatRooms.get(roomId); // 채팅방 조회
    }

    public ChatRoom createChatRoom(String name) {
        String randomId = UUID.randomUUID().toString(); // 랜덤 아이디 생성
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(randomId)
                .name(name)
                .build();
        chatRooms.put(randomId, chatRoom); // 채팅방 목록에 채팅방 추가
        return chatRoom;
    }
}

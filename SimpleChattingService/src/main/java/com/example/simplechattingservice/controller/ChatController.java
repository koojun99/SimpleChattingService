package com.example.simplechattingservice.controller;

import com.example.simplechattingservice.entity.ChatRoom;
import com.example.simplechattingservice.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    // 채팅방 목록 조회
    @RequestMapping("/chat/rooms")
    public String rooms(Model model) {
        List<ChatRoom> rooms = chatService.findAllRoom();
        model.addAttribute("rooms", rooms);
        return "/chat/rooms"; // templates/chat/rooms.html을 반환
    }

    // 채팅방 생성
    @PostMapping("/chat/createRoom")
    public String createRoom(Model model,
                             @RequestParam String name,
                             String username) {
        ChatRoom room = chatService.createChatRoom(name);
        model.addAttribute("room", room);
        model.addAttribute("username", username);
        return "/chat/room"; // 채팅방 입장
    }

    // 채팅방 입장
    @GetMapping("/chat/room")
    public String room(Model model, @RequestParam String roomId) {
        ChatRoom room = chatService.findRoomById(roomId);
        model.addAttribute("room", room);
        return "/chat/room"; // 입장
    }
}

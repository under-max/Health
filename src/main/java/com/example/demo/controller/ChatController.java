package com.example.demo.controller;

import com.example.demo.entity.AuthUser;
import com.example.demo.request.ChatSaveMsgRequest;
import com.example.demo.response.ChatMessageResponse;
import com.example.demo.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class ChatController {

    @Autowired
    ChatService chatService;

    @PostMapping("/chat/saveMessage")
    public void saveMessage(@RequestBody ChatSaveMsgRequest chatSaveMsgRequest) {
        System.out.println("chatSaveMsgRequest = " + chatSaveMsgRequest);
        chatService.saveMessage(chatSaveMsgRequest);
    }

    @GetMapping("/chat/getMessageList")
    public List<ChatMessageResponse> getMessageList (AuthUser authUser) {
        List<ChatMessageResponse> messageList = chatService.getMessageList(authUser.getUserId().intValue());
        System.out.println("messageList = " + messageList);
        return messageList;
    }
}

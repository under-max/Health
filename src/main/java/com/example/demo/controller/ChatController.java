package com.example.demo.controller;

import com.example.demo.entity.AuthUser;
import com.example.demo.request.ChatSaveMsgRequest;
import com.example.demo.response.ChatMessageResponse;
import com.example.demo.response.ChatSaveMsgDTO;
import com.example.demo.response.UserDetailResponse;
import com.example.demo.service.ChatService;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Transactional
public class ChatController {

    @Autowired
    ChatService chatService;

    @Autowired
    UserService userService;

    @PostMapping("/chat/saveMessage")
    public void saveMessage(@RequestBody ChatSaveMsgRequest chatSaveMsgRequest, AuthUser authUser) {

        Integer authority = userService.getAuthority(authUser.getUserId());
        ChatSaveMsgDTO chatSaveMsgDTO = new ChatSaveMsgDTO();

        if (authority == 1) {
            chatSaveMsgDTO = new ChatSaveMsgDTO();
            chatSaveMsgDTO.setMessageFrom(authUser.getUserId().intValue());
            chatSaveMsgDTO.setMessageTo(chatSaveMsgRequest.getTrainerId());
            chatSaveMsgDTO.setMessage(chatSaveMsgRequest.getMessage());
        } else if(authority == 2) {
            chatSaveMsgDTO = new ChatSaveMsgDTO();
            chatSaveMsgDTO.setMessageFrom(authUser.getUserId().intValue());
            chatSaveMsgDTO.setMessageTo(chatSaveMsgRequest.getMemberId());
            chatSaveMsgDTO.setMessage(chatSaveMsgRequest.getMessage());
        }
            chatService.saveMessage(chatSaveMsgDTO);
    }

    @GetMapping("/chat/getMessageList/{id}")
    public List<ChatMessageResponse> getMessageList (@PathVariable Long id, AuthUser authUser) {
        UserDetailResponse getUserDetail = userService.getUserDetail(id);
        Integer messageFrom = getUserDetail.getId();
        Integer messageTo = getUserDetail.getTrainerId();
        List<ChatMessageResponse> messageList = chatService.getMessageList(messageFrom, messageTo);
        return messageList;
    }



}


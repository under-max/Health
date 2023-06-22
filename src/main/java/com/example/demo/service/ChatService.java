package com.example.demo.service;

import com.example.demo.entity.AuthUser;
import com.example.demo.mapper.ChatMapper;
import com.example.demo.request.ChatSaveMsgRequest;
import com.example.demo.response.ChatMessageResponse;
import com.example.demo.response.ChatSaveMsgDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    ChatMapper chatMapper;


    public void saveMessage(ChatSaveMsgDTO chatSaveMsgDTO) {
        chatMapper.saveMessage(chatSaveMsgDTO);
    }

    public List<ChatMessageResponse> getMessageList(Integer messageFrom, Integer messageTo) {
        List<ChatMessageResponse> list = chatMapper.getMessageList(messageFrom, messageTo);
        return list;
    }


}

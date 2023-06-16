package com.example.demo.service;

import com.example.demo.mapper.ChatMapper;
import com.example.demo.request.ChatSaveMsgRequest;
import com.example.demo.response.ChatMessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    ChatMapper chatMapper;


    public void saveMessage(ChatSaveMsgRequest chatSaveMsgRequest) {
        chatMapper.saveMessage(chatSaveMsgRequest);
    }

    public List<ChatMessageResponse> getMessageList(Integer memberId) {
        List<ChatMessageResponse> list = chatMapper.getMessageList(memberId);
        System.out.println("memberId " + memberId);
        return list;
    }
}

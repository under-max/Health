package com.example.demo.mapper;

import com.example.demo.request.ChatSaveMsgRequest;
import com.example.demo.response.ChatMessageResponse;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChatMapper {


    @Insert("""
            INSERT INTO 
                CHAT 
                    (from, to, message)
                VALUES 
                    (#{from}, #{to}, #{message})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "chatId")
    void saveMessage(ChatSaveMsgRequest chatSaveMsgRequest);

    @Select("""
            SELECT
            	c.chatId
            	, c.from
            	, c.to
            	, c.timestamp
            	, m.name		AS memberName
            	, c.message
            	, m.authority
            	, t.name 		AS trainerName
            FROM CHAT c
            LEFT JOIN
            	`MEMBER` m ON c.from = m.id
            LEFT JOIN
            	TRAINER t ON t.id = m.trainerId
            WHERE c.from = #{from};
            """)
    List<ChatMessageResponse> getMessageList(Integer memberId);
}

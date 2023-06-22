package com.example.demo.mapper;

import com.example.demo.request.ChatSaveMsgRequest;
import com.example.demo.response.ChatMessageResponse;
import com.example.demo.response.ChatSaveMsgDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ChatMapper {


    @Insert("""
            INSERT INTO
                CHAT 
                    (messageFrom, messageTo, message)
                VALUES 
                    (#{messageFrom}, #{messageTo}, #{message})
            """)
    void saveMessage(ChatSaveMsgDTO chatSaveMsgDTO);

    @Select("""
            SELECT
            	c.chatId
            	, c.messageFrom
            	, c.messageTo
            	, c.timestamp
            	, m.name		AS memberName
            	, c.message
            	, m.authority
            	, t.name 		AS trainerName
            FROM CHAT c
            LEFT JOIN
            	`MEMBER` m ON c.messageFrom = m.id
            LEFT JOIN
            	TRAINER t ON t.id = m.trainerId
            WHERE (
                        c.messageFrom = #{messageFrom} 
                    OR 
                        c.messageTo = #{messageFrom}
                  )
                    AND
                  (
                        c.messageFrom = #{messageTo} 
                    OR 
                        c.messageTo = #{messageTo}
                  )
            ORDER BY c.timestamp DESC;
            """)
    List<ChatMessageResponse> getMessageList(@Param("messageFrom") Integer messageFrom, @Param("messageTo")Integer messageTo);


}

package com.example.demo.mapper;

import com.example.demo.entity.Memo;
import com.example.demo.request.MemoSaveRequest;
import com.example.demo.request.MemoUpdateRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MemoMapper {

    @Select("""
            SELECT me.memoId, me.division, me.inserted, me.title, me.content
            	FROM MEMO me
            WHERE me.memberId = #{id};
            """)
    @Options(keyProperty = "memoId", useGeneratedKeys = true)
    List<Memo> getMemoList(Long id);

    @Insert("""
            INSERT INTO MEMO (division, title, content, memberId)
            VALUES (#{division}, #{title}, #{content}, #{memberId})
            """)
    void saveMemo(MemoSaveRequest memoSaveRequest);

    @Select("""
            SELECT * FROM MEMO
            WHERE memoId = #{memoId}
            """)
    Memo getMemo(Integer memoId);


    @Update("""
            UPDATE MEMO 
            SET title = #{title}, content = #{content}
            WHERE memoId = #{memoId} 
            """)
    void updateMemo(MemoUpdateRequest memoUpdateRequest);

    @Delete("""
            DELETE FROM MEMO
            WHERE memoId = #{memoId}  
            """)
    void deleteMemo(Integer memoId);
}

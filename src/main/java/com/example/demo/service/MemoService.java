package com.example.demo.service;

import com.example.demo.entity.Memo;
import com.example.demo.mapper.MemoMapper;
import com.example.demo.request.MemoSaveRequest;
import com.example.demo.request.MemoUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MemoService {

    @Autowired
    MemoMapper memoMapper;

    public List<Memo> getMemoList(Long id) {
        return memoMapper.getMemoList(id);
    }

    public void saveMemo(MemoSaveRequest memoSaveRequest) {
        memoMapper.saveMemo(memoSaveRequest);
    }

    public Memo getMemo(Integer memoId) {
        return memoMapper.getMemo(memoId);
    }

    public void updateMemo(MemoUpdateRequest memoUpdateRequest) {
        memoMapper.updateMemo(memoUpdateRequest);
    }

    public void deleteMemo(Integer memoId) {
        memoMapper.deleteMemo(memoId);
    }
}

package com.example.demo.controller;

import com.example.demo.entity.AuthUser;
import com.example.demo.entity.Memo;
import com.example.demo.request.MemoSaveRequest;
import com.example.demo.request.MemoUpdateRequest;
import com.example.demo.service.MemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class MemoController {

    @Autowired
    MemoService memoService;

    @GetMapping("/memo/getMemoList/{id}")
    public List<Memo> getMomoList(@PathVariable Long id, AuthUser authUser) {
        List<Memo> getMemoList = memoService.getMemoList(id);
        return getMemoList;
    }

    @PostMapping("/memo/saveMemo")
    public void saveMemo(@RequestBody MemoSaveRequest memoSaveRequest) {
        memoService.saveMemo(memoSaveRequest);
    }

    @GetMapping("/memo/getMemo")
    public Memo getMemo(Integer memoId) {
        return memoService.getMemo(memoId);
    }

    @PostMapping("/memo/updateMemo")
    public void updateMemo(@RequestBody MemoUpdateRequest memoUpdateRequest) {
        memoService.updateMemo(memoUpdateRequest);
    }

    @PostMapping("/memo/deleteMemo")
    public void deleteMemo(Integer memoId) {
        memoService.deleteMemo(memoId);
    }
}

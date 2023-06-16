package com.example.demo.exception;

public class DuplicateSchedule extends JJinBBangException {

    private final static String message = "중복된 PT시간 입니다.";

    public DuplicateSchedule() {
        super(message);
    }

    public DuplicateSchedule(Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getStatusCode() {
        return 429;
    }
}

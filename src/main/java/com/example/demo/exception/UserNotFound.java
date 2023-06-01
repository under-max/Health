package com.example.demo.exception;

public class UserNotFound extends JJinBBangException {

    private static final String MESSAGE = "존재하지 회원입니다.";

    public UserNotFound() {
        super(MESSAGE);
    }

    //    public PostNotFound(Throwable cause) {
//        super(MESSAGE, cause);
//    }
    @Override
    public int getStatusCode() {
        return 404;
    }
}

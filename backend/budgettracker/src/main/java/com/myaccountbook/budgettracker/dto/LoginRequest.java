package com.myaccountbook.budgettracker.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String username;
    private String password;

    //기본 생성자
    public LoginRequest() {

    }

    //생성자
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }


}

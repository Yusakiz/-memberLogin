package com.example.membersystem.dto;

public class LoginResponse {
    private String token;
    private MemberResponse member;

    public LoginResponse() {}

    public LoginResponse(String token, MemberResponse member) {
        this.token = token;
        this.member = member;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public MemberResponse getMember() {
        return member;
    }

    public void setMember(MemberResponse member) {
        this.member = member;
    }
}

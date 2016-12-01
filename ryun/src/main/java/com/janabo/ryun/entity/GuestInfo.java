package com.janabo.ryun.entity;

/**
 * 作者：janabo on 2016/11/26 16:37
 */
public class GuestInfo {
    private String guest_id;
    private String guest_name;
    private String guest_info;
    private String callid;
    private String code;
    private String token;

    public String getGuest_id() {
        return guest_id;
    }

    public void setGuest_id(String guest_id) {
        this.guest_id = guest_id;
    }

    public String getGuest_name() {
        return guest_name;
    }

    public void setGuest_name(String guest_name) {
        this.guest_name = guest_name;
    }

    public String getGuest_info() {
        return guest_info;
    }

    public void setGuest_info(String guest_info) {
        this.guest_info = guest_info;
    }

    public String getCallid() {
        return callid;
    }

    public void setCallid(String callid) {
        this.callid = callid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

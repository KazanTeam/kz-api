package com.kazan.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.StringUtils;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private String token;

    private String refreshToken;

    private Integer tokenExpire;

    private Integer userId;

    private String username;

    private String password;

    private String firstname;

    private String lastname;

    private String email;

    private String phone;

    private String language;

    private String refId;

    private Integer telegramId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public Integer getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(Integer telegramId) {
        this.telegramId = telegramId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Integer getTokenExpire() {
        return tokenExpire;
    }

    public void setTokenExpire(Integer tokenExpire) {
        this.tokenExpire = tokenExpire;
    }

    public boolean validation() {
        if (StringUtils.isEmpty(this.username) || StringUtils.isEmpty(this.password) ) {
            return false;
        }
        return true;
    }

}

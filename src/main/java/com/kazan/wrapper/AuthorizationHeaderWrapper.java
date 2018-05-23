package com.kazan.wrapper;

import com.fasterxml.jackson.annotation.JsonSetter;

public class AuthorizationHeaderWrapper {
	private String sub;
	private Boolean email_verified;
	private String iss;
	private String phone_number_verified;
	private String aud;
	private String event_id;
	private String token_use;
	private Long auth_time;
	private String nickname;
	private String phone_number;
	private String exp;
	private String iat;
	private String email;
	private String username;
	public String getSub() {
		return sub;
	}
	public void setSub(String sub) {
		this.sub = sub;
	}
	public Boolean getEmail_verified() {
		return email_verified;
	}
	public void setEmail_verified(Boolean email_verified) {
		this.email_verified = email_verified;
	}
	public String getIss() {
		return iss;
	}
	public void setIss(String iss) {
		this.iss = iss;
	}
	public String getPhone_number_verified() {
		return phone_number_verified;
	}
	public void setPhone_number_verified(String phone_number_verified) {
		this.phone_number_verified = phone_number_verified;
	}
	public String getAud() {
		return aud;
	}
	public void setAud(String aud) {
		this.aud = aud;
	}
	public String getEvent_id() {
		return event_id;
	}
	public void setEvent_id(String event_id) {
		this.event_id = event_id;
	}
	public String getToken_use() {
		return token_use;
	}
	public void setToken_use(String token_use) {
		this.token_use = token_use;
	}
	public Long getAuth_time() {
		return auth_time;
	}
	public void setAuth_time(Long auth_time) {
		this.auth_time = auth_time;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public String getExp() {
		return exp;
	}
	public void setExp(String exp) {
		this.exp = exp;
	}
	public String getIat() {
		return iat;
	}
	public void setIat(String iat) {
		this.iat = iat;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	@JsonSetter("cognito:username")
	public void setUsername(String username) {
		this.username = username;
	}
	
}

package com.kazan.wrapper;

import java.util.List;

import com.kazan.model.KazanObject;

public class ObjectRequestWrapper {
	private String username;
	private String password;
	private List<String> groupAliases;
	private String symbol;
	private Integer mode;
	private String accountName;
	private String accountNumber;
	private String accountServer;
	private List<KazanObject> objects;
	private String period;
	private Integer orderTicket;
	private Double orderPrice;
	private Double orderSL;
	private Double orderTP;
	private Integer orderType;
	private String image;
	private String note;
	private String getFromUser;
	
	public Integer getOrderTicket() {
		return orderTicket;
	}
	public void setOrderTicket(Integer orderTicket) {
		this.orderTicket = orderTicket;
	}
	public Double getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(Double orderPrice) {
		this.orderPrice = orderPrice;
	}
	public Double getOrderSL() {
		return orderSL;
	}
	public void setOrderSL(Double orderSL) {
		this.orderSL = orderSL;
	}
	public Double getOrderTP() {
		return orderTP;
	}
	public void setOrderTP(Double orderTP) {
		this.orderTP = orderTP;
	}
	public Integer getOrderType() {
		return orderType;
	}
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getGetFromUser() {
		return getFromUser;
	}
	public void setGetFromUser(String getFromUser) {
		this.getFromUser = getFromUser;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
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
	public List<String> getGroupAliases() {
		return groupAliases;
	}
	public void setGroupAliases(List<String> groupAliases) {
		this.groupAliases = groupAliases;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public Integer getMode() {
		return mode;
	}
	public void setMode(Integer mode) {
		this.mode = mode;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getAccountServer() {
		return accountServer;
	}
	public void setAccountServer(String accountServer) {
		this.accountServer = accountServer;
	}
	public List<KazanObject> getObjects() {
		return objects;
	}
	public void setObjects(List<KazanObject> objects) {
		this.objects = objects;
	}	
}

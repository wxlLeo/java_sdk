package com.fanlitou.controler;

public class Param {
	public String fcode;
	public String phone_num;
	public String t;
	public String uid;
	public String register_token;
	public String login_token;
	public String bid_url;
	public String source;
	public String start_time;
	public String end_time;
	public String sign;
	
	@Override
	public String toString(){
		return "fcode:" + fcode + " phone_num:" + phone_num + " t:" + t + " uid:" + uid + " sign:" + sign 
				+ " register_token:" + register_token + " login_token:" + login_token + " bid_url:" + bid_url + " source:" + source
				+ " start_time:" + start_time + " end_time:" + end_time;
	}
	
	public String getFcode() {
		return fcode;
	}
	public void setFcode(String fcode) {
		this.fcode = fcode;
	}
	public String getPhone_num() {
		return phone_num;
	}
	public void setPhone_num(String phone_num) {
		this.phone_num = phone_num;
	}
	public String getT() {
		return t;
	}
	public void setT(String t) {
		this.t = t;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getRegistertoken() {
		return register_token;
	}

	public void setRegistertoken(String registertoken) {
		this.register_token = registertoken;
	}

	public String getLogintoken() {
		return login_token;
	}

	public void setLogintoken(String logintoken) {
		this.login_token = logintoken;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getBidUrl() {
		return bid_url;
	}

	public void setBidUrl(String bidUrl) {
		this.bid_url = bidUrl;
	}

	public String getStartTime() {
		return start_time;
	}

	public void setStartTime(String startTime) {
		this.start_time = startTime;
	}

	public String getEndTime() {
		return end_time;
	}

	public void setEndTime(String endTime) {
		this.end_time = endTime;
	}
	
	

}

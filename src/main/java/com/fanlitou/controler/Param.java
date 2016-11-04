package com.fanlitou.controler;

public class Param {
	public String fcode;
	public String phone_num;
	public String t;
	public String serial_num;
	public String uid;
	public String registertoken;
	public String logintoken;
	public String bidUrl;
	public String source;
	public String startTime;
	public String endTime;
	public String sign;
	
	@Override
	public String toString(){
		return "fcode:" + fcode + " phone_num:" + phone_num + " t:" + t + " serial_num:" + serial_num + " uid:" + uid + " sign:" + sign;
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
	public String getSerial_num() {
		return serial_num;
	}
	public void setSerial_num(String serial_num) {
		this.serial_num = serial_num;
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
		return registertoken;
	}

	public void setRegistertoken(String registertoken) {
		this.registertoken = registertoken;
	}

	public String getLogintoken() {
		return logintoken;
	}

	public void setLogintoken(String logintoken) {
		this.logintoken = logintoken;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getBidUrl() {
		return bidUrl;
	}

	public void setBidUrl(String bidUrl) {
		this.bidUrl = bidUrl;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	

}

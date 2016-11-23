package com.fanlitou.pojo;

public class Result {
	
	public boolean isSuccess = false;
	public String status;
	public String user_name = "";
	public String register_token = "";
	public String msg = "";
	public String login_token = "";
	
	
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getRegister_token() {
		return register_token;
	}
	public void setRegister_token(String register_token) {
		this.register_token = register_token;
	}
	public String getLogin_token() {
		return login_token;
	}
	public void setLogin_token(String login_token) {
		this.login_token = login_token;
	}

}

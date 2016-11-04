package com.fanlitou;

public class Constants {
	
	public interface AutoRegisterStatus{
		public static final String REGISTER_SUCCESS = "01";				//注册成功
		public static final String BIND_SUCCESS = "02";					//绑定成功
		public static final String REGISTER_SUCCESS_BIND_FAIL = "03";	//注册成功,绑定失败
		public static final String BIND_SUCCESS_NOT_FLT_USER = "04";	//绑定成功,但非返利投用户
		public static final String GET_TOKEN_SUCCESS = "05";			//获取token成功
		public static final String NOT_PASS_VALIDATION = "41";			//未通过安全校验
		public static final String USER_ALREADY_EXIST = "42";			//注册失败,该用户已存在,不可重复注册
		public static final String REGISTER_FAIL = "43";				//注册失败,注明其他详细原因
		public static final String BIND_FAIL = "44";					//绑定失败
		public static final String OTHER_FAIL_STATUS = "45";			//其他错误
	}
	
	public interface RegisterQueryStatus{
		public static final String NOT_REGISTER = "10";				//新用户，未注册
		public static final String NOT_FANLITOU_USER = "11";		//老用户，已注册，非渠道用户，但未绑定返利投账户
		public static final String FANLITOU_USER = "12";			//老用户，已注册，返利投渠道用户
		public static final String OTHER_CHANNEL_USER = "13";		//老用户，已注册，其他渠道用户
		public static final String OLD_ACCOUNT_BIND = "14";			//老用户，已注册，已有平台账户绑定
	}
	
	public interface RegisterType{
		public static final int REGISTER = 0;						//新用户注册
		public static final int BIND = 1;							//老用户绑定
	}
}

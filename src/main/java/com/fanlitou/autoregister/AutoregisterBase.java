package com.fanlitou.autoregister;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.fanlitou.utils.MD5;
import com.fanlitou.utils.AES;
import com.fanlitou.pojo.Result;
import com.alibaba.fastjson.JSONObject;
import com.fanlitou.Constants;
import com.fanlitou.Utils;

public class AutoregisterBase {
	String notifyUrlTest;
	String notifyUrlProduction;
	String fcode;
	String secretKey;
	String cryptionKey;
	
	String userAgent;
	String contentType;
	
	AES cryption;
	
	
	
	public AutoregisterBase(){
		this.notifyUrlTest = "http://test.fanlitou.com/api/user_bound/notify/";
		this.notifyUrlProduction = "https://www.fanlitou.com/api/user_bound/notify/";
		this.fcode = "fanlitou";
		this.secretKey = "1234567890123456";
		this.cryptionKey = "1234567890123456";  //初始化密钥，长度必须为16位
		
		this.userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/534.30 (KHTML, like Gecko) Ubuntu/11.04 Chromium/12.0.742.112 Chrome/12.0.742.112 Safari/534.30";
		this.contentType = "application/x-www-form-urlencoded; charset=UTF-8";
		cryption = new AES(this.cryptionKey);
	}
	
	/**
	 * @param phoneNum
	 * @param fcode
	 * @param timeStamp
	 * @return md5验签
	 */
	public String sign(String phoneNum, String fcode, String timeStamp){
		String signStr = phoneNum + fcode + timeStamp + this.secretKey;
		String md5Sign = MD5.GetMD5Code(signStr);
		return md5Sign;
	}
	
	/**
	 * @param sign
	 * @param phoneNum
	 * @param fcode
	 * @param timeStamp
	 * @param serialNum
	 * 验证注册sign
	 */
	public Result validateRegisterSign(String sign, String phoneNum, String fcode, String timeStamp){
		String mySign = this.sign(this.cryption.decrypt(phoneNum), fcode, this.cryption.decrypt(timeStamp));
		Result validationResult = new Result();
		if(StringUtils.equals(mySign, sign)){
			validationResult.setSuccess(true);
		}else{
			validationResult.setSuccess(false);
			validationResult.setStatus(Constants.AutoRegisterStatus.NOT_PASS_VALIDATION);
			validationResult.setPhoneNum(phoneNum);
			validationResult.setErrMsg("未通过安全校验");
		}
		return validationResult;
	}
	
	/**
	 * @param sign
	 * @param fcode
	 * @param timeStamp
	 * @return 验证投资记录sign
	 */
	public Result validateInvestRecordSign(String sign, String fcode, String timeStamp){
		String signStr = timeStamp + fcode + this.secretKey;
		Result validationResult = new Result();
		String mySign = MD5.GetMD5Code(signStr);
		if(StringUtils.equals(mySign, sign)){
			validationResult.setSuccess(true);
		}else{
			validationResult.setSuccess(false);
			validationResult.setStatus(Constants.AutoRegisterStatus.NOT_PASS_VALIDATION);
			validationResult.setErrMsg("未通过安全校验");
		}
		return validationResult;
	}
	
	/**
	 * @param startTime
	 * @param endTime
	 * @return 验证投资记录查询开始结束时间，时间跨度不能超过30天
	 */
	public Result validateStartEndTime(String startTime, String endTime){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Result validationResult = new Result();
		try {
			Date startDate = sdf.parse(startTime);
			Date endDate = sdf.parse(endTime);
			Calendar calendar=Calendar.getInstance();   
		    calendar.setTime(startDate); 
		    calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)+30);  
		    Date startDateAfter30Days = calendar.getTime();
		    if(startDateAfter30Days.before(endDate)){
		    	validationResult.setSuccess(false);
		    	validationResult.setStatus(Constants.AutoRegisterStatus.NOT_PASS_VALIDATION);
				validationResult.setErrMsg("请求时间跨度超过30天");
		    }else{
		    	validationResult.setSuccess(true);
		    }
		} catch (ParseException e) {
			e.printStackTrace();
			
			validationResult.setStatus(Constants.AutoRegisterStatus.NOT_PASS_VALIDATION);
			validationResult.setErrMsg("查询日期格式错误");
		}
		return validationResult;
	}
	
	/**
	 * @param data
	 * @return 验证数据解密是否正确
	 */
	public Result validateRequestData(String data){
		String decryptStr = this.cryption.decrypt(data);
		Result validationResult = new Result();
		if(StringUtils.isEmpty(decryptStr)){
			validationResult.setSuccess(false);
			validationResult.setStatus(Constants.AutoRegisterStatus.OTHER_FAIL_STATUS);
			validationResult.setErrMsg("数据解密错误");
		}else{
			validationResult.setSuccess(true);
		}
		
		return validationResult;
	}
	
	public String generatePassword(){
		Random random = new Random();
		int pwd = random.nextInt(899999);
		pwd = pwd + 100000;
		return String.valueOf(pwd);
	}
	
	public String genToken(){
		 UUID uuid = UUID.randomUUID();
		 String token = this.cryption.encrypt(uuid.toString());
		 token = token.replaceAll("-", "");
		 return token;
	}
	
	public Result autoRegisterValidate(String phoneNum,  String serialNum, String fcode, String t, String uid, String sign){
		Result phoneValidateResult =  this.validateRequestData(phoneNum);
		if(!phoneValidateResult.isSuccess){
			return phoneValidateResult;
		}
		
		Result tValidateResult =  this.validateRequestData(t);
		if(!tValidateResult.isSuccess){
			return tValidateResult;
		}
		
		Result uidValidateResult =  this.validateRequestData(uid);
		if(!uidValidateResult.isSuccess){
			return uidValidateResult;
		}
		
		Result signValidateResult =  this.validateRegisterSign(sign, phoneNum, fcode, t);
		if(!signValidateResult.isSuccess){
			return signValidateResult;
		}
		
		return null;
	}
	
	/**
	 * @param fcode
	 * @param phoneNum
	 * @param uid
	 * @param serialNum
	 * @return 字段注册，创建一个新用户
	 */
	public Result autoRegister(String phoneNum,  String serialNum, String fcode, String t, String uid, String sign){
		Result result = new Result();
		try{
			Result validateResult =  this.autoRegisterValidate(phoneNum, serialNum, fcode, t, uid, sign);
			if(validateResult != null){
				return validateResult;
			}
			
		// TODO 需要按照平台自己规则，增加用户已经存在逻辑
//		if(user already exists){
//			validationResult.setSuccess(false);
//			validationResult.setStatus(Constants.AutoRegisterStatus.USER_ALREADY_EXIST);
//			validationResult.setPhoneNum(this.cryption.encrypt(phoneNum));
//			validationResult.setSerialNum(serialNum);
//			validationResult.setErrMsg("注册失败，该用户已存在，不可重复注册");
//		}
		String password = this.generatePassword();
		
		// TODO 需要按照平台自己规则，创建新用户
		// this.registerUser(phoneNum, password)
		
		String registerToken = this.genToken();
		
		// TODO 需要异步发送短信告知用户密码
		//this.sendSms(phoneNum, password)
		
		result.setSuccess(true);
		result.setStatus(Constants.AutoRegisterStatus.REGISTER_SUCCESS);
		result.setPhoneNum(this.cryption.encrypt(phoneNum));
		// TODO 需要将注册后的用户名返回回来
		result.setUserName(this.cryption.encrypt("userName"));
		result.setSerialNum(serialNum);
		result.setRegisterToken(registerToken);
		result.setErrMsg("注册成功");
		
		}catch(Exception e){
			e.printStackTrace();
			
			result.setSuccess(false);
	    	result.setStatus(Constants.AutoRegisterStatus.OTHER_FAIL_STATUS);
	    	result.setPhoneNum(this.cryption.encrypt(phoneNum));
	    	result.setSerialNum(serialNum);
			result.setErrMsg("注册失败，系统错误");
		}
		
		return result;
		
	}
	
	public Result registerQuery(String phoneNum, String serialNum, String fcode, String t, String uid, String sign){
		Result result = new Result();
		try {
			Result validateResult =  this.autoRegisterValidate(phoneNum, serialNum, fcode, t, uid, sign);
			if(validateResult != null){
				return validateResult;
			}
			
			// TODO 需要根据平台自己规则，查询用户注册状态
//			if("新用户，未注册"){
//				validationResult.setSuccess(true);
//		    	validationResult.setStatus(Constants.RegisterQueryStatus.NOT_REGISTER);
//		    	validationResult.setPhoneNum(this.cryption.encrypt(phoneNum));
//		    	validationResult.setSerialNum(serialNum);
//				validationResult.setErrMsg("新用户，未注册");
//			}else if("老用户，已注册，非渠道用户，但未绑定渠道账户"){
//				validationResult.setSuccess(true);
//		    	validationResult.setStatus(Constants.RegisterQueryStatus.NOT_FANLITOU_USER);
//		    	validationResult.setPhoneNum(this.cryption.encrypt(phoneNum));
//		    	validationResult.setSerialNum(serialNum);
//				validationResult.setErrMsg("老用户，已注册，非渠道用户，但未绑定渠道账户");
//			}else if("老用户绑定"){
//				validationResult.setSuccess(true);
//		    	validationResult.setStatus(Constants.RegisterQueryStatus.OLD_ACCOUNT_BIND);
//		    	validationResult.setPhoneNum(this.cryption.encrypt(phoneNum));
//		    	// TODO 需要将注册后的用户名返回回来
//				validationResult.setUserName(this.cryption.encrypt("userName"));
//				// TODO 需要将register token设置上
//				validationResult.setRegisterToken("registerToken");
//		    	validationResult.setSerialNum(serialNum);
//				validationResult.setErrMsg("老用户绑定");
//			}else if("老用户，已注册，渠道用户"){
//				validationResult.setSuccess(true);
//		    	validationResult.setStatus(Constants.RegisterQueryStatus.FANLITOU_USER);
//		    	validationResult.setPhoneNum(this.cryption.encrypt(phoneNum));
//		    	// TODO 需要将注册后的用户名返回回来
//				validationResult.setUserName(this.cryption.encrypt("userName"));
//				// TODO 需要将register token设置上
//				validationResult.setRegisterToken("registerToken");
//		    	validationResult.setSerialNum(serialNum);
//				validationResult.setErrMsg("老用户，已注册，渠道用户");
//			}else if("老用户，已注册，其他渠道用户"){
//				validationResult.setSuccess(true);
//		    	validationResult.setStatus(Constants.RegisterQueryStatus.OTHER_CHANNEL_USER);
//		    	validationResult.setPhoneNum(this.cryption.encrypt(phoneNum));
//		    	validationResult.setSerialNum(serialNum);
//				validationResult.setErrMsg("老用户，已注册，其他渠道用户");
//			}
		} catch (Exception e) {
			e.printStackTrace();
			
			result.setSuccess(false);
	    	result.setStatus(Constants.AutoRegisterStatus.OTHER_FAIL_STATUS);
	    	result.setPhoneNum(this.cryption.encrypt(phoneNum));
	    	result.setSerialNum(serialNum);
			result.setErrMsg("注册查询失败，系统错误");
		}
		
		return result;
	}
	
	/**
	 * @param phoneNum
	 * @param fcode
	 * @param registertoken
	 * @param uid
	 * @return 获取用户自动登录token
	 */
	public Result getUserLoginToken(String phoneNum, String fcode, String registertoken, String uid, String t, String serialNum, String sign){
		Result result = new Result();
		try{
			Result validateResult =  this.autoRegisterValidate(phoneNum, serialNum, fcode, t, uid, sign);
			if(validateResult != null){
				return validateResult;
			}
			
//			//TODO: 需要平台实现判断新用户，未注册逻辑
//			if ("新用户，未注册"){
//				validationResult.setSuccess(false);
//				validationResult.setStatus(Constants.RegisterQueryStatus.NOT_REGISTER);
//				validationResult.setLoginToken("");
//				validationResult.setErrMsg("新用户，未注册");
//                return validationResult;
//			}
			//TODO: 需要平台实现判断老用户，已注册，其他渠道用户逻辑
            if ("老用户，已注册，其他渠道用户" != null){
            	result.setSuccess(false);
				result.setStatus(Constants.RegisterQueryStatus.NOT_FANLITOU_USER);
				result.setLoginToken("");
				result.setErrMsg("老用户，已注册，其他渠道用户");
	            return result;
            }
            //TODO: 需要平台实现根据phoneNum获得平台register token逻辑
            if (!StringUtils.equals(registertoken, "register token")){
            	result.setSuccess(false);
				result.setStatus(Constants.AutoRegisterStatus.OTHER_FAIL_STATUS);
				result.setLoginToken("");
				result.setErrMsg("register token错误");
	            return result;
            }
            
            String loginToken = this.genToken();
            result.setSuccess(true);
			result.setStatus(Constants.AutoRegisterStatus.GET_TOKEN_SUCCESS);
			result.setLoginToken(loginToken);
			result.setErrMsg("获得login token成功");
            return result;
			
		} catch (Exception e) {
			e.printStackTrace();
			
			result.setSuccess(false);
	    	result.setStatus(Constants.AutoRegisterStatus.OTHER_FAIL_STATUS);
	    	result.setLoginToken("");
			result.setErrMsg("获得login token失败，系统错误");
			return result;
		}
		
	}
	
	/**
	 * @param phoneNum
	 * @param fcode
	 * @param registerToken
	 * @param loginToken
	 * @param timeStamp
	 * @param bidUrl
	 * @param source
	 * @return 用户自动登录
	 */
	public void doAutoLogin(String phoneNum, String fcode, String registerToken, String loginToken, String timeStamp, String bidUrl, String source, String sign){
		try {
			Result phoneValidateResult =  this.validateRequestData(phoneNum);
			if(!phoneValidateResult.isSuccess){
				//TODO: 跳转到bidUrl指定的页面
			}
			
			Result tValidateResult =  this.validateRequestData(timeStamp);
			if(!tValidateResult.isSuccess){
				//TODO: 跳转到bidUrl指定的页面
			}
			
			Result signValidateResult =  this.validateRegisterSign(sign, phoneNum, fcode, timeStamp);
			if(!signValidateResult.isSuccess){
				//TODO: 跳转到bidUrl指定的页面
			}
			
//			User user = queryUser(phoneNum);
//			if (user == null){
//				System.out.println("用户不存在");
//				//TODO: 跳转到bidUrl指定的页面
//			}
//			LoginToken platLoginToken = queryLoginToken(fcode, phoneNum, loginToken, registerToken);
//			if(!StringUtils.equals(loginToken, platLoginToken.getLoginToken())){
//				System.out.println("loginToken错误");
//				//TODO: 跳转到bidUrl指定的页面
//			}
//			
//			Date requestTime = Utils.stampToDate(timeStamp);
//			Date loginTokenCreateTime = platLoginToken.getCreateDate();
//			
//			Calendar calendar=Calendar.getInstance();   
//		    calendar.setTime(requestTime); 
//		    calendar.add(Calendar.MINUTE, 10);
//		    Date requestTimeAfter10Minute = calendar.getTime();
//		    
//		    if(requestTimeAfter10Minute.after(loginTokenCreateTime)){
//		    	System.out.println("login token已过期");
//		    	//TODO: 跳转到bidUrl指定的页面
//		    }
//		    
//		    if ("user is not login or login user is not current user"){
//		    	if(StringUtils.equals(source, "wap")){
////		    		do login for user in wap
//		    	}else{
////		    		do login for user in pc
//		    	}
//		    }
//		    
//		  //TODO: 跳转到bidUrl指定的页面
//			
//			
		} catch (Exception e) {
			e.printStackTrace();
			
			//TODO: 跳转到bidUrl指定的页面
		}
	}
	
	/**
	 * @param phoneNum
	 * @param fcode
	 * @param registerToken
	 * @param loginToken
	 * @param timeStamp
	 * @param bidUrl
	 * @param source
	 * @return 用户自动登录
	 */
	public void doUserBind(String phoneNum, String fcode, String registerToken, String loginToken, String timeStamp, String bidUrl, String source, String sign){
		try {
			Result phoneValidateResult =  this.validateRequestData(phoneNum);
			if(!phoneValidateResult.isSuccess){
				//TODO: 跳转到bidUrl指定的页面
			}
			
			Result tValidateResult =  this.validateRequestData(timeStamp);
			if(!tValidateResult.isSuccess){
				//TODO: 跳转到bidUrl指定的页面
			}
			
			Result signValidateResult =  this.validateRegisterSign(sign, phoneNum, fcode, timeStamp);
			if(!signValidateResult.isSuccess){
				//TODO: 跳转到bidUrl指定的页面
			}
			
	    	if(StringUtils.equals(source, "wap")){
	    		//TODO: 跳转到wap账号绑定登录页面，用户登录成功之后，为用户做账号绑定
			}else{
				//TODO: 跳转到pc账号绑定登录页面，用户登录成功之后，为用户做账号绑定
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
			//TODO: 跳转到bidUrl指定的页面
		}
	}
	
	/**
	 * @param phoneNum
	 * @param status
	 * @param userName
	 * @return 账户绑定通知
	 */
	public String userBindNotify(String phoneNum, String status, String userName){
		// TODO: 需要平台实现查询该绑定用户的register token逻辑
//		String registerToken = getRegisterToken(phoneNum, this.fcode);
		String registerToken = "";
		// TODO: 需要平台实现查询该绑定用户在绑定之前是否有过投资的逻辑
		boolean is_already_invest_before_user_bind = false;
		String timeStamp = String.valueOf(Utils.getTimeStampInt());
		JSONObject obj = new JSONObject(); 
        obj.put("phone_num", this.cryption.encrypt(phoneNum)); 
        obj.put("status", status);
        obj.put("t", this.cryption.encrypt(timeStamp));
        obj.put("user_name", this.cryption.encrypt(userName));
        obj.put("sign", this.sign(phoneNum, userName, timeStamp));
        obj.put("serial_num", "");
        obj.put("register_token", registerToken);
        obj.put("is_already_invest_before_user_bind", is_already_invest_before_user_bind);
        obj.put("err_msg", "");
        
        String param = obj.toJSONString();
        
		String result = Utils.sendPost(this.notifyUrlTest, param, "application/json");
		
		return result;
	}
	
	/**
	 * @param sign
	 * @param fcode
	 * @param timeStamp
	 * @param startTime
	 * @param endTime
	 * @return 投资记录查询
	 */
	public String getInvestRecord(String sign, String fcode, String timeStamp, String startTime, String endTime){
		JSONObject obj = new JSONObject();
		Result signValidationResult = this.validateInvestRecordSign(sign, fcode, timeStamp);
		if(!signValidationResult.isSuccess){
			obj.put("success", false);
			obj.put("message", signValidationResult.getErrMsg());
			return obj.toJSONString();
		}
		Result dateValidationResult = this.validateStartEndTime(startTime, endTime);
		if(!dateValidationResult.isSuccess){
			obj.put("success", false);
			obj.put("message", dateValidationResult.getErrMsg());
			return obj.toJSONString();
		}
		startTime = startTime + " 00:00:00";
		endTime = endTime + " 23:59:59";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			Date startDate = sdf.parse(startTime);
			Date endDate = sdf.parse(endTime);
			//TODO: 需要平台实现根据startDate，endDate查询该范围内fcode对应的投资记录
//			List records = this.queryInvestRecords(startDate, endDate);
			List investRecords = new ArrayList();
			
			HashMap rd = new HashMap();
			rd.put("phoneNum", "13012345678");
			rd.put("bidId", "1");
			rd.put("bidName", "test");
			rd.put("bidStatus", "还款中");
			rd.put("isFirstInvest", true);
			rd.put("investAmount", 10000);
			rd.put("investTime", "2015-10-10 01:02:03");
			rd.put("isAdvancedRepay", false); //是否提前还款
			rd.put("advancedRepayDate", ""); //提前还款时间
			rd.put("isAssign", false); //是否债权转让
			rd.put("assignDate", ""); //债权转让时间
			
			investRecords.add(rd);
			
			obj.put("success", true);
			obj.put("orders", investRecords);
			
			return obj.toJSONString();
			
		} catch (ParseException e) {
			e.printStackTrace();
			
			obj.put("success", false);
			obj.put("message", "获取投资记录失败");
			return obj.toJSONString();
		}
	}
	
	
}
















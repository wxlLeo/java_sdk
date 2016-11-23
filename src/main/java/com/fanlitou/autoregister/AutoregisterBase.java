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
		this.fcode = "fanlitou";
		this.secretKey = "1234567890123456";
		this.cryptionKey = "1234567890123456";  //初始化密钥，长度必须为16位
		
		this.userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/534.30 (KHTML, like Gecko) Ubuntu/11.04 Chromium/12.0.742.112 Chrome/12.0.742.112 Safari/534.30";
		this.contentType = "application/x-www-form-urlencoded; charset=UTF-8";
		cryption = new AES(this.cryptionKey);
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
				validationResult.setMsg("请求时间跨度超过30天");
		    }else{
		    	validationResult.setSuccess(true);
		    }
		} catch (ParseException e) {
			e.printStackTrace();
			validationResult.setSuccess(false);
			validationResult.setStatus(Constants.AutoRegisterStatus.NOT_PASS_VALIDATION);
			validationResult.setMsg("查询日期格式错误");
		}
		return validationResult;
	}
	
	/**
	 * @param data
	 * @return 验证数据解密是否正确
	 */
	public Result validateRequestData(String ...params){
		Result validationResult = new Result();
		for(String param: params){
			String decryptStr = this.cryption.decrypt(param);
			
			if(StringUtils.isEmpty(decryptStr)){
				validationResult.setSuccess(false);
				validationResult.setStatus(this.cryption.encrypt(Constants.AutoRegisterStatus.OTHER_FAIL_STATUS));
				validationResult.setMsg(this.cryption.encrypt("数据解密错误"));
			}
		}
		validationResult.setSuccess(true);
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
		 String token = uuid.toString().replaceAll("-", "");
		 return token;
	}
	
	public Result doValidate(String sign, String t, String ...params){
		Result phoneValidateResult =  this.validateRequestData(params);
		if(!phoneValidateResult.isSuccess){
			return phoneValidateResult;
		}
		
		Result signValidateResult =  this.validateSign(sign, t);
		if(!signValidateResult.isSuccess){
			return signValidateResult;
		}
		
		Result result = new Result();
		result.setSuccess(true);
		return result;
	}
	
	/**
	 * @param fcode
	 * @param phoneNum
	 * @param uid
	 * @param serialNum
	 * @return 字段注册，创建一个新用户
	 */
	public Result autoRegister(String phoneNum, String fcode, String t, String uid, String sign){
		Result result = new Result();
		try{
			Result validateResult =  this.doValidate(sign, t, phoneNum, fcode, t, uid, sign);
			if(validateResult != null && !validateResult.isSuccess){
				return validateResult;
			}
			
			String phoneNumDecrypt = this.cryption.decrypt(phoneNum);
			
		// TODO 需要按照平台自己规则，增加用户已经存在逻辑
//		if(user already exists){
//			result.setSuccess(false);
//			result.setStatus(this.cryption.encrypt(Constants.AutoRegisterStatus.USER_ALREADY_EXIST));
//			result.setMsg(this.cryption.encrypt("注册失败，该用户已存在，不可重复注册"));
//		}
		String password = this.generatePassword();
		
		// TODO 需要按照平台自己规则，创建新用户
		// this.registerUser(phoneNumDecrypt, password)
		
		String registerToken = this.genToken();
		
		// TODO 需要异步发送短信告知用户密码
		//this.sendSms(phoneNumDecrypt, password)
		
		result.setSuccess(true);
		result.setStatus(this.cryption.encrypt(Constants.AutoRegisterStatus.REGISTER_SUCCESS));
		// TODO 需要将注册后的用户名返回回来
		result.setUser_name(this.cryption.encrypt("userName"));
		result.setRegister_token(this.cryption.encrypt(registerToken));
		result.setMsg(this.cryption.encrypt("注册成功"));
		
		}catch(Exception e){
			e.printStackTrace();
			
			result.setSuccess(false);
	    	result.setStatus(this.cryption.encrypt(Constants.AutoRegisterStatus.OTHER_FAIL_STATUS));
			result.setMsg(this.cryption.encrypt("注册失败，系统错误"));
		}
		
		return result;
		
	}
	
	public Result registerQuery(String uid, String fcode, String t, String sign){
		Result result = new Result();
		try {
			Result validateResult =  this.doValidate(sign, t, uid, fcode, t, sign);
			if(validateResult != null && !validateResult.isSuccess){
				return validateResult;
			}
			
			// TODO 需要根据平台自己规则，查询用户注册状态
//			if("新用户，未注册"){
//				result.setSuccess(true);
//		    	result.setStatus(this.cryption.encrypt(Constants.RegisterQueryStatus.NOT_REGISTER));
//		    	result.setRegister_token(this.cryption.encrypt(""));
//		    	result.setUser_name(this.cryption.encrypt(""));
//				result.setMsg(this.cryption.encrypt("新用户，未注册"));
//				
//			}else if("老用户，已注册，非渠道用户，但未绑定渠道账户"){
//				result.setSuccess(true);
//		    	result.setStatus(this.cryption.encrypt(Constants.RegisterQueryStatus.NOT_FANLITOU_USER));
//		    	result.setRegister_token(this.cryption.encrypt(""));
//		    	result.setUser_name(this.cryption.encrypt(""));
//				result.setMsg("老用户，已注册，非渠道用户，但未绑定渠道账户");
//			}else if("老用户绑定"){
//				result.setSuccess(true);
//		    	result.setStatus(this.cryption.encrypt(Constants.RegisterQueryStatus.OLD_ACCOUNT_BIND));
//		    	// TODO 需要将注册后的用户名返回回来
//				result.setUser_name(this.cryption.encrypt("userName"));
//				// TODO 需要将register token设置上
//				result.setRegister_token(this.cryption.encrypt("registerToken"));
//				result.setMsg(this.cryption.encrypt("老用户绑定"));
//			}else if("老用户，已注册，返利投渠道用户"){
//				result.setSuccess(true);
//		    	result.setStatus(this.cryption.encrypt(Constants.RegisterQueryStatus.FANLITOU_USER));
//		    	// TODO 需要将注册后的用户名返回回来
//				result.setUser_name(this.cryption.encrypt("userName"));
//				// TODO 需要将register token设置上
//				result.setRegister_token(this.cryption.encrypt("registerToken"));
//				result.setMsg("老用户，已注册，渠道用户");
//			}else if("老用户，已注册，其他渠道用户"){
//				result.setSuccess(true);
//		    	result.setStatus(this.cryption.encrypt(Constants.RegisterQueryStatus.OTHER_CHANNEL_USER));
//		    	result.setRegister_token(this.cryption.encrypt(""));
//		    	result.setUser_name(this.cryption.encrypt(""));
//				result.setMsg(this.cryption.encrypt("老用户，已注册，其他渠道用户"));
//			}
			
			result.setSuccess(true);
	    	result.setStatus(this.cryption.encrypt(Constants.RegisterQueryStatus.FANLITOU_USER));
	    	// TODO 需要将注册后的用户名返回回来
			result.setUser_name(this.cryption.encrypt("userName"));
			// TODO 需要将register token设置上
			result.setRegister_token(this.cryption.encrypt("registerToken"));
			result.setMsg("老用户，已绑定返利投渠道用户");
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
	    	result.setStatus(this.cryption.encrypt(Constants.AutoRegisterStatus.OTHER_FAIL_STATUS));
			result.setMsg(this.cryption.encrypt("注册查询失败，系统错误"));
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
	public Result getUserLoginToken(String fcode, String registertoken, String uid, String t, String sign){
		Result result = new Result();
		try{
			Result validateResult =  this.doValidate(sign, t, fcode, t, uid, sign);
			if(validateResult != null && !validateResult.isSuccess){
				return validateResult;
			}
			
			//TODO: 需要平台实现判断新用户，未注册逻辑
//			if ("新用户，未注册"){
//				result.setSuccess(false);
//				result.setStatus(this.cryption.encrypt(Constants.RegisterQueryStatus.NOT_REGISTER));
//				result.setLogin_token(this.cryption.encrypt(""));
//				result.setMsg(this.cryption.encrypt("新用户，未注册"));
//                return result;
//			}
//			TODO: 需要平台实现判断老用户，已注册，其他渠道用户逻辑
//            if ("老用户，已注册，其他渠道用户" != null){
//            	result.setSuccess(false);
//				result.setStatus(this.cryption.encrypt(Constants.RegisterQueryStatus.NOT_FANLITOU_USER));
//				result.setLogin_token(this.cryption.encrypt(""));
//				result.setMsg(this.cryption.encrypt("老用户，已注册，其他渠道用户"));
//	            return result;
//            }
//            //TODO: 需要平台实现根据phoneNum获得平台register token逻辑
//            if (!StringUtils.equals(registertoken, "register token")){
//            	result.setSuccess(false);
//				result.setStatus(this.cryption.encrypt(Constants.AutoRegisterStatus.OTHER_FAIL_STATUS));
//				result.setLogin_token(this.cryption.encrypt(""));
//				result.setMsg(this.cryption.encrypt("register token错误"));
//	            return result;
//            }
            
            String loginToken = this.genToken();
            result.setSuccess(true);
			result.setStatus(this.cryption.encrypt(Constants.AutoRegisterStatus.GET_TOKEN_SUCCESS));
			result.setLogin_token(this.cryption.encrypt(loginToken));
			result.setMsg(this.cryption.encrypt("获得login token成功"));
            return result;
			
		} catch (Exception e) {
			e.printStackTrace();
			
			result.setSuccess(false);
	    	result.setStatus(this.cryption.encrypt(Constants.AutoRegisterStatus.OTHER_FAIL_STATUS));
	    	result.setLogin_token(this.cryption.encrypt(""));
			result.setMsg(this.cryption.encrypt("获得login token失败，系统错误"));
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
	public void doAutoLogin(String uid, String fcode, String registerToken, String loginToken, String timeStamp, String bidUrl, String source, String sign){
		try {
			Result validateResult =  this.doValidate(sign, timeStamp, uid, fcode, registerToken, loginToken, timeStamp, source, sign);
			if(validateResult != null && !validateResult.isSuccess){
				System.out.println("not pass validation");
				//TODO: 跳转到bidUrl指定的页面
			}
			
			String uidDecrypt = this.cryption.decrypt(uid);
			String timeStampDecrypt = this.cryption.decrypt(timeStamp);
			
//			User user = queryUser(uidDecrypt);
//			if (user == null){
//				System.out.println("用户不存在");
//				//TODO: 跳转到bidUrl指定的页面
//			}
//			LoginToken platLoginToken = queryLoginToken(fcode, uidDecrypt, loginToken, registerToken);
//			if(!StringUtils.equals(loginToken, platLoginToken.getLoginToken())){
//				System.out.println("loginToken错误");
//				//TODO: 跳转到bidUrl指定的页面
//			}
//			
//			Date requestTime = Utils.stampToDate(timeStampDecrypt);
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
		    
		  //TODO: 跳转到bidUrl指定的页面
			
			System.out.println("Login success");
			
			
		} catch (Exception e) {
			e.printStackTrace();
			
			//TODO: 跳转到bidUrl指定的页面
		}
	}
	
	
	/**
	 * @param uid
	 * @param fcode
	 * @param timeStamp
	 * @param bidUrl
	 * @param source
	 * @param sign
	 * 老账户绑定
	 */
	public void doUserBind(String uid, String fcode, String timeStamp, String bidUrl, String source, String sign){
		try {
			Result validateResult =  this.doValidate(sign, timeStamp, uid, fcode, timeStamp, source, sign);
			if(validateResult != null && !validateResult.isSuccess){
				System.out.println("not pass validation");
				//TODO: 跳转到bidUrl指定的页面
			}
			String sourceDecrypt = this.cryption.decrypt(source);
			
	    	if(StringUtils.equals(sourceDecrypt, "wap")){
	    		//TODO: 跳转到wap账号绑定登录页面，用户登录成功之后，为用户做账号绑定
			}else{
				//TODO: 跳转到pc账号绑定登录页面，用户登录成功之后，为用户做账号绑定
			}
	    	
	    	System.out.println("Bind success");
			
		} catch (Exception e) {
			e.printStackTrace();
			
			//TODO: 跳转到bidUrl指定的页面
		}
	}
	

	
	/**
	 * @param sign
	 * @param timeStamp
	 * @param startTime
	 * @param endTime
	 * @return 投资记录查询
	 */
	public String getInvestRecord(String sign, String timeStamp, String startTime, String endTime){
		JSONObject obj = new JSONObject();
		Result signValidationResult = this.validateNotEncryptSign(sign, timeStamp);
		if(!signValidationResult.isSuccess){
			obj.put("success", false);
			obj.put("message", signValidationResult.getMsg());
			obj.put("orders", new ArrayList());
			return obj.toJSONString();
		}
		Result dateValidationResult = this.validateStartEndTime(startTime, endTime);
		if(!dateValidationResult.isSuccess){
			obj.put("success", false);
			obj.put("message", dateValidationResult.getMsg());
			obj.put("orders", new ArrayList());
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
			rd.put("uid", "H31f70274e");
			rd.put("phoneNum", "13012345678");
			rd.put("bidId", "1");
			rd.put("bidName", "test");
			rd.put("bidStatus", 2);
			rd.put("isFirstInvest", true);
			rd.put("investAmount", 10000);
			rd.put("investTime", "2015-10-10 01:02:03");
			rd.put("isAdvancedRepay", false); //是否提前还款
			rd.put("advancedRepayDate", ""); //提前还款时间
			rd.put("isAssign", false); //是否债权转让
			rd.put("isFullAssign", false); //是否债权转让
			rd.put("assignAmount", 0); //是否债权转让
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
	
	/**
	 * @param sign
	 * @param timeStamp
	 * @return 验证sign
	 */
	public Result validateSign(String sign, String timeStamp){
		String timeStampDecrypt = this.cryption.decrypt(timeStamp);
		String signStr = timeStampDecrypt + this.secretKey;
		Result validationResult = new Result();
		String mySign = MD5.GetMD5Code(signStr);
		String signDecrypt = this.cryption.decrypt(sign);
		if(StringUtils.equals(mySign, signDecrypt)){
			validationResult.setSuccess(true);
		}else{
			validationResult.setSuccess(false);
			validationResult.setStatus(this.cryption.encrypt(Constants.AutoRegisterStatus.NOT_PASS_VALIDATION));
			validationResult.setMsg(this.cryption.encrypt("未通过安全校验"));
		}
		return validationResult;
	}
	
	/**
	 * @param sign
	 * @param timeStamp
	 * @return 验证投资记录sign
	 */
	public Result validateNotEncryptSign(String sign, String timeStamp){
		String signStr = timeStamp + this.secretKey;
		Result validationResult = new Result();
		String mySign = MD5.GetMD5Code(signStr);
		if(StringUtils.equals(mySign, sign)){
			validationResult.setSuccess(true);
		}else{
			validationResult.setSuccess(false);
			validationResult.setMsg("未通过安全校验");
		}
		return validationResult;
	}
	
	
	/**
	 * @param pageCount
	 * @param pageIndex
	 * @param timeStamp
	 * @param sign
	 * @return
	 */
	public String getBidList(String pageCount, String pageIndex, String timeStamp, String sign){
		JSONObject obj = new JSONObject();
		Result signValidationResult = this.validateNotEncryptSign(sign, timeStamp);
		if(!signValidationResult.isSuccess){
			obj.put("success", false);
			obj.put("message", signValidationResult.getMsg());
			obj.put("totalCount", 0);  //总记录数
			obj.put("bidList", new ArrayList());
			return obj.toJSONString();
		}
		
		
		try {
			List bidList = new ArrayList();
			
			HashMap rd1 = new HashMap();
			rd1.put("bidId", "123");
			rd1.put("status", 1);
			rd1.put("name", "新手标");
			rd1.put("minInvestAmount", 100);
			rd1.put("introduction", "产品描述");
			rd1.put("pcUrl", "https://www.xxdai.com/123.html");
			rd1.put("mobileUrl", "https://m.xxdai.com/123.html");
			rd1.put("totalAmount", 500000);
			rd1.put("remainAmount", 10000);
			rd1.put("duration", 3);
			rd1.put("durationUnit", 30);
			rd1.put("isNewUser", true);
			rd1.put("repaymentType", 1);
			rd1.put("isGroup", false);
			rd1.put("interestRate", 10);
			rd1.put("awardInterestRate", 1);
			rd1.put("interestStartDate", "2016-10-10");
			rd1.put("repaymentDate", "2017-01-10");
			rd1.put("payInterestDay", "");
			
			bidList.add(rd1);
			
			HashMap rd2 = new HashMap();
			rd2.put("bidId", "124");
			rd2.put("status", 1);
			rd2.put("name", "普通标");
			rd2.put("minInvestAmount", 100);
			rd2.put("introduction", "产品描述");
			rd2.put("pcUrl", "https://www.xxdai.com/124.html");
			rd2.put("mobileUrl", "https://m.xxdai.com/124.html");
			rd2.put("totalAmount", 3000000);
			rd2.put("remainAmount", 200000);
			rd2.put("duration", 1);
			rd2.put("durationUnit", 360);
			rd2.put("isNewUser", false);
			rd2.put("repaymentType", 1);
			rd2.put("isGroup", false);
			rd2.put("interestRate", 12);
			rd2.put("awardInterestRate", 0);
			rd2.put("interestStartDate", "2016-10-10");
			rd2.put("repaymentDate", "2017-10-10");
			rd2.put("payInterestDay", "");
			
			bidList.add(rd2);
			
			obj.put("success", true);
			obj.put("message", "获取可投资标列表成功");
			obj.put("totalCount", 10);  //总记录数
			obj.put("bidList", bidList);
			
			return obj.toJSONString();
			
		} catch (Exception e) {
			e.printStackTrace();
			
			obj.put("success", false);
			obj.put("message", "获取投资记录失败");
			obj.put("totalCount", 0);  //总记录数
			obj.put("bidList", new ArrayList());
			return obj.toJSONString();
		}
	}
	
	
}
















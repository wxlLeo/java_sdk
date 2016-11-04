package com.fanlitou.controler;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fanlitou.autoregister.AutoregisterBase;
import com.fanlitou.pojo.Result;


@RestController
public class FanlitouController {

	/**
	 * @param param
	 * @return 注册用户
	 */
	@RequestMapping(value="/flt/register",method=RequestMethod.POST)
    @ResponseBody
    public Result register(@RequestBody Param param){
		String fcode = param.getFcode();
		String phoneNum = param.getPhone_num();
		String t = param.getT();
		String serialNum = param.getSerial_num();
		String uid = param.getUid();
		String sign = param.getSign();
		
		System.out.println(param.toString());
		
		AutoregisterBase autoregister = new AutoregisterBase();
		
		Result result = autoregister.autoRegister(phoneNum, serialNum, fcode, t, uid, sign);
		
		return result;
	}
	
	/**
	 * @param param
	 * @return 注册用户查询
	 */
	@RequestMapping(value="/flt/registerQuery",method=RequestMethod.POST)
    @ResponseBody
    public Result registerQuery(@RequestBody Param param){
		String fcode = param.getFcode();
		String phoneNum = param.getPhone_num();
		String t = param.getT();
		String serialNum = param.getSerial_num();
		String uid = param.getUid();
		String sign = param.getSign();
		
		System.out.println(param.toString());
		
		AutoregisterBase autoregister = new AutoregisterBase();
		
		Result result = autoregister.registerQuery(phoneNum, serialNum, fcode, t, uid, sign);
		
		return result;
	}
	
	/**
	 * @param param
	 * @return 获取用户自动登录token
	 */
	@RequestMapping(value="/flt/loginToken",method=RequestMethod.POST)
    @ResponseBody
    public Result loginToken(@RequestBody Param param){
		String fcode = param.getFcode();
		String phoneNum = param.getPhone_num();
		String t = param.getT();
		String serialNum = param.getSerial_num();
		String uid = param.getUid();
		String registertoken = param.getRegistertoken();
		String sign = param.getSign();
		
		System.out.println(param.toString());
		
		AutoregisterBase autoregister = new AutoregisterBase();
		
		Result result = autoregister.getUserLoginToken(phoneNum, fcode, registertoken, uid, t, serialNum, sign);
		
		return result;
	}
	
	/**
	 * @param param
	 * @return 用户自动登录
	 */
	@RequestMapping(value="/flt/autoLogin",method=RequestMethod.GET)
    @ResponseBody
    public void autoLogin(@RequestParam("fcode") String fcode, 
    		@RequestParam("phone_num") String phoneNum, 
    		@RequestParam("t") String t, 
    		@RequestParam("register_token") String registerToken, 
    		@RequestParam("login_token") String loginToken, 
    		@RequestParam("bid_url") String bidUrl, 
    		@RequestParam("source") String source, 
    		@RequestParam("sign") String sign){
		
		AutoregisterBase autoregister = new AutoregisterBase();
		
		autoregister.doAutoLogin(phoneNum, fcode, registerToken, loginToken, t, bidUrl, source, sign);
		
	}
	
	/**
	 * @param param
	 * @return 老账户绑定
	 */
	@RequestMapping(value="/flt/userBind",method=RequestMethod.GET)
    @ResponseBody
    public void userBind(@RequestParam("fcode") String fcode, 
    		@RequestParam("phone_num") String phoneNum, 
    		@RequestParam("t") String t, 
    		@RequestParam("bid_url") String bidUrl, 
    		@RequestParam("source") String source, 
    		@RequestParam("sign") String sign){
		
		AutoregisterBase autoregister = new AutoregisterBase();
		
		autoregister.doUserBind(phoneNum, fcode, t, bidUrl, source, sign);
		
	}
	
	/**
	 * @param param
	 * @return 查询用户投资记录
	 */
	@RequestMapping(value="/flt/investRecord",method=RequestMethod.GET)
    @ResponseBody
    public String investRecord(@RequestParam("fcode") String fcode, 
    		@RequestParam("t") String t, 
    		@RequestParam("start_time") String startTime, 
    		@RequestParam("end_time") String endTime, 
    		@RequestParam("sign") String sign){
		
		AutoregisterBase autoregister = new AutoregisterBase();
		
		return autoregister.getInvestRecord(sign, fcode, t, startTime, endTime);
		
	}
    
    
    
}

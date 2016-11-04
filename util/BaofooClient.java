package com.baofoo.sdk.util;

import com.baofoo.sdk.domain.RequestParams;
import com.baofoo.sdk.http.HttpMethod;
import com.baofoo.sdk.http.HttpSendModel;
import com.baofoo.sdk.http.SimpleHttpResponse;

/**
 * 项目名称：baofoo-fopay-sdk-java
 * 类名称：请求客户端
 * 类描述：
 * 创建人：陈少杰
 * 创建时间：2014-10-22 下午2:58:22
 * 修改人：陈少杰
 * 修改时间：2014-10-22 下午2:58:22
 * @version
 */
public class BaofooClient {

	/**
	 * 宝付接口请求
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static SimpleHttpResponse doRequest(RequestParams params) throws Exception {
		// 请求解密工具
		String postData = "member_id=%s&terminal_id=%s&data_type=%s&data_content=%s&version=%s&txn_type=%s&txn_sub_type=%s";
		postData = String.format(postData, params.getMemberId(),
				params.getTerminalId(), params.getDataType(), params.getDataContent(),params.getVersion(),params.getTxn_type(),
				params.getTxn_sub_type());
		HttpSendModel httpSendModel = new HttpSendModel(params.getRequestUrl() + "?" + postData);
		System.out.println("验证请求：" + params.getRequestUrl() + "?" + postData);
		httpSendModel.setMethod(HttpMethod.POST);
		return HttpUtil.doRequest(httpSendModel, "utf-8");
	}
}

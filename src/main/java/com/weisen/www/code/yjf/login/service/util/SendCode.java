package com.weisen.www.code.yjf.login.service.util;

import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

public final class SendCode {

	private static final String SIGNNAME = "园积分";

	private static final String AccessKey_ID = "LTAIO0A18h5ZjM6h";

	private static final String Access_Key_Secret = "IjcmZ4II6Dm9CQtDRlfqOyjnUOijco";
	// 模板:身份验证211，登录确认210，登录异常209，用户注册208，修改密码207，信息变更206
	private static final List<String> TEMPLATE = Arrays.asList("SMS_168096211", "SMS_168096210", "SMS_168096209",
			"SMS_168096208", "SMS_168096207", "SMS_168096206");

	private static Integer searchTemplate(String name) {
		if ("身份验证".equals(name)) {
			return 0;
		}else if ("登录确认".equals(name)) {
			return 1;
		}else if ("登录异常".equals(name)) {
			return 2;
		}else if ("用户注册".equals(name)) {
			return 3;
		}else if ("修改密码".equals(name)) {
			return 4;
		}else{
			return 5;
		}
	}
	
	public static String issue(String phone, String name, String key) {
		DefaultProfile profile = DefaultProfile.getProfile("default", AccessKey_ID, Access_Key_Secret);
		IAcsClient client = new DefaultAcsClient(profile);
		CommonRequest request = new CommonRequest();
		request.setMethod(MethodType.POST);
		request.setDomain("dysmsapi.aliyuncs.com");
		request.setVersion("2017-05-25");
		request.setAction("SendSms");
		request.putQueryParameter("PhoneNumbers", phone);
		request.putQueryParameter("SignName", SIGNNAME);
		request.putQueryParameter("TemplateCode", TEMPLATE.get(searchTemplate(name)));
		request.putQueryParameter("TemplateParam", "{\"code\":\"" + key + "\"}");
		try {
			CommonResponse response = client.getCommonResponse(request);
			JSONObject parse = (JSONObject) JSON.parse(response.getData());
			return parse.get("Message").toString().toLowerCase().trim().equals("ok")?"发送成功":"发送失败";
		} catch (ServerException e) {
			return "网络繁忙";
		} catch (ClientException e) {
			return "系统异常";
		}
	}
}

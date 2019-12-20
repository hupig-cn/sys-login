package com.weisen.www.code.yjf.login.service;

import com.weisen.www.code.yjf.login.service.util.Result;

/**
 * 注销账号
 *
 * @author LuoJinShui
 *
 */
public interface Rewrite_ActivateAccountService {

	// 查询账户状态
	Result getAccountStatus(String userPhone);

	// 注销账号
	Result CancellationAccount(Long userId);

	// 找回账号
	Result getActivateAccount(String userPhone, String vertifyCode);

}

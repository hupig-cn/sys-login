package com.weisen.www.code.yjf.login.web.rest;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.weisen.www.code.yjf.login.service.Rewrite_ActivateAccountService;
import com.weisen.www.code.yjf.login.service.util.Result;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 账号注销,账号激活,查询账号状态
 *
 * @author LuoJinShui
 * @date 2019-12-18 17:14:34
 *
 */
@RestController
@RequestMapping("/api")
@Api(tags = "002-激活账号")
public class Rewrite_CancellationAccountResource {

	private final Logger logger = LoggerFactory.getLogger(Rewrite_CancellationAccountResource.class);

	private final Rewrite_ActivateAccountService rewrite_ActivateAccountService;

	public Rewrite_CancellationAccountResource(Rewrite_ActivateAccountService rewrite_ActivateAccountService) {
		this.rewrite_ActivateAccountService = rewrite_ActivateAccountService;
	}

	/**
	 * 查询账号状态
	 * 
	 * @param userId
	 * @return
	 */
	@PostMapping(value = "/public/get/Account/Status")
	@ApiOperation(value = "查询账号状态")
	public ResponseEntity<Result> getAccountStatus(@RequestParam(value = "userPhone") String userPhone) {
		Result result = rewrite_ActivateAccountService.getAccountStatus(userPhone);
		logger.debug("访问成功:{},传入值:{},返回值:{}", "/get/Account/Status", userPhone, result);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
	}

	/**
	 * 注销账号
	 * 
	 * @param userId
	 * @return
	 */
	@PostMapping(value = "/Cancellation/Account")
	@ApiOperation(value = "注销账号")
	public ResponseEntity<Result> deleteCancellationAccount(@RequestParam(value = "userId") Long userId) {
		Result result = rewrite_ActivateAccountService.CancellationAccount(userId);
		logger.debug("访问成功:{},传入值:{},返回值:{}", "/Cancellation/Account", userId, result);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
	}

	/**
	 * 激活账号
	 * 
	 * @param userId
	 * @return
	 */
	@PostMapping(value = "/Activate/Account")
	@ApiOperation(value = "激活账号")
	public ResponseEntity<Result> getActivateAccount(@RequestParam(value = "userId") Long userId) {
		Result result = rewrite_ActivateAccountService.getActivateAccount(userId);
		logger.debug("访问成功:{},传入值:{},返回值:{}", "/Activate/Account", userId, result);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
	}

}

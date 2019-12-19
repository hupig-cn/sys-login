package com.weisen.www.code.yjf.login.service.impl;

import java.time.Instant;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.weisen.www.code.yjf.login.domain.User;
import com.weisen.www.code.yjf.login.repository.Rewrite_UserRepository;
import com.weisen.www.code.yjf.login.service.Rewrite_ActivateAccountService;
import com.weisen.www.code.yjf.login.service.util.Result;

@Service
@Transactional
public class Rewrite_ActivateAccountServiceImpl implements Rewrite_ActivateAccountService {

	private final Rewrite_UserRepository rewrite_UserRepository;

	public Rewrite_ActivateAccountServiceImpl(Rewrite_UserRepository rewrite_UserRepository) {
		this.rewrite_UserRepository = rewrite_UserRepository;
	}

	/**
	 * 查询账号状态
	 * 
	 * @author LuoJinShui
	 * @date 2019-12-18 17:25:40
	 */
	@Override
	public Result getAccountStatus(String userPhone) {
		User user = rewrite_UserRepository.finByLogin(userPhone);
		if (user == null) {
			return Result.fail("该用户不存在!请重新输入查找!");
		}
		// 状态为true正常,为false已注销
		boolean status = user.getActivated();
		if (user.getActivated() == false) {
			return Result.suc("查询成功!", user.getActivated());
		} else {
			return Result.suc("查询成功!", status);
		}
	}

	/**
	 * 注销账号
	 * 
	 * @author LuoJinShui
	 * @date 2019-12-18 17:25:40
	 */
	@Override
	public Result CancellationAccount(Long userId) {
		User user = rewrite_UserRepository.findUserById(userId);
		// 判断该用户是否存在
		if (user == null) {
			return Result.fail("该用户不存在!请重新输入查找!");
		}
		Instant now = Instant.now().plusMillis(TimeUnit.HOURS.toMillis(8));
		long nowTime = now.toEpochMilli();
		// 拿取用户注册账号时间
		long createdDate = user.getCreatedDate().toEpochMilli();
		// 拿取用户最后修改时间
		long lastModifiedDate = user.getLastModifiedDate().toEpochMilli();
		if (createdDate == lastModifiedDate) {
			// 修改账号状态
			user.setActivated(false);
			// 修改最后一次操作时间
			user.setLastModifiedDate(now);
			rewrite_UserRepository.saveAndFlush(user);
			return Result.suc("注销成功!七天后才可激活此账号!");
		}
		if ((nowTime - lastModifiedDate) < 2592000000L) {
			return Result.fail("不能频繁注销哦!");
		}
		if (user.getActivated() == false) {
			return Result.fail("该账号已被注销!不能重复注销哦!");
		} else {
			// 修改账号状态
			user.setActivated(false);
			// 修改最后一次操作时间
			user.setLastModifiedDate(now);
			rewrite_UserRepository.saveAndFlush(user);
			return Result.suc("注销成功!七天后才可激活此账号!");
		}
	}

	/**
	 * 激活账号
	 * 
	 * @author LuoJinShui
	 * @date 2019-12-18 17:25:40
	 */
	@Override
	public Result getActivateAccount(Long userId) {
		User user = rewrite_UserRepository.findUserById(userId);
		if (user == null) {
			return Result.fail("该用户不存在!请重新输入查找!");
		}
		// 获取当前时间日期--nowDate
		// 拿取用户最后注销时间
		long lastModifiedDate = user.getLastModifiedDate().toEpochMilli();
		Instant now = Instant.now();
		long nowTime = now.toEpochMilli();
		// 注销后七天才可激活
		if ((nowTime - lastModifiedDate) < 604800000L) {
			return Result.fail("还不能激活哦!");
		}
		if (user.getActivated() == true) {
			return Result.fail("该账号已激活!不能重复激活哦!");
		} else {
			user.setId(user.getId());
			user.setActivated(true);
			user.getCreatedDate();
			rewrite_UserRepository.saveAndFlush(user);
			return Result.suc("该账号已成功激活!可以去登录啦!");
		}
	}
}

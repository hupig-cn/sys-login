package com.weisen.www.code.yjf.login.service.impl;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.weisen.www.code.yjf.login.domain.SmsService;
import com.weisen.www.code.yjf.login.domain.User;
import com.weisen.www.code.yjf.login.repository.Rewrite_SmsServiceRepository;
import com.weisen.www.code.yjf.login.repository.Rewrite_UserRepository;
import com.weisen.www.code.yjf.login.service.Rewrite_ActivateAccountService;
import com.weisen.www.code.yjf.login.service.util.Result;

@Service
@Transactional
public class Rewrite_ActivateAccountServiceImpl implements Rewrite_ActivateAccountService {

	private final Logger log = LoggerFactory.getLogger(Rewrite_ActivateAccountServiceImpl.class);

	private final Rewrite_UserRepository rewrite_UserRepository;

	private final Rewrite_SmsServiceRepository rewrite_SmsServiceRepository;
	
	private final PasswordEncoder passwordEncoder;

	// 验证码过期时间
	private static final int DEF_OVERDUE = 60 * 5;

	// 30天的时间戳
	private static final long DAY_THIRTY_INSTANT = 2592000000L;

	// 7天的时间戳
	private static final long DAY_SEVEN_INSTANT = 604800000L;

	public Rewrite_ActivateAccountServiceImpl(Rewrite_UserRepository rewrite_UserRepository,
			Rewrite_SmsServiceRepository rewrite_SmsServiceRepository,PasswordEncoder passwordEncoder) {
		this.rewrite_UserRepository = rewrite_UserRepository;
		this.rewrite_SmsServiceRepository = rewrite_SmsServiceRepository;
		this.passwordEncoder = passwordEncoder;
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
		if (user.getActivated() == false) {
			return Result.fail("该账号已被注销!不能重复注销哦!");
		}
		if ((nowTime - lastModifiedDate) < DAY_THIRTY_INSTANT) {
			return Result.fail("30天内不能频繁注销哦!");
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
	public Result getActivateAccount(String userPhone, String vertifyCode) {
		User user = rewrite_UserRepository.finByLogin(userPhone);
		if (user == null) {
			return Result.fail("该用户不存在!请重新输入查找!");
		}
		// 获取当前时间日期--nowDate
		// 拿取用户最后注销时间
		long lastModifiedDate = user.getLastModifiedDate().toEpochMilli();
		Instant now = Instant.now().plusMillis(TimeUnit.HOURS.toMillis(8));
		long nowTime = now.toEpochMilli();
		// 注销后七天才可激活
		if ((nowTime - lastModifiedDate) < DAY_SEVEN_INSTANT) {
			return Result.fail("还不能激活哦!");
		}
		if (user.getActivated() == true) {
			return Result.fail("该账号已激活!不能重复激活哦!");
		}
		SmsService mSmsData = rewrite_SmsServiceRepository.findOneByPhoneAndType(userPhone, "找回账号");
		log.debug(new Gson().toJson(mSmsData));
		if (mSmsData == null) {
			return Result.fail("非法调用!");
		} else if ((System.currentTimeMillis() - mSmsData.getSendtime()) / 1000 > DEF_OVERDUE) {
			return Result.fail("验证码已失效!");
		} else if (!mSmsData.getCode().equals(vertifyCode)) {
			return Result.fail("验证码不正确!");
		} else {
			String encryptedPassword = passwordEncoder.encode("123456");
			user.setPassword(encryptedPassword);
			user.setActivated(true);
			rewrite_UserRepository.save(user);
//			rewrite_UserRepository.saveUser(userPhone);
			return Result.suc("该账号已成功激活!可以去登录啦!默认密码为123456");
		}
	}
}

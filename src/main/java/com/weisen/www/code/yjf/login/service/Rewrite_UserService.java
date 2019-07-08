package com.weisen.www.code.yjf.login.service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weisen.www.code.yjf.login.domain.Authority;
import com.weisen.www.code.yjf.login.domain.SmsService;
import com.weisen.www.code.yjf.login.domain.User;
import com.weisen.www.code.yjf.login.repository.AuthorityRepository;
import com.weisen.www.code.yjf.login.repository.Rewrite_SmsServiceRepository;
import com.weisen.www.code.yjf.login.repository.UserRepository;
import com.weisen.www.code.yjf.login.security.AuthoritiesConstants;
import com.weisen.www.code.yjf.login.security.SecurityUtils;
import com.weisen.www.code.yjf.login.service.dto.UserDTO;
import com.weisen.www.code.yjf.login.web.rest.errors.InvalidPasswordException;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class Rewrite_UserService {

	private final Logger log = LoggerFactory.getLogger(Rewrite_UserService.class);

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final AuthorityRepository authorityRepository;

	private final CacheManager cacheManager;

	private final Rewrite_SmsServiceRepository rewrite_SmsServiceRepository;
	// 验证码过期时间
	private static final int DEF_OVERDUE = 60 * 5;

	public Rewrite_UserService(Rewrite_SmsServiceRepository rewrite_SmsServiceRepository, UserRepository userRepository,
			PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository, CacheManager cacheManager) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authorityRepository = authorityRepository;
		this.cacheManager = cacheManager;
		this.rewrite_SmsServiceRepository = rewrite_SmsServiceRepository;
	}

	public String registerUser() {
		User newUser = new User();
		newUser.setLogin(RandomStringUtils.randomNumeric(20));
		// new user gets initially a generated password
		newUser.setPassword(passwordEncoder.encode(RandomStringUtils.randomNumeric(6)));
		newUser.setFirstName("Auto");
		newUser.setLastName("user");
		newUser.setEmail(RandomStringUtils.randomNumeric(6) + "@local");
		newUser.setActivated(true);
		Set<Authority> authorities = new HashSet<>();
		authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
		newUser.setAuthorities(authorities);
		userRepository.save(newUser);
		this.clearUserCaches(newUser);
		log.debug("Created Information for User: {}", newUser);
		return newUser.getId().toString();
	}

	public String registerUser(UserDTO userDTO, String password) {
		SmsService smsService = rewrite_SmsServiceRepository.findOneByPhone(userDTO.getLogin());
		if (null == smsService)
			return "请先发送验证码";
		if ((System.currentTimeMillis() - smsService.getSendtime()) / 1000 > DEF_OVERDUE)
			return "验证码已过期";
		if (!userDTO.getLastName().equals(smsService.getCode()))
			return "验证码错误";
		if (userRepository.findOneByLogin(userDTO.getLogin()).isPresent())
			return "用户已存在";
		User newUser = new User();
		String encryptedPassword = passwordEncoder.encode(password);
		newUser.setLogin(userDTO.getLogin().toLowerCase().trim());
		// new user gets initially a generated password
		newUser.setPassword(encryptedPassword);
		newUser.setFirstName(userDTO.getLogin());
		newUser.setEmail(RandomStringUtils.randomAlphanumeric(20) + "@local");
		newUser.setActivated(true);
		Set<Authority> authorities = new HashSet<>();
		authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
		newUser.setAuthorities(authorities);
		userRepository.save(newUser);
		this.clearUserCaches(newUser);
		log.debug("Created Information for User: {}", newUser);
		return newUser.getId().toString();
	}

	public void changePassword(String currentClearTextPassword, String newPassword) {
		SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).ifPresent(user -> {
			String currentEncryptedPassword = user.getPassword();
			if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
				throw new InvalidPasswordException();
			}
			String encryptedPassword = passwordEncoder.encode(newPassword);
			user.setPassword(encryptedPassword);
			this.clearUserCaches(user);
			log.debug("Changed password for User: {}", user);
		});
	}

	public void completePasswordReset(String password) {
		SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).ifPresent(user -> {
			String encryptedPassword = passwordEncoder.encode(password);
			user.setPassword(encryptedPassword);
			this.clearUserCaches(user);
			log.debug("Changed password for User: {}", user);
		});
	}

	/**
	 * Gets a list of all the authorities.
	 * 
	 * @return a list of all the authorities.
	 */
	public List<String> getAuthorities() {
		return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
	}

	private void clearUserCaches(User user) {
		Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
		Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
	}
}

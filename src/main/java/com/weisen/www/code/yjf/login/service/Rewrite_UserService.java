package com.weisen.www.code.yjf.login.service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
import com.weisen.www.code.yjf.login.service.dto.Rewrite_submitResetPasswrodDTO;
import com.weisen.www.code.yjf.login.service.dto.UserDTO;
import com.weisen.www.code.yjf.login.service.util.CheckUtils;
import com.weisen.www.code.yjf.login.service.util.Result;
import com.weisen.www.code.yjf.login.web.rest.SensitiveWord;
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
        SmsService smsService = rewrite_SmsServiceRepository.findOneByPhoneAndType(userDTO.getLogin(),"用户注册");
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
    public String rewrite_changePassword(String currentClearTextPassword, String newPassword) {
        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        String login = currentUserLogin.get();
        Optional<User> oneByLogin = userRepository.findOneByLogin(login);
        User user = oneByLogin.get();
        String currentEncryptedPassword = user.getPassword();
        if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
            return "旧密码错误";
        }
        String encryptedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encryptedPassword);
        this.clearUserCaches(user);
        log.debug("Changed password for User: {}", user);
//        SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).ifPresent(user -> {
//            String currentEncryptedPassword = user.getPassword();
//            if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
//                throw new InvalidPasswordException();
//            }
//            String encryptedPassword = passwordEncoder.encode(newPassword);
//            user.setPassword(encryptedPassword);
//            this.clearUserCaches(user);
//            log.debug("Changed password for User: {}", user);
//        });
        return "修改成功";
    }

    public void completePasswordReset(String password) {
        SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).ifPresent(user -> {
            String encryptedPassword = passwordEncoder.encode(password);
            user.setPassword(encryptedPassword);
            this.clearUserCaches(user);
            log.debug("Changed password for User: {}", user);
        });
    }

    public void completePasswordResetAdmin(String id, String password) {
        User user = userRepository.findById(Long.parseLong(id)).get();
        String encryptedPassword = passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);
        this.clearUserCaches(user);
        log.debug("Changed password for User: {}", user);
    }

    public String delUsers(String userid) {
        User user = userRepository.findById(Long.parseLong(userid)).get();
        if (user != null && user.getLogin().length() != 11) {
            userRepository.delete(user);
            return "成功";
        } else {
            return "操作失败";
        }
    }

    public String updateUser(UserDTO userDTO) {
    	if(null != userDTO.getFirstName()) {
    		if (!SensitiveWord.check(userDTO.getFirstName())) {
    			return "昵称包含敏感词，请重新修改";
    		}
    	}
        User user = userRepository.findById(userDTO.getId()).get();
        this.clearUserCaches(user);
        user.setImageUrl(userDTO.getImageUrl() == null ? user.getImageUrl() : userDTO.getImageUrl());
        user.setFirstName(userDTO.getFirstName() == null ? user.getFirstName() : userDTO.getFirstName());
        user = userRepository.save(user);
        this.clearUserCaches(user);
        return user.getId() == userDTO.getId() ? "修改成功" : "修改失败，请稍后再试";
    }
    
    
    public Result updateUser1(UserDTO userDTO) {
    	if(null != userDTO.getFirstName()) {
    		if (!SensitiveWord.check(userDTO.getFirstName())) {
    			return Result.fail("昵称包含敏感词，请重新修改");
    		}
    	}
        User user = userRepository.findById(userDTO.getId()).get();
        this.clearUserCaches(user);
        user.setImageUrl(userDTO.getImageUrl() == null ? user.getImageUrl() : userDTO.getImageUrl());
        user.setFirstName(userDTO.getFirstName() == null ? user.getFirstName() : userDTO.getFirstName());
        user = userRepository.save(user);
        this.clearUserCaches(user);
        return user.getId() == userDTO.getId() ? Result.fail("修改成功") :  Result.fail("修改失败，请稍后再试");
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

    public String changePasswordByCodeAndPhone(Rewrite_submitResetPasswrodDTO rewrite_submitResetPasswrodDTO) {
        if (!CheckUtils.checkObj(rewrite_submitResetPasswrodDTO))
            return "修改资料错误,请重试";
        else if (!CheckUtils.checkPhoneNumber(rewrite_submitResetPasswrodDTO.getPhone()))
            return "电话号码错误,请检查电话号码";
        else if (!CheckUtils.checkString(rewrite_submitResetPasswrodDTO.getCode()))
            return "验证码不能为空";
        else if (!CheckUtils.checkString(rewrite_submitResetPasswrodDTO.getPassword()))
            return "新密码不能为空";
        else if (rewrite_submitResetPasswrodDTO.getPassword().length() < 6 || rewrite_submitResetPasswrodDTO.getPassword().length() > 18)
            return "密码长度请保持在6-18位之间,建议使用数字,字母或者符号混搭的强密码";
        else {
            // 根据当前手机号码和验证码类型去查询验证码,先判断验证码是否对应
            SmsService code = rewrite_SmsServiceRepository.findOneByPhoneAndType(rewrite_submitResetPasswrodDTO.getPhone(), "修改密码");
            if (code == null) {
                return "非法调用";
            } else if ((System.currentTimeMillis() - code.getSendtime()) / 1000 > DEF_OVERDUE) {
                return "验证码已失效";
            } else if (!code.getCode().equals(rewrite_submitResetPasswrodDTO.getCode())) {
                return "验证码不正确";
            } else {
                // 然后根据电话号码找到当前的登录用户的userid,去修改密码
                Optional<User> oneByLogin = userRepository.findOneByLogin(rewrite_submitResetPasswrodDTO.getPhone());
                User user = oneByLogin.get();
                String encryptedPassword = passwordEncoder.encode(rewrite_submitResetPasswrodDTO.getPassword());
                user.setPassword(encryptedPassword);
                this.clearUserCaches(user);
                log.debug("根据验证码重新设置密码 for User: {}", user);
                return "重置密码成功";
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("12asdf..".length());
    }
}

package com.weisen.www.code.yjf.login.web.rest;


import com.weisen.www.code.yjf.login.domain.User;
import com.weisen.www.code.yjf.login.repository.UserRepository;
import com.weisen.www.code.yjf.login.security.SecurityUtils;
import com.weisen.www.code.yjf.login.service.MailService;
import com.weisen.www.code.yjf.login.service.Rewrite_UserService;
import com.weisen.www.code.yjf.login.service.dto.PasswordChangeDTO;
import com.weisen.www.code.yjf.login.service.dto.UserDTO;
import com.weisen.www.code.yjf.login.web.rest.errors.*;
import com.weisen.www.code.yjf.login.web.rest.vm.KeyAndPasswordVM;
import com.weisen.www.code.yjf.login.web.rest.vm.ManagedUserVM;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
@Api(tags = "000-用户操作")
public class Rewrite_AccountResource {

    private static class AccountResourceException extends RuntimeException {
        private AccountResourceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(Rewrite_AccountResource.class);

    private final UserRepository userRepository;

    private final Rewrite_UserService rewrite_UserService;

    private final MailService mailService;

    public Rewrite_AccountResource(UserRepository userRepository, Rewrite_UserService rewrite_UserService, MailService mailService) {

        this.userRepository = userRepository;
        this.rewrite_UserService = rewrite_UserService;
        this.mailService = mailService;
    }

    /**
     * {@code POST  /register} : register the user.
     *
     * @param managedUserVM the managed user View Model.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already used.
     */
    @GetMapping("/registerOverride")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("生成一个随机用户,并返回用户ID")
    public String registerAccount(HttpServletRequest request) {
        return rewrite_UserService.registerUser();
    }
    
    @PostMapping("/registerOverride")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("根据手机号生成一个用户,并返回用户ID")
    public String registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
        if (!checkPasswordLength(managedUserVM.getPassword())) {
            throw new InvalidPasswordException();
        }
        return rewrite_UserService.registerUser(managedUserVM, managedUserVM.getPassword());
    }

    /**
     * {@code POST  /account/change-password} : changes the current user's password.
     *
     * @param passwordChangeDto current and new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the new password is incorrect.
     */
    @PostMapping(path = "/accountOverride/change-password")
    @ApiOperation("使用旧密码修改密码")
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        if (!checkPasswordLength(passwordChangeDto.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        rewrite_UserService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
    }

    /**
     * {@code POST   /account/reset-password/finish} : Finish to reset the password of the user.
     *
     * @param keyAndPassword the generated key and the new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the password could not be reset.
     */
    @PostMapping(path = "/accountOverride/reset-password/finish")
    @ApiOperation("直接修改密码")
    public void finishPasswordReset(@RequestBody String password) {
        if (!checkPasswordLength(password)) {
            throw new InvalidPasswordException();
        }
        rewrite_UserService.completePasswordReset(password);
    }

    private static boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) &&
            password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
            password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }
}

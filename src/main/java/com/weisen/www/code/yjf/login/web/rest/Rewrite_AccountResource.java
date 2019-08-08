package com.weisen.www.code.yjf.login.web.rest;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.weisen.www.code.yjf.login.repository.UserRepository;
import com.weisen.www.code.yjf.login.security.AuthoritiesConstants;
import com.weisen.www.code.yjf.login.service.MailService;
import com.weisen.www.code.yjf.login.service.Rewrite_UserService;
import com.weisen.www.code.yjf.login.service.dto.PasswordChangeDTO;
import com.weisen.www.code.yjf.login.web.rest.errors.EmailAlreadyUsedException;
import com.weisen.www.code.yjf.login.web.rest.errors.InvalidPasswordException;
import com.weisen.www.code.yjf.login.web.rest.errors.LoginAlreadyUsedException;
import com.weisen.www.code.yjf.login.web.rest.vm.ManagedUserVM;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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

    private final Rewrite_UserService rewrite_UserService;

    public Rewrite_AccountResource(Rewrite_UserService rewrite_UserService) {
        this.rewrite_UserService = rewrite_UserService;
    }

    /**
     * {@code POST  /register} : register the user.
     *
     * @param managedUserVM the managed user View Model.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already used.
     */
    @GetMapping("/public/random-user")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("生成一个随机用户,并返回用户ID")
    public String registerAccount(HttpServletRequest request) {
        return rewrite_UserService.registerUser();
    }
    
    @PostMapping("/public/phone-user")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("根据手机号生成一个用户,并返回用户ID")
    public String registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
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
    
    @PostMapping(path = "/accountOverride/reset-password/finish/admin")
    @ApiOperation("直接修改密码admin")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public void finishPasswordReset(@RequestBody String id,  String password) {
        if (!checkPasswordLength(password)) {
            throw new InvalidPasswordException();
        }
        rewrite_UserService.completePasswordResetAdmin(id, password);
    }

    private static boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) &&
            password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
            password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }
}

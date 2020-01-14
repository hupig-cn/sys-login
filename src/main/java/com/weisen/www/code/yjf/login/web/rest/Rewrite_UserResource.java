package com.weisen.www.code.yjf.login.web.rest;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weisen.www.code.yjf.login.service.Rewrite_UserService;
import com.weisen.www.code.yjf.login.service.dto.UserDTO;
import com.weisen.www.code.yjf.login.service.util.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
@Api(tags = "000-个人信息修改")
public class Rewrite_UserResource {

    private final Logger log = LoggerFactory.getLogger(Rewrite_UserResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Rewrite_UserService rewrite_UserService;

    public Rewrite_UserResource(Rewrite_UserService rewrite_UserService ) {
        this.rewrite_UserService = rewrite_UserService;
    }

    @PutMapping("/users-ImageOrName")
    @ApiOperation("修改用户的头像或昵称")
    public String updateUserImageOrName(@Valid @RequestBody UserDTO userDTO) {
        log.debug("REST request to update User : {}", userDTO);
        return rewrite_UserService.updateUser(userDTO);
    }
    
    @DeleteMapping("/public/deluser/{userid}")
    public String deleteUser(@PathVariable String userid) {
    	return rewrite_UserService.delUsers(userid);
    }
    
    @PutMapping("/users-ImageOrName1")
    @ApiOperation("修改用户的头像或昵称1")
    public Result updateUserImageOrName1(@Valid @RequestBody UserDTO userDTO) {
        log.debug("REST request to update User : {}", userDTO);
        return rewrite_UserService.updateUser1(userDTO);
    }
}

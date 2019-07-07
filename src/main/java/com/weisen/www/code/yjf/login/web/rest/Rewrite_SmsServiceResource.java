package com.weisen.www.code.yjf.login.web.rest;

import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weisen.www.code.yjf.login.service.Rewrite_SmsServiceService;
import com.weisen.www.code.yjf.login.service.dto.SmsServiceDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * REST controller for managing {@link com.weisen.www.code.yjf.login.domain.SmsService}.
 */
@RestController
@RequestMapping("/api")
@Api(tags = "001-短信操作")
public class Rewrite_SmsServiceResource {

    private final Logger log = LoggerFactory.getLogger(Rewrite_SmsServiceResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Rewrite_SmsServiceService rewrite_SmsServiceService;

    public Rewrite_SmsServiceResource(Rewrite_SmsServiceService rewrite_SmsServiceService) {
        this.rewrite_SmsServiceService = rewrite_SmsServiceService;
    }

    /**
     * {@code POST  /sms-services} : 发送注册短信验证码.
     *
     * @param smsServiceDTO the smsServiceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new smsServiceDTO, or with status {@code 400 (Bad Request)} if the smsService has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/public/send-sms")
    @ApiOperation("发送注册新用户的短信验证码")
    public String createSmsService(@RequestBody SmsServiceDTO smsServiceDTO) {
        log.debug("REST request to save SmsService : {}", smsServiceDTO);
        return rewrite_SmsServiceService.save(smsServiceDTO.getPhone());
    }

}

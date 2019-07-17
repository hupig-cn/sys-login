package com.weisen.www.code.yjf.login.web.rest;

import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weisen.www.code.yjf.login.domain.User;
import com.weisen.www.code.yjf.login.repository.UserRepository;
import com.weisen.www.code.yjf.login.service.MailService;
import com.weisen.www.code.yjf.login.service.Rewrite_UserService;
import com.weisen.www.code.yjf.login.service.dto.UserDTO;
import com.weisen.www.code.yjf.login.web.rest.errors.EmailAlreadyUsedException;
import com.weisen.www.code.yjf.login.web.rest.errors.LoginAlreadyUsedException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * REST controller for managing users.
 * <p>
 * This class accesses the {@link User} entity, and needs to fetch its collection of authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this case.
 */
@RestController
@RequestMapping("/api")
@Api(tags = "000-个人信息修改")
public class Rewrite_UserResource {

    private final Logger log = LoggerFactory.getLogger(Rewrite_UserResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Rewrite_UserService rewrite_UserService;

    private final UserRepository userRepository;

    private final MailService mailService;

    public Rewrite_UserResource(Rewrite_UserService rewrite_UserService, UserRepository userRepository, MailService mailService) {

        this.rewrite_UserService = rewrite_UserService;
        this.userRepository = userRepository;
        this.mailService = mailService;
    }

    @PutMapping("/users-ImageOrName")
    @ApiOperation("修改用户的头像或昵称")
    public String updateUserImageOrName(@Valid @RequestBody UserDTO userDTO) {
        log.debug("REST request to update User : {}", userDTO);
        return rewrite_UserService.updateUser(userDTO);
    }
}

package com.weisen.www.code.yjf.login.service.mapper;

import com.weisen.www.code.yjf.login.domain.*;
import com.weisen.www.code.yjf.login.service.dto.SmsServiceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SmsService} and its DTO {@link SmsServiceDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SmsServiceMapper extends EntityMapper<SmsServiceDTO, SmsService> {



    default SmsService fromId(Long id) {
        if (id == null) {
            return null;
        }
        SmsService smsService = new SmsService();
        smsService.setId(id);
        return smsService;
    }
}

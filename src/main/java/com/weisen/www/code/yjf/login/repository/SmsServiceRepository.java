package com.weisen.www.code.yjf.login.repository;

import com.weisen.www.code.yjf.login.domain.SmsService;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SmsService entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SmsServiceRepository extends JpaRepository<SmsService, Long> {

}

package com.weisen.www.code.yjf.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.weisen.www.code.yjf.login.domain.SmsService;

/**
 * Spring Data  repository for the SmsService entity.
 */
@SuppressWarnings("unused")
@Repository
public interface Rewrite_SmsServiceRepository extends JpaRepository<SmsService, Long> {
	
	SmsService findOneByPhone(String phone);
	
}

package com.weisen.www.code.yjf.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.weisen.www.code.yjf.login.domain.SmsService;

/**
 * Spring Data  repository for the SmsService entity.
 */
@Repository
public interface Rewrite_SmsServiceRepository extends JpaRepository<SmsService, Long> {
	
	SmsService findOneByPhone(String phone);
	
	@Query(nativeQuery = true, value = "select * from sms_service where phone = ?1 and jhi_type = ?2")
	SmsService findOneByPhoneAndType(String phone,String type);
	
}

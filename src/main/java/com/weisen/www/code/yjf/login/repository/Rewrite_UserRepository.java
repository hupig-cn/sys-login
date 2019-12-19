package com.weisen.www.code.yjf.login.repository;

import java.time.Instant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.weisen.www.code.yjf.login.domain.User;

public interface Rewrite_UserRepository extends JpaRepository<User, Long> {

	// 根据用户手机号查找用户数据
	@Query(value = "select * from jhi_user where login = ?1 ", nativeQuery = true)
	User finByLogin(String userPhone);

	// 根据用户ID查找用户数据
	User findUserById(Long userId);
}

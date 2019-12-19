package com.weisen.www.code.yjf.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weisen.www.code.yjf.login.domain.User;

public interface Rewrite_UserRepository extends JpaRepository<User, Long> {
	
	// 根据用户ID查找用户数据
	User findUserById(Long id);

}

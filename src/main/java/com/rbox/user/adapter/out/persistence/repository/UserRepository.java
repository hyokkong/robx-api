package com.rbox.user.adapter.out.persistence.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 사용자 정보를 조회하기 위한 MyBatis Mapper.
 */
@Mapper
public interface UserRepository {
    /**
     * 이메일로 사용자 정보를 조회한다.
     *
     * @param email 사용자 이메일
     * @return 사용자 엔티티
     */
    UserEntity findByEmail(@Param("email") String email);

    /**
     * ID로 사용자 정보를 조회한다.
     *
     * @param id 사용자 ID
     * @return 사용자 엔티티
     */
    UserEntity findById(@Param("id") Long id);
}

package com.rbox.user.adapter.out.persistence.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRepository {
    UserEntity findByEmail(@Param("email") String email);
    UserEntity findById(@Param("id") Long id);
}

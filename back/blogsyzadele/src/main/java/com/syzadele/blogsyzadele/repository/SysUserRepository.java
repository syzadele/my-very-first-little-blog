package com.syzadele.blogsyzadele.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.syzadele.blogsyzadele.model.SysUser;

public interface SysUserRepository extends JpaRepository<SysUser, Long> {
    SysUser findByUsername(String username);
}

package com.jaramgroupware.mms.domain.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Role(Object)에 의해 생성된 DB에 접근하는 메서드들을 사용하기 위한 인터페이스
 * @since 2023-03-07
 * @author 황준서(37기) hzser123@gmail.com
 * @author 이현희(38기) heeit13145@gmail.com
 */
@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findRoleById (Long id);
    List<Role> findAllBy();
}

package com.jaramgroupware.mms.domain.member;


import com.jaramgroupware.mms.domain.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Member(Object)에 의해 생성된 DB에 접근하는 메서드들을 사용하기 위한 인터페이스
 *
 * @author 황준서(37기) hzser123@gmail.com
 * @author 이현희(38기) heeit13145@gmail.com
 * @since 2023-03-07
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, String>{
    Optional<Member> findMemberById(String id);
    List<Member> findAllByRole(Role role);
    boolean existsByEmail(String email);


}

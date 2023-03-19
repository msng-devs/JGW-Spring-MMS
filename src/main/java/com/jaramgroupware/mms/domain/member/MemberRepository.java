package com.jaramgroupware.mms.domain.member;


import com.jaramgroupware.mms.domain.rank.Rank;
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
 * @since 2023-03-07
 * @author 황준서(37기) hzser123@gmail.com
 * @author 이현희(38기) heeit13145@gmail.com
 */
@Repository
public interface MemberRepository extends JpaRepository<Member,String>, JpaSpecificationExecutor<Member> {
    Optional<Member> findMemberById(String id);
    Optional<List<Member>> findAllBy();
    boolean existsByEmail(String email);

    /**
     * 검색하고자 하는 Member(Object)와 연관된 Role(Object)을 함께 로드하기 위한 메서드
     * @param ids 검색하고자 하는 엔티티들의 PK들 (Set type)
     * @return 검색된 Member(Object)의 List를 반환
     */
    @Query("SELECT m FROM MEMBER m JOIN FETCH m.role WHERE m.id IN :ids")
    List<Member> findAllByIdIn(@Param("ids") Set<String> ids);

    /**
     * 인자로 받은 ids의 Member(Object)를 모두 삭제하기 위한 메서드
     * @param ids 삭제하고자 하는 Member(Object)의 PK들 (Set type)
     * @return 삭제된 Member(Object)의 개수를 반환
     */
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM MEMBER m WHERE m.id IN :ids")
    int deleteAllByIdInQuery(@Param("ids") Set<String> ids);
}

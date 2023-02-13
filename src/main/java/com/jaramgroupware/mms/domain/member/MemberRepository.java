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

@Repository
public interface MemberRepository extends JpaRepository<Member,String>, JpaSpecificationExecutor<Member>,MemberCustomRepository{
    Optional<Member> findMemberById(String id);
    Optional<List<Member>> findAllBy();
    boolean existsByEmail(String email);

    @Query("SELECT m FROM MEMBER m JOIN FETCH m.role WHERE m.id IN :ids")
    List<Member> findAllByIdIn(@Param("ids") Set<String> ids);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM MEMBER m WHERE m.id IN :ids")
    int deleteAllByIdInQuery(@Param("ids") Set<String> ids);
}

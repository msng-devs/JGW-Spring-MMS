package com.jaramgroupware.mms.domain.registerCode;

import com.jaramgroupware.mms.domain.preMemberInfo.PreMemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * RegisterCode(Object)에 의해 생성된 DB에 접근하는 메서드들을 사용하기 위한 인터페이스
 * @since 2023-06-25
 * @author 황준서(37기) hzser123@gmail.com
 * @author 이현희(38기) heeit13145@gmail.com
 */
@Repository
public interface RegisterCodeRepository extends JpaRepository<RegisterCode, String> {
    Optional<RegisterCode> findByCode(String code);
    Optional<RegisterCode> findByPreMemberInfo(PreMemberInfo preMemberInfo);

    @Query("SELECT rc FROM REGISTER_CODE rc WHERE rc.expiredAt <= :expiredDate")
    List<RegisterCode> findAllExpired(@Param("expiredDate") LocalDate expiredDate);

    @Modifying
    @Query("DELETE FROM REGISTER_CODE rc WHERE rc.code IN :codes")
    void deleteAllByCodeIn(@Param("codes") List<String> codes);
}

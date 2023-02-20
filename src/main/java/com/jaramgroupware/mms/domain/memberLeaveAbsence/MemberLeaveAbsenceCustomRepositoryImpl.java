package com.jaramgroupware.mms.domain.memberLeaveAbsence;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
public class MemberLeaveAbsenceCustomRepositoryImpl implements MemberLeaveAbsenceCustomRepository {

    private final JdbcTemplate jdbcTemplate;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //https://gksdudrb922.tistory.com/154

    @Override
    public void bulkInsert(List<MemberLeaveAbsence> memberLeaveAbsences) {
        String sql = "INSERT INTO `MEMBER_LEAVE_ABSENCE` (`MEMBER_LEAVE_ABSENCE_PK`, `MEMBER_LEAVE_ABSENCE_STATUS`, `MEMBER_LEAVE_ABSENCE_EXPECTED_DATE_RETURN_SCHOOL`) VALUES (?,?,?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                MemberLeaveAbsence memberLeaveAbsence = memberLeaveAbsences.get(i);
                ps.setString(1,memberLeaveAbsence.getMember().getId());
                ps.setBoolean(2,memberLeaveAbsence.isStatus());
                ps.setObject(3,memberLeaveAbsence.getExpectedDateReturnSchool());
            }

            @Override
            public int getBatchSize() {
                return memberLeaveAbsences.size();
            }
        });
        memberLeaveAbsences.clear();
    }
}

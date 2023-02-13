package com.jaramgroupware.mms.domain.memberInfo;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class MemberInfoCustomRepositoryImpl implements MemberInfoCustomRepository {

    private final JdbcTemplate jdbcTemplate;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //https://gksdudrb922.tistory.com/154

    @Override
    public void bulkInsert(List<MemberInfo> memberInfos,String who) {
        String sql = "INSERT INTO `MEMBER_INFO` (`MEMBER_INFO_PK`, `MEMBER_INFO_CELL_PHONE_NUMBER`, `MEMBER_INFO_STUDENT_ID`, `MEMBER_INFO_YEAR`, `RANK_RANK_PK`, `MAJOR_MAJOR_PK`, `MEMBER_INFO_DATEOFBIRTH`, `MEMBER_INFO_MODIFIED_DTTM`, `MEMBER_INFO_CREATED_DTTM`, `MEMBER_INFO_MODIFIED_BY`, `MEMBER_INFO_CREATED_BY`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                MemberInfo memberInfo = memberInfos.get(i);
                ps.setString(1,memberInfo.getMember().getId());
                ps.setString(2,memberInfo.getPhoneNumber());
                ps.setString(3,memberInfo.getStudentID());
                ps.setInt(4,memberInfo.getYear());
                ps.setInt(5,memberInfo.getRank().getId());
                ps.setInt(6,memberInfo.getMajor().getId());
                ps.setObject(7,memberInfo.getDateOfBirth());
                ps.setObject(8, LocalDateTime.now());
                ps.setObject(9,LocalDateTime.now());
                ps.setString(10,who);
                ps.setString(11,who);
            }

            @Override
            public int getBatchSize() {
                return memberInfos.size();
            }
        });
        memberInfos.clear();
    }

    public void bulkUpdate(List<MemberInfo> memberInfos,String who) {
        String sql = "UPDATE `MEMBER_INFO`"
                + " SET `MEMBER_INFO_CELL_PHONE_NUMBER` = (?), `MEMBER_INFO_YEAR` = (?), `RANK_RANK_PK` = (?), `MAJOR_MAJOR_PK` = (?), `MEMBER_INFO_MODIFIED_DTTM` = (?), `MEMBER_INFO_MODIFIED_BY` = (?) "
                + " WHERE `MEMBER_INFO_PK` = (?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                MemberInfo memberInfo = memberInfos.get(i);
                ps.setString(1,memberInfo.getPhoneNumber());
                ps.setInt(2,memberInfo.getYear());
                ps.setInt(3,memberInfo.getRank().getId());
                ps.setInt(4,memberInfo.getMajor().getId());
                ps.setObject(5,LocalDateTime.now());
                ps.setString(6,who);
                ps.setString(7,memberInfo.getMember().getId());
            }

            @Override
            public int getBatchSize() {
                return memberInfos.size();
            }
        });
        memberInfos.clear();
    }
}

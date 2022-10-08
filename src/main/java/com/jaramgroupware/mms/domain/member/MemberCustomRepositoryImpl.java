package com.jaramgroupware.mms.domain.member;//package com.jaramgroupware.mms.domain.attendance;

import lombok.RequiredArgsConstructor;
import org.hibernate.engine.jdbc.batch.spi.Batch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {

    private final JdbcTemplate jdbcTemplate;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //https://gksdudrb922.tistory.com/154

    @Override
    public void bulkInsert(List<Member> members,String who) {
        String sql = "INSERT INTO `MEMBER` (`MEMBER_PK`, `MEMBER_NM`, `MEMBER_MODIFIED_DTTM`, `MEMBER_CREATED_DTTM`, `MEMBER_EMAIL`, `MEMBER_CELL_PHONE_NUMBER`, `MEMBER_STUDENT_ID`, `MEMBER_YEAR`, `ROLE_ROLE_PK`, `RANK_RANK_PK`, `MAJOR_MAJOR_PK`, `MEMBER_LEAVE_ABSENCE`,`MEMBER_CREATED_BY`,`MEMBER_MODIFIED_BY`,`MEMBER_DATEOFBIRTH`) VALUES (?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ? ,?,?,?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Member member = members.get(i);
                ps.setString(1,member.getId());
                ps.setString(2,member.getName());
                ps.setObject(3, LocalDateTime.now());
                ps.setObject(4, LocalDateTime.now());
                ps.setString(5,member.getEmail());
                ps.setString(6,member.getPhoneNumber());
                ps.setString(7,member.getStudentID());
                ps.setInt(8,member.getYear());
                ps.setInt(9,member.getRole().getId());
                ps.setInt(10,member.getRank().getId());
                ps.setInt(11,member.getMajor().getId());
                ps.setBoolean(12,member.isLeaveAbsence());
                ps.setString(13,who);
                ps.setString(14,who);
                ps.setObject(15,member.getDateOfBirth());
            }

            @Override
            public int getBatchSize() {
                return members.size();
            }
        });
        members.clear();
    }
    public void bulkUpdate(List<Member> members,String who) {
        String sql = "UPDATE `MEMBER`"
                + " SET `MEMBER_NM` = (?), `MEMBER_CELL_PHONE_NUMBER` = (?), `MAJOR_MAJOR_PK` = (?), `RANK_RANK_PK` = (?) , `ROLE_ROLE_PK` = (?),`MEMBER_YEAR` = (?),`MEMBER_MODIFIED_DTTM` = (?),`MEMBER_LEAVE_ABSENCE` = (?),`MEMBER_MODIFIED_BY` = (?) "
                + " WHERE `MEMBER_PK` = (?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Member member = members.get(i);
                ps.setString(1,member.getName());
                ps.setString(2,member.getPhoneNumber());
                ps.setLong(3,member.getMajor().getId());
                ps.setLong(4,member.getRank().getId());
                ps.setLong(5,member.getRole().getId());
                ps.setInt(6,member.getYear());
                ps.setObject(7,LocalDateTime.now());
                ps.setBoolean(8,member.isLeaveAbsence());
                ps.setString(9,who);
                ps.setString(10,member.getId());
            }

            @Override
            public int getBatchSize() {
                return members.size();
            }
        });
        members.clear();
    }
}

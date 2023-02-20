package com.jaramgroupware.mms.domain.member;

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
    public void bulkInsert(List<Member> members) {
        String sql = "INSERT INTO `MEMBER` (`MEMBER_PK`, `MEMBER_NM`, `MEMBER_EMAIL`, `ROLE_ROLE_PK`, `MEMBER_STATUS`) VALUES (?,?,?,?,?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Member member = members.get(i);
                ps.setString(1,member.getId());
                ps.setString(2,member.getName());
                ps.setString(3,member.getEmail());
                ps.setInt(4,member.getRole().getId());
                ps.setBoolean(5,member.isStatus());
            }

            @Override
            public int getBatchSize() {
                return members.size();
            }
        });
        members.clear();
    }
    public void bulkUpdate(List<Member> members) {
        String sql = "UPDATE `MEMBER`"
                + " SET `MEMBER_NM` = (?), `ROLE_ROLE_PK` = (?),`MEMBER_STATUS` = (?) "
                + " WHERE `MEMBER_PK` = (?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Member member = members.get(i);
                ps.setString(1,member.getName());
                ps.setInt(2,member.getRole().getId());
                ps.setBoolean(3,member.isStatus());
                ps.setString(4,member.getId());
            }

            @Override
            public int getBatchSize() {
                return members.size();
            }
        });
        members.clear();
    }
}

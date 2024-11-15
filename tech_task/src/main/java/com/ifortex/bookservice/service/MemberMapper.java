package com.ifortex.bookservice.service;

import com.ifortex.bookservice.model.Member;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class MemberMapper implements RowMapper<Member> {
    @Override
    public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
        Member member = new Member();
        member.setId(rs.getLong("id"));
        member.setName(rs.getString("name"));
        LocalDateTime localDateTime = rs.getObject("membership_date", LocalDateTime.class);
        member.setMembershipDate(localDateTime);
        return member;
    }
}
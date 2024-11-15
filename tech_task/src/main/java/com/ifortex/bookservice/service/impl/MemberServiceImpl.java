package com.ifortex.bookservice.service.impl;

import com.ifortex.bookservice.model.Member;
import com.ifortex.bookservice.service.MemberMapper;
import com.ifortex.bookservice.service.MemberService;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@Primary
public class MemberServiceImpl implements MemberService {

    JdbcTemplate jdbcTemplate;

    public MemberServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Member findMember() {
        String sql = "SELECT m.id, m.name, b.title, b.publication_date, m.membership_date FROM members m JOIN member_books mb ON m.id = mb.member_id JOIN books b ON b.id = mb.book_id WHERE 'Romance' = ANY(b.genre) ORDER BY b.publication_date ASC, m.membership_date DESC LIMIT 1";
        return jdbcTemplate.query(sql, new MemberMapper())
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Member> findMembers() {
        String sql = "SELECT m.id, m.name, m.membership_date FROM members m\n LEFT JOIN member_books mb ON m.id = mb.member_id WHERE EXTRACT(YEAR FROM m.membership_date) = 2023 AND mb.book_id IS NULL";
        return jdbcTemplate.query(sql, new MemberMapper());
    }
}
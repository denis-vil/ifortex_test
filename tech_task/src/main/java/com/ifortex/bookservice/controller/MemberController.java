package com.ifortex.bookservice.controller;

import com.ifortex.bookservice.model.Member;
import com.ifortex.bookservice.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/oldest-romantic-members")
    public Member findMember() {
        return memberService.findMember();
    }

    @GetMapping("/members-with-no-book-history")
    public List<Member> findMembers() {
        return memberService.findMembers();
    }
}
package hoon.football.member.service.impl;

import hoon.football.member.domain.Member;
import hoon.football.member.exception.exceptions.*;
import hoon.football.member.repository.MemberRepository;
import hoon.football.member.service.MemberService;
import hoon.football.validator.member.MemberValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberValidator memberValidator;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override
    public Member save(Member member) {
        memberValidator.validateForSignUp(member.getUsername(), member.getPassword());

        String encodedPassword = passwordEncoder.encode(member.getPassword());
        Member encodedMember = new Member(member.getUsername(), encodedPassword); // password 가, encoded 된 멤버객체를 저장.

        return memberRepository.save(encodedMember);
    }

    @Override
    @Transactional(readOnly = true)
    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("[ID(pk)] 조회실패"));
    }

    @Override
    @Transactional(readOnly = true)
    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberNotFoundException("[USERNAME] 조회 실패"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Member login(String username, String password) {
        Member loginMember = findByUsername(username);

        checkPasswordToEncoded(password, loginMember);

        return loginMember;

    }

    @Override
    @Transactional(readOnly = true)
    public List<Member> findByTeamId(Long teamId) {
        return memberRepository.findByTeamId(teamId);
    }



    private void checkPasswordToEncoded(String password, Member loginMember) {
        if (!passwordEncoder.matches(password, loginMember.getPassword())) {
            throw new MemberLoginException("로그인에 실패했습니다.");
        }
    }

}

package hoon.football.member.service.impl;

import hoon.football.member.domain.Member;
import hoon.football.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class MemberServiceImplSuccessTest {

    @Autowired MemberService memberService;
    @Autowired PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("저장_성공")
    void save_success() {
        // given
        Member userA = new Member("userA", "1234");

        // when
        Member savedMember = memberService.save(userA);
        boolean matchPassword = passwordEncoder.matches(userA.getPassword(), savedMember.getPassword());

        // then
        assertThat(matchPassword).isTrue();
        assertThat(savedMember.getUsername()).isEqualTo("userA");
        assertThat(savedMember.getRating()).isEqualTo(1000);
    }

    @Test
    @DisplayName("PK인 Id 값으로 조회_성공")
    void findById_success() {
        // given
        Member userA = new Member("userA", "1234");
        Member savedMember = memberService.save(userA);

        // when
        Member findMember = memberService.findById(savedMember.getId());

        // then
        assertThat(findMember.getUsername()).isEqualTo("userA");
        assertThat(findMember.getRating()).isEqualTo(1000);
    }

    @Test
    @DisplayName("username 값으로 조회_성공")
    void findByUsername_success() {
        // given
        Member userA = new Member("userA", "1234");
        Member savedMember = memberService.save(userA);

        // when
        Member findMember = memberService.findByUsername(savedMember.getUsername());

        // then
        assertThat(findMember.getUsername()).isEqualTo("userA");
        assertThat(findMember.getRating()).isEqualTo(1000);
    }

    @Test
    @DisplayName("모든 멤버 조회_성공")
    void findAll_success() {
        // given
        Member userA = new Member("userA", "1234");
        Member userB = new Member("userB", "1234");
        Member savedUserA = memberService.save(userA);
        Member savedUserB = memberService.save(userB);

        // when
        List<Member> members = memberService.findAll();

        // then
        assertThat(members).hasSize(2);
        assertThat(members).contains(savedUserA, savedUserB);
    }

    @Test
    @DisplayName(value = "로그인_성공")
    void login_success() throws Exception {
        // given
        Member userA = new Member("userA", "1234"); // "userA", "1234"
        memberService.save(userA);

        // when
        Member loginMember = memberService.login("userA", "1234");
        boolean matchPassword = passwordEncoder.matches(userA.getPassword(), loginMember.getPassword());

        // then
        assertThat(loginMember.getUsername()).isEqualTo("userA");
        assertThat(matchPassword).isTrue();
    }

}
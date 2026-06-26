package hoon.football.member.service.impl;

import hoon.football.member.domain.Member;
import hoon.football.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceImplSuccessTest {

    @Autowired MemberService memberService;

    @Test
    @DisplayName("저장_성공")
    void save_success() {
        // given
        Member userA = new Member("userA", "1234");

        // when
        Member savedMember = memberService.save(userA);

        // then
        assertThat(savedMember).isEqualTo(userA);
        assertThat(savedMember.getId()).isEqualTo(userA.getId());
        assertThat(savedMember.getUsername()).isEqualTo("userA");
        assertThat(savedMember.getPassword()).isEqualTo("1234");
        assertThat(savedMember.getRating()).isEqualTo(1000);
    }

    @Test
    @DisplayName("PK인 Id 값으로 조회_성공")
    void findById_success() {
        // given
        Member userA = new Member("userA", "1234");
        memberService.save(userA);

        // when
        Member findMember = memberService.findById(userA.getId());

        // then
        assertThat(findMember).isEqualTo(userA);
        assertThat(findMember.getUsername()).isEqualTo("userA");
        assertThat(findMember.getPassword()).isEqualTo("1234");
        assertThat(findMember.getRating()).isEqualTo(1000);
    }

    @Test
    @DisplayName("username 값으로 조회_성공")
    void findByUsername_success() {
        // given
        Member userA = new Member("userA", "1234");
        memberService.save(userA);

        // when
        Member findMember = memberService.findByUsername("userA");

        // then
        assertThat(findMember).isEqualTo(userA);
        assertThat(findMember.getUsername()).isEqualTo("userA");
        assertThat(findMember.getPassword()).isEqualTo("1234");
        assertThat(findMember.getRating()).isEqualTo(1000);
    }

    @Test
    @DisplayName("모든 멤버 조회_성공")
    void findAll_success() {
        // given
        Member userA = new Member("userA", "1234");
        Member userB = new Member("userB", "1234");
        memberService.save(userA);
        memberService.save(userB);

        // when
        List<Member> members = memberService.findAll();

        // then
        assertThat(members).hasSize(2);
        assertThat(members).contains(userA, userB);
    }

    @Test
    @DisplayName(value = "로그인_성공")
    void login_success() throws Exception {
        // given
        Member userA = new Member("userA", "1234");
        memberService.save(userA);

        // when
        Member loginMember = memberService.login("userA", "1234");

        // then
        assertThat(loginMember).isEqualTo(userA);
        assertThat(loginMember.getUsername()).isEqualTo("userA");
        assertThat(loginMember.getPassword()).isEqualTo("1234");
    }

}
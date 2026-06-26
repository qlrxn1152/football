package hoon.football.member.service.impl;

import hoon.football.member.domain.Member;
import hoon.football.member.exception.DuplicateUsernameException;
import hoon.football.member.exception.MemberNotFoundException;
import hoon.football.member.exception.MemberPasswordLengthException;
import hoon.football.member.exception.MemberUsernameLengthException;
import hoon.football.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceImplFailTest {

    @Autowired MemberService memberService;

    @Test
    @DisplayName(value = "저장_실패 ( 아이디 이미 존재 )")
    void save_fail_duplicateUsername() throws Exception {
        // given
        Member userA = new Member("userA", "1234");
        memberService.save(userA);
        Member duplicateMember = new Member("userA", "1234");

        // when && then
        assertThatThrownBy(() -> memberService.save(duplicateMember))
                .isInstanceOf(DuplicateUsernameException.class);
    }

    @Test
    @DisplayName(value = "저장_실패 ( 아이디 길이 최소조건 )")
    void save_fail_usernameLength_min() throws Exception {
        // given
        Member userA = new Member("1", "1234");

        // when && then
        assertThatThrownBy(() -> memberService.save(userA))
                .isInstanceOf(MemberUsernameLengthException.class);
    }

    @Test
    @DisplayName(value = "저장_실패 ( 아이디 길이 최대조건 )")
    void save_fail_usernameLength_max() throws Exception {
        // given
        Member userA = new Member("aofdgkaofgkaaaaa", "1234");

        // when && then
        assertThatThrownBy(() -> memberService.save(userA))
                .isInstanceOf(MemberUsernameLengthException.class);
    }

    @Test
    @DisplayName(value = "저장_실패 ( 비밀번호 길이 최소조건 )")
    void save_fail_passwordLength_min() throws Exception {
        // given
        Member userA = new Member("userA", "1");

        // when && then
        assertThatThrownBy(() -> memberService.save(userA))
                .isInstanceOf(MemberPasswordLengthException.class);
    }

    @Test
    @DisplayName(value = "저장_실패 ( 비밀번호 길이 최대조건 )")
    void save_fail_passwordLength_max() throws Exception {
        // given
        Member userA = new Member("userA", "1sdaokfasodkfasokdfsaok");

        // when && then
        assertThatThrownBy(() -> memberService.save(userA))
                .isInstanceOf(MemberPasswordLengthException.class);
    }

    @Test
    @DisplayName(value = "조회_실패 ( PK 인 id 값으로 조회 실패 )")
    void findById_fail() throws Exception {
        // given
        Member userA = new Member("userA", "1234");
        memberService.save(userA);

        // when && then
        assertThatThrownBy(() -> memberService.findById(1243L))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName(value = "조회_실패 ( username 값으로 조회 실패 )")
    void findByUsername_fail() throws Exception {
        // given
        Member userA = new Member("userA", "1234");
        memberService.save(userA);

        // when && then
        assertThatThrownBy(() -> memberService.findByUsername("asdfokasdfok"))
                .isInstanceOf(MemberNotFoundException.class);
    }

}
package hoon.football.team.service.impl;

import hoon.football.member.domain.Member;
import hoon.football.member.service.MemberService;
import hoon.football.team.domain.Team;
import hoon.football.team.exception.exceptions.TeamCreateException;
import hoon.football.team.exception.exceptions.TeamNameDuplicateException;
import hoon.football.team.exception.exceptions.TeamNotFoundException;
import hoon.football.team.service.TeamService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TeamServiceImplFailTest {

    @Autowired MemberService memberService;
    @Autowired TeamService teamService;

    @Test
    @DisplayName(value = "팀조회 실패 ( 팀장멤버 존재 X )")
    void createTeam_fail_failFindMember() throws Exception {
        // when && then
        assertThatThrownBy(() -> teamService.createTeam("teamA", 124124L))
                .isInstanceOf(TeamCreateException.class)
                .hasMessage("팀장을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName(value = "팀조회 실패 ( 팀 이름 중복 )")
    void createTeam_fail_duplicateTeamName() throws Exception {
        // given
        Member member = new Member("userA", "1234");
        Member savedMember = memberService.save(member);
        teamService.createTeam("teamA", savedMember.getId()); // 팀이름이 teamA 인 팀을 생성.

        // when && then
        assertThatThrownBy(() -> teamService.createTeam("teamA", savedMember.getId())) // 중복 !
                .isInstanceOf(TeamNameDuplicateException.class)
                .hasMessage("팀 이름이 이미 존재합니다.");
    }

    @Test
    @DisplayName(value = "팀 조회 실패 ( PK 인 Id값으로 조회 실패 )")
    void findById_fail() throws Exception {
        // given
        Member member = new Member("userA", "1234");
        Member savedMember = memberService.save(member);
        teamService.createTeam("teamA", savedMember.getId());

        // when && then
        assertThatThrownBy(() -> teamService.findById(123L))
                .isInstanceOf(TeamNotFoundException.class)
                .hasMessage("ID로, 팀을 조회하지 못했습니다.");
    }

    @Test
    @DisplayName(value = "팀 조회 실패 ( teamName 으로 조회 실패 )")
    void findByTeamName_fail() throws Exception {
        // given
        Member member = new Member("userA", "1234");
        Member savedMember = memberService.save(member);
        teamService.createTeam("teamA", savedMember.getId());

        // when && then
        assertThatThrownBy(() -> teamService.findByTeamName("unknown"))
                .isInstanceOf(TeamNotFoundException.class)
                .hasMessage("TeamName으로, 팀을 조회하지 못했습니다.");
    }

    @Test
    @DisplayName(value = "팀 조회 실패 ( leaderMemberId 로 조회 실패 )")
    void findByLeaderMemberId_fail() throws Exception {
        // given
        Member member = new Member("userA", "1234");
        Member savedMember = memberService.save(member);
        teamService.createTeam("teamA", savedMember.getId());

        // when && then
        assertThatThrownBy(() -> teamService.findByLeaderMemberId(9999L))
                .isInstanceOf(TeamNotFoundException.class)
                .hasMessage("leaderMemberId로, 팀을 조회하지 못했습니다.");
    }



}
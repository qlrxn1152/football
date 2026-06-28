package hoon.football.team.service.impl;

import hoon.football.member.domain.Member;
import hoon.football.member.exception.exceptions.AlreadyJoinedTeamException;
import hoon.football.member.service.MemberService;
import hoon.football.team.domain.Team;
import hoon.football.team.exception.exceptions.*;
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
        Member memberB = new Member("userB", "1234");
        Member savedMemberB = memberService.save(memberB);
        teamService.createTeam("teamA", savedMember.getId()); // 팀이름이 teamA 인 팀을 생성.

        // when && then
        assertThatThrownBy(() -> teamService.createTeam("teamA", savedMemberB.getId())) // 팀이름 중복 !
                .isInstanceOf(TeamNameDuplicateException.class)
                .hasMessage("팀 이름이 이미 존재합니다.");
    }

    @Test
    @DisplayName(value = "팀조회 실패 ( 회원 팀 이미 존재 )")
    void createTeam_fail_AlreadyJoinedTeam() throws Exception {
        // given
        Member member = new Member("userA", "1234");

        Member savedMember = memberService.save(member);
        teamService.createTeam("teamA", savedMember.getId()); // 팀이름이 teamA 인 팀을 생성.

        // when && then
        assertThatThrownBy(() -> teamService.createTeam("teamB", savedMember.getId())) // 이미 팀이 존재하는 멤버가 팀을 생성
                .isInstanceOf(AlreadyJoinedTeamException.class)
                .hasMessage("팀이 이미 존재합니다.");
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

    // 팀조회 , 로그인한 멤버 팀 , 팀장확인, 팀이름 중복
    @Test
    @DisplayName(value = "팀이름 변경 실패 ( 팀 조회 실패 )")
    void updateTeamName_fail_notFoundTeam() throws Exception {
        // given
        Member member = new Member("userA", "1234");
        Member savedMember = memberService.save(member);
        teamService.createTeam("teamA", savedMember.getId());

        // when && then
        assertThatThrownBy(() -> teamService.updateTeamName(344L, "teamB", savedMember.getId()))
                .isInstanceOf(TeamNotFoundException.class)
                .hasMessage("ID로, 팀을 조회하지 못했습니다.");
    }

    @Test
    @DisplayName(value = "팀이름 변경 실패 (로그인한 멤버의 팀이 아님 )")
    void updateTeamName_fail_notLoginMemberTeam() throws Exception {
        // given
        Member member = new Member("userA", "1234");
        Member memberB = new Member("userB", "1234");
        Member savedMember = memberService.save(member);
        Member savedMemberB = memberService.save(memberB);
        Team savedTeam = teamService.createTeam("teamA", savedMember.getId());

        // when && then
        assertThat(savedMemberB.getTeam()).isNull();
        assertThat(savedMember.getTeam()).isEqualTo(savedTeam);
        assertThatThrownBy(() -> teamService.updateTeamName(savedTeam.getId(), "teamB", savedMemberB.getId()))
                .isInstanceOf(TeamNotFoundException.class)
                .hasMessage("leaderMemberId로, 팀을 조회하지 못했습니다.");
    }

    @Test
    @DisplayName(value = "팀이름 변경 실패 (변경 팀 이름 중복)")
    void updateTeamName_fail_notTeamLeader() throws Exception {
        // given
        Member member = new Member("userA", "1234");
        Member memberB = new Member("userB", "1234");
        Member savedMember = memberService.save(member);
        Member savedMemberB = memberService.save(memberB);
        Team savedTeam = teamService.createTeam("teamA", savedMember.getId());
        Team savedTeamB = teamService.createTeam("teamB", savedMemberB.getId());

        // when && then
        // teamA -> teamB 로 변경
        assertThatThrownBy(() -> teamService.updateTeamName(savedTeam.getId(), "teamB", savedMember.getId()))
                .isInstanceOf(DuplicateTeamNameException.class)
                .hasMessage("팀 이름이 이미 존재합니다.");
    }

    //TODO : 팀장 확인은, 팀원 구현이후 확인 !!!



}
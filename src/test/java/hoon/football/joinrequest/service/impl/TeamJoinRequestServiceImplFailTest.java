package hoon.football.joinrequest.service.impl;

import hoon.football.joinrequest.exception.exceptions.DuplicateTeamJoinRequestException;
import hoon.football.joinrequest.service.TeamJoinRequestService;
import hoon.football.member.domain.Member;
import hoon.football.member.exception.exceptions.AlreadyJoinedTeamException;
import hoon.football.member.exception.exceptions.MemberNotFoundException;
import hoon.football.member.service.MemberService;
import hoon.football.team.domain.Team;
import hoon.football.team.exception.exceptions.TeamNotFoundException;
import hoon.football.team.service.TeamService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TeamJoinRequestServiceImplFailTest {

    @Autowired TeamJoinRequestService tjrService;
    @Autowired MemberService memberService;
    @Autowired TeamService teamService;

    @Test
    @DisplayName(value = "요청생성 실패 ( 멤버조회 실패 )")
    void createRequest_fail_memberNotFound() throws Exception {
        // given
        Member member = memberService.save(new Member("memberA", "1234"));
        Team team = teamService.createTeam("teamA", member.getId()); // memberA -> teamA 팀 생성

        // when && then
        Assertions.assertThatThrownBy(() -> tjrService.createRequest(9999L, team.getId()))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessage("[ID(pk)] 조회실패");
    }

    @Test
    @DisplayName(value = "요청생성 실패 ( 팀 조회 실패 )")
    void createRequest_fail_teamNotFound() throws Exception {
        // given
        Member member = memberService.save(new Member("memberA", "1234"));
        Team team = teamService.createTeam("teamA", member.getId()); // memberA -> teamA 팀 생성

        Member requestMember = memberService.save(new Member("memberB", "1234")); // memberB -> 조회되지않는 team 에 가입신청
        // when && then
        Assertions.assertThatThrownBy(() -> tjrService.createRequest(requestMember.getId(), 999L))
                .isInstanceOf(TeamNotFoundException.class)
                .hasMessage("ID로, 팀을 조회하지 못했습니다.");
    }

    @Test
    @DisplayName(value = "요청생성 실패 ( 팀이 이미 존재하는경우 )")
    void createRequest_fail_AlreadyJoinTeam() throws Exception {
        // given
        Member member = memberService.save(new Member("memberA", "1234"));
        Team team = teamService.createTeam("teamA", member.getId()); // memberA -> teamA 팀 생성

        Member memberB = memberService.save(new Member("memberB", "1234"));
        Team teamB = teamService.createTeam("teamB", memberB.getId());

        // 팀이 이미 존재하는 memberA -> teamB 에 가입신청넣는상황.
        // when && then
        Assertions.assertThatThrownBy(() -> tjrService.createRequest(member.getId(), teamB.getId()))
                .isInstanceOf(AlreadyJoinedTeamException.class)
                .hasMessage("팀이 이미 존재합니다.");
    }

    @Test
    @DisplayName(value = "요청생성 실패 ( 자신이 속한 팀에 가입신청한 경우 )")
    void createRequest_fail_RequestSelfTeam() throws Exception {
        // given
        Member member = memberService.save(new Member("memberA", "1234"));
        Team team = teamService.createTeam("teamA", member.getId()); // memberA -> teamA 팀 생성

        Member memberB = memberService.save(new Member("memberB", "1234"));
        Team teamB = teamService.createTeam("teamB", memberB.getId());

        // teamB 에 속해있는 memberB -> teamB 에 가입신청.
        // when && then
        Assertions.assertThatThrownBy(() -> tjrService.createRequest(memberB.getId(), teamB.getId()))
                .isInstanceOf(AlreadyJoinedTeamException.class)
                .hasMessage("자신의 팀에는 가입신청을 할 수 없습니다.");
    }

    @Test
    @DisplayName(value = "요청생성 실패 ( 이미 신청한 팀에 또다시 가입신청한 경우 )")
    void createRequest_fail_AlreadyRequest() throws Exception {
        // given
        Member member = memberService.save(new Member("memberA", "1234"));
        Team team = teamService.createTeam("teamA", member.getId()); // memberA -> teamA 팀 생성

        Member memberB = memberService.save(new Member("memberB", "1234"));
        tjrService.createRequest(memberB.getId(), team.getId());


        // memberB -> teamA 에 가입신청을 넣고, 또 다시 memberB -> teamA 에 가입신청을 넣은상황 ( 중복요청 )
        // when && then
        Assertions.assertThatThrownBy(() -> tjrService.createRequest(memberB.getId(), team.getId()))
                .isInstanceOf(DuplicateTeamJoinRequestException.class)
                .hasMessage("이미 신청한 팀입니다.");
    }

    // memberA 가 팀장이지만, 수락버튼 누른사람이 memberC 라고 가정
    @Test
    @DisplayName(value = "요청수락 실패 ( 수락버튼 누른 유저가 해당팀의 팀장이 아닌 경우_ 해당 팀 멤버도 아닌사람)")
    void acceptRequest_fail_notTeamLeaderAndNotTeamMember() throws Exception {
        // given
        Member memberA = memberService.save(new Member("memberA", "1234"));
        Member memberB = memberService.save(new Member("memberB", "1234"));
        Member memberC = memberService.save(new Member("memberC", "1234"));
        Team team = teamService.createTeam("teamA", memberA.getId()); // memberA -> leaderMember
        tjrService.createRequest(memberB.getId(), team.getId()); // memberB -> teamA 에 가입신청.

        // when && then || 팀장이 아닌, memberC -> 가입신청 수락을 누름
        Assertions.assertThatThrownBy(() -> tjrService.acceptRequest(memberB.getId(), team.getId(), memberC.getId()))
                .isInstanceOf(NotTeamLeaderException.class)
                .hasMessage("해당 팀 팀장이 아닙니다.");
    }

    // memberA 가 팀장이지만, 수락버튼 누른사람이 memberC 라고 가정
    @Test
    @DisplayName(value = "요청수락 실패 ( 수락버튼 누른 유저가 해당팀의 팀장이 아닌 경우_ 해당 팀 멤버이지만, 팀장이 아님)")
    void acceptRequest_fail_notTeamLeader() throws Exception {
        // given
        Member memberA = memberService.save(new Member("memberA", "1234"));
        Member memberB = memberService.save(new Member("memberB", "1234"));
        Member memberC = memberService.save(new Member("memberC", "1234"));
        Team team = teamService.createTeam("teamA", memberA.getId()); // memberA -> leaderMember
        tjrService.createRequest(memberB.getId(), team.getId()); // memberB -> teamA 에 가입신청.
        tjrService.createRequest(memberC.getId(), team.getId()); // memberC -> teamA 에 가입신청.
        tjrService.acceptRequest(memberC.getId(), team.getId(), memberA.getId()); // memberA -> memberC 가입신청 수락 -> memberC.team = teamA

        // when && then || 팀장이 아닌, memberC -> 가입신청 수락을 누름
        Assertions.assertThatThrownBy(() -> tjrService.acceptRequest(memberB.getId(), team.getId(), memberC.getId()))
                .isInstanceOf(NotTeamLeaderException.class)
                .hasMessage("해당 팀 팀장이 아닙니다.");
    }

    // memberA 가 팀장이지만, 수락버튼 누른사람이 memberC 라고 가정
    @Test
    @DisplayName(value = "요청거절 실패 ( 수락버튼 누른 유저가 해당팀의 팀장이 아닌 경우_ 해당 팀 멤버도 아닌사람)")
    void rejectRequest_fail_notTeamLeaderAndNotTeamMember() throws Exception {
        // given
        Member memberA = memberService.save(new Member("memberA", "1234"));
        Member memberB = memberService.save(new Member("memberB", "1234"));
        Member memberC = memberService.save(new Member("memberC", "1234"));
        Team team = teamService.createTeam("teamA", memberA.getId()); // memberA -> leaderMember
        tjrService.createRequest(memberB.getId(), team.getId()); // memberB -> teamA 에 가입신청.

        // when && then || 팀장이 아닌, memberC -> 가입신청 수락을 누름
        Assertions.assertThatThrownBy(() -> tjrService.rejectRequest(memberB.getId(), team.getId(), memberC.getId()))
                .isInstanceOf(NotTeamLeaderException.class)
                .hasMessage("해당 팀 팀장이 아닙니다.");
    }

    // memberA 가 팀장이지만, 수락버튼 누른사람이 memberC 라고 가정
    @Test
    @DisplayName(value = "요청거절 실패 ( 수락버튼 누른 유저가 해당팀의 팀장이 아닌 경우_ 해당 팀 멤버이지만, 팀장이 아님)")
    void rejectRequest_fail_notTeamLeader() throws Exception {
        // given
        Member memberA = memberService.save(new Member("memberA", "1234"));
        Member memberB = memberService.save(new Member("memberB", "1234"));
        Member memberC = memberService.save(new Member("memberC", "1234"));
        Team team = teamService.createTeam("teamA", memberA.getId()); // memberA -> leaderMember
        tjrService.createRequest(memberB.getId(), team.getId()); // memberB -> teamA 에 가입신청.
        tjrService.createRequest(memberC.getId(), team.getId()); // memberC -> teamA 에 가입신청.
        tjrService.acceptRequest(memberC.getId(), team.getId(), memberA.getId()); // memberA -> memberC 가입신청 수락 -> memberC.team = teamA

        // when && then || 팀장이 아닌, memberC -> 가입신청 수락을 누름
        Assertions.assertThatThrownBy(() -> tjrService.rejectRequest(memberB.getId(), team.getId(), memberC.getId()))
                .isInstanceOf(NotTeamLeaderException.class)
                .hasMessage("해당 팀 팀장이 아닙니다.");
    }


}

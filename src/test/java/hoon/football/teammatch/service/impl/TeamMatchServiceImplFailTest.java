package hoon.football.teammatch.service.impl;

import hoon.football.joinrequest.domain.TeamJoinRequest;
import hoon.football.joinrequest.service.TeamJoinRequestService;
import hoon.football.member.domain.Member;
import hoon.football.member.exception.exceptions.MemberNotFoundException;
import hoon.football.member.service.MemberService;
import hoon.football.team.domain.Team;
import hoon.football.team.exception.exceptions.NotTeamLeaderException;
import hoon.football.team.exception.exceptions.NotTeamMemberException;
import hoon.football.team.exception.exceptions.TeamNotFoundException;
import hoon.football.team.service.TeamService;
import hoon.football.teammatch.domain.TeamMatch;
import hoon.football.teammatch.exception.exceptions.DuplicateTeamMatchRequestException;
import hoon.football.teammatch.exception.exceptions.NotFoundTeamMatchException;
import hoon.football.teammatch.exception.exceptions.TeamMatchAcceptToSelfTeamException;
import hoon.football.teammatch.service.TeamMatchService;
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
class TeamMatchServiceImplFailTest {

    @Autowired TeamMatchService teamMatchService;
    @Autowired MemberService memberService;
    @Autowired TeamService teamService;
    @Autowired TeamJoinRequestService teamJoinRequestService;

    @Test
    @DisplayName(value = "매칭등록 실패 ( 멤버조회 실패 )")
    void createMatch_fail_notFoundMember() throws Exception {
        // given
        Member member = memberService.save(new Member("memberA", "1234"));
        Team team = teamService.createTeam("teamA", member.getId()); // memberA -> teamA 생성

        // when && then
        assertThatThrownBy(() -> teamMatchService.createTeamMatch(team.getId(), 999L))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessage("멤버 조회하는데 실패했습니다.");
    }

    @Test
    @DisplayName(value = "매칭등록 실패 ( 팀 조회 실패 )")
    void createMatch_fail_notFoundTeam() throws Exception {
        // given
        Member member = memberService.save(new Member("memberA", "1234"));
        Team team = teamService.createTeam("teamA", member.getId());

        // when && then
        assertThatThrownBy(() -> teamMatchService.createTeamMatch(333L, member.getId()))
                .isInstanceOf(TeamNotFoundException.class)
                .hasMessage("팀을 조회하는데 실패했습니다.");
    }

    @Test
    @DisplayName(value = "매칭등록 실패 ( 해당 팀 멤버아님 )")
    void createMatch_fail_notJoinedMember() throws Exception {
        // given
        Member member = memberService.save(new Member("memberA", "1234"));
        Member memberB = memberService.save(new Member("memberB", "1234"));

        Team team = teamService.createTeam("teamA", member.getId());
        Team teamB = teamService.createTeam("teamB", memberB.getId());
        // when && then
        assertThatThrownBy(() -> teamMatchService.createTeamMatch(team.getId(), memberB.getId())) // memberB -> teamA 매칭등록 -> 실패
                .isInstanceOf(NotTeamMemberException.class)
                .hasMessage("다른팀 소속이잖아");
    }

    @Test
    @DisplayName(value = "매칭등록 실패 ( 해당 팀 팀장아님 )")
    void createMatch_fail_notTeamLeader() throws Exception {
        // given
        Member member = memberService.save(new Member("memberA", "1234"));
        Team team = teamService.createTeam("teamA", member.getId());

        Member memberB = memberService.save(new Member("memberB", "1234"));
        teamJoinRequestService.createRequest(memberB.getId(), team.getId());
        teamJoinRequestService.acceptRequest(memberB.getId(), team.getId(), member.getId()); // memberB -> teamA 가입성공

        // when && then
        assertThatThrownBy(() -> teamMatchService.createTeamMatch(team.getId(), memberB.getId())) // memberB -> teamA 매칭등록 -> 실패
                .isInstanceOf(NotTeamLeaderException.class)
                .hasMessage("팀장아닌데요.");
    }

    @Test
    @DisplayName(value = "매치수락 실패 ( 매치 조회 실패 )")
    void acceptMatch_fail_notFoundMatch() throws Exception {
        // given
        Member member = memberService.save(new Member("memberA", "1234"));
        Member memberB = memberService.save(new Member("memberB", "1234"));

        Team team = teamService.createTeam("teamA", member.getId());
        Team teamB = teamService.createTeam("teamB", memberB.getId());

        teamMatchService.createTeamMatch(team.getId(), member.getId()); // memberA -> 매치등록.

        // when && then
        assertThatThrownBy(() -> teamMatchService.acceptTeamMatch(888L, teamB.getId(), member.getId()))
                .isInstanceOf(NotFoundTeamMatchException.class)
                .hasMessage("팀매치 조회하는데 실패했습니다.");
    }

    @Test
    @DisplayName(value = "매치수락 실패 ( 원정팀 조회 실패 )")
    void acceptMatch_fail_notFoundAwayTeam() throws Exception {
        // given
        Member member = memberService.save(new Member("memberA", "1234"));
        Team team = teamService.createTeam("teamA", member.getId());

        Member memberB = memberService.save(new Member("memberB", "1234"));
        Team teamB = teamService.createTeam("teamB", memberB.getId());

        TeamMatch match = teamMatchService.createTeamMatch(team.getId(), member.getId());

        // when && then
        assertThatThrownBy(() -> teamMatchService.acceptTeamMatch(match.getId(), 999L, member.getId()))
                .isInstanceOf(TeamNotFoundException.class)
                .hasMessage("팀을 조회하는데 실패했습니다.");
    }

    @Test
    @DisplayName(value = "같은매치에 중복요청")
    void duplicate_matchRequest() throws Exception {
        // given
        Member memberA = memberService.save(new Member("memberA", "1234"));
        Member memberB = memberService.save(new Member("memberB", "1234"));

        Team teamA = teamService.createTeam("teamA", memberA.getId());// memberA -> teamA
        Team teamB = teamService.createTeam("teamB", memberB.getId()); // memberB -> teamB

        TeamMatch match = teamMatchService.createTeamMatch(teamA.getId(), memberA.getId()); // memberA , teamA -> 매칭등록
        teamMatchService.acceptTeamMatchRequest(match.getId(), teamB.getId(), memberB.getId()); // memberB , teamB -> teamA 가 올린 매칭에 수락요청

        // when && then
        assertThatThrownBy(() -> teamMatchService.acceptTeamMatchRequest(match.getId(), teamB.getId(), memberB.getId())) // 중복요청
                .isInstanceOf(DuplicateTeamMatchRequestException.class)
                .hasMessage("같은 매치에 중복요청을 보낼 수 없습니다.");
    }

    @Test
    @DisplayName(value = "자기팀에 매치요청 ( 팀원이 요청 )")
    void matchRequestToSelfTeam_member() throws Exception {
        // given
        Member memberA = memberService.save(new Member("memberA", "1234"));
        Member memberB = memberService.save(new Member("memberB", "1234"));

        Team teamA = teamService.createTeam("teamA", memberA.getId());// memberA -> teamA
        TeamJoinRequest joinRequest = teamJoinRequestService.createRequest(memberB.getId(), teamA.getId());// memberB -> teamA 에 가입요청.
        joinRequest.accept(memberB, teamA); // memberA -> memberB teamA 에 가입 승인.

        TeamMatch match = teamMatchService.createTeamMatch(teamA.getId(), memberA.getId()); // memberA , teamA -> 매칭등록

        // when && then
        assertThatThrownBy(() -> teamMatchService.acceptTeamMatchRequest(match.getId(), teamA.getId(), memberB.getId()))
                .isInstanceOf(NotTeamLeaderException.class)
                .hasMessage("팀장아닌데요.");
    }

    @Test
    @DisplayName(value = "자기팀에 매치요청 ( 팀장이 요청 )")
    void matchRequestToSelfTeam_leader() throws Exception {
        // given
        Member memberA = memberService.save(new Member("memberA", "1234"));
        Member memberB = memberService.save(new Member("memberB", "1234"));

        Team teamA = teamService.createTeam("teamA", memberA.getId());// memberA -> teamA
        TeamJoinRequest joinRequest = teamJoinRequestService.createRequest(memberB.getId(), teamA.getId());// memberB -> teamA 에 가입요청.
        joinRequest.accept(memberB, teamA); // memberA -> memberB teamA 에 가입 승인.

        TeamMatch match = teamMatchService.createTeamMatch(teamA.getId(), memberA.getId()); // memberA , teamA -> 매칭등록

        // when && then
        assertThatThrownBy(() -> teamMatchService.acceptTeamMatchRequest(match.getId(), teamA.getId(), memberA.getId()))
                .isInstanceOf(TeamMatchAcceptToSelfTeamException.class)
                .hasMessage("자신의 팀에는 매치요청을 보낼 수 없습니다.");
    }








}
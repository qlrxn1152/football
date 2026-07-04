package hoon.football.teammatch.service.impl;

import hoon.football.member.domain.Member;
import hoon.football.member.service.MemberService;
import hoon.football.team.domain.Team;
import hoon.football.team.service.TeamService;
import hoon.football.teammatch.domain.TeamMatch;
import hoon.football.teammatch.domain.TeamMatchRequest;
import hoon.football.teammatch.domain.TeamMatchStatus;
import hoon.football.teammatch.service.TeamMatchService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TeamMatchServiceImplSuccessTest {

    @Autowired TeamMatchService teamMatchService;
    @Autowired MemberService memberService;
    @Autowired TeamService teamService;

    @Test
    @DisplayName(value = "매칭등록 성공")
    void createMatch_success() throws Exception {
        // given
        Member member = memberService.save(new Member("memberA", "1234"));
        Team team = teamService.createTeam("teamA", member.getId());

        // when
        TeamMatch match = teamMatchService.createTeamMatch(team.getId(), member.getId()); // memberA -> 매칭등록

        // then
        assertThat(match.getAwayTeam()).isNull();
        assertThat(match.getHomeTeam()).isEqualTo(team);
        assertThat(match.getStatus()).isEqualTo(TeamMatchStatus.PENDING);
    }

    @Test
    @DisplayName(value = "등록된 매칭에 수락요청 보냄 성공")
    void matchAcceptRequest_success() throws Exception {
        // given
        Member memberA = memberService.save(new Member("memberA", "1234"));
        Member memberB = memberService.save(new Member("memberB", "1234"));

        Team teamA = teamService.createTeam("teamA", memberA.getId()); // memberA -> teamA 생성
        Team teamB = teamService.createTeam("teamB", memberB.getId()); // memberB -> teamB 생성

        TeamMatch match = teamMatchService.createTeamMatch(teamA.getId(), memberA.getId());// memberA , teamA -> 매칭등록

        // when
        TeamMatchRequest matchRequest = teamMatchService.acceptTeamMatchRequest(match.getId(), teamB.getId(), memberB.getId());// memberA, teamB -> memberA, teamA 가 올린 매칭에 수락요청을 보냄

        // then
        assertThat(match.getStatus()).isEqualTo(TeamMatchStatus.PENDING);
        assertThat(match.getAwayTeam()).isNull();
        assertThat(match.getHomeTeam()).isEqualTo(teamA);
        assertThat(matchRequest.getTeamMatch()).isEqualTo(match);
        assertThat(matchRequest.getHomeTeam()).isEqualTo(teamA);
        assertThat(matchRequest.getAwayTeam()).isEqualTo(teamB);
    }

    @Test
    @DisplayName(value = "매칭 정상적으로 잡힘")
    void matchAccept_success() throws Exception {
        // given
        Member memberA = memberService.save(new Member("memberA", "1234"));
        Member memberB = memberService.save(new Member("memberB", "1234"));

        Team teamA = teamService.createTeam("teamA", memberA.getId()); // memberA -> teamA 생성
        Team teamB = teamService.createTeam("teamB", memberB.getId()); // memberB -> teamB 생성

        TeamMatch match = teamMatchService.createTeamMatch(teamA.getId(), memberA.getId()); // memberA , teamA -> 매칭등록
        TeamMatchRequest request = teamMatchService.acceptTeamMatchRequest(match.getId(), teamB.getId(), memberB.getId());// memberA, teamB -> memberA, teamA 가 올린 매칭에 수락요청을 보냄

        // when
        teamMatchService.acceptTeamMatch(match.getId(), teamB.getId(), memberA.getId()); // teamA, memberA 가 teamB, memberB 가 보낸 요청을 수락 -> 매칭잡힘
        List<TeamMatch> pendingMatches = teamMatchService.findPendingMatch(); // matches ( 매치들 보여줄때 사용하는 메서드 )

        // then
        assertThat(match.getStatus()).isEqualTo(TeamMatchStatus.MATCHED);
        assertThat(match.getAwayTeam()).isEqualTo(teamB);
        assertThat(pendingMatches).hasSize(0);
        assertThat(request.getTeamMatch()).isEqualTo(match);
        assertThat(request.getHomeTeam()).isEqualTo(teamA);
        assertThat(request.getAwayTeam()).isEqualTo(teamB);
    }

    @Test
    @DisplayName(value = "매치결과 입력 성공")
    void resultTeamMatch() throws Exception {
        // given
        Member memberA = memberService.save(new Member("memberA", "1234"));
        Member memberB = memberService.save(new Member("memberB", "1234"));
        Team teamA = teamService.createTeam("teamA", memberA.getId());
        Team teamB = teamService.createTeam("teamB", memberB.getId());
        TeamMatch match = teamMatchService.createTeamMatch(teamA.getId(), memberA.getId()); // teamA, memberA -> 매칭등록
        teamMatchService.acceptTeamMatchRequest(match.getId(), teamB.getId(), memberB.getId()); // teamB, memberB -> teamA, memberA 가 등록한 매칭에 수락요청보냄.
        teamMatchService.acceptTeamMatch(match.getId(), teamB.getId(), memberA.getId()); // memberA -> teamB 요청 수락 => 매칭잡힘

        // when
        teamMatchService.resultTeamMatch(match.getId(), 3, 1); // teamA 3 : 1 teamB 로, teamA 팀장인 memberA 가 결과를 입력함.

        // then
        assertThat(teamA.getRating()).isEqualTo(1030);
        assertThat(teamB.getRating()).isEqualTo(970);
        assertThat(match.getStatus()).isEqualTo(TeamMatchStatus.COMPLETED);
    }

    @Test
    @DisplayName(value = "status = PENDING 인 매치들 조회 성공")
    void findPendingMatches_success() throws Exception {
        // given
        Member memberA = memberService.save(new Member("memberA", "1234"));
        Member memberB = memberService.save(new Member("memberB", "1234"));
        Member memberC = memberService.save(new Member("memberC", "1234"));

        Team teamA = teamService.createTeam("teamA", memberA.getId());
        Team teamB = teamService.createTeam("teamB", memberB.getId());
        Team teamC = teamService.createTeam("teamC", memberC.getId());

        TeamMatch matchA = teamMatchService.createTeamMatch(teamA.getId(), memberA.getId());
        TeamMatch matchB = teamMatchService.createTeamMatch(teamB.getId(), memberB.getId()); // 이거는 teamB 의 새로운 매칭요청
        TeamMatch matchC = teamMatchService.createTeamMatch(teamC.getId(), memberC.getId());

        // when
        matchA.acceptMatch(teamB); // teamA < -> teamB 매칭 성공

        List<TeamMatch> pendingMatches = teamMatchService.findPendingMatch();

        // then
        assertThat(matchA.getStatus()).isEqualTo(TeamMatchStatus.MATCHED);
        assertThat(matchB.getStatus()).isEqualTo(TeamMatchStatus.PENDING);

        assertThat(pendingMatches).hasSize(2);
        assertThat(pendingMatches).contains(matchB, matchC);
    }

}
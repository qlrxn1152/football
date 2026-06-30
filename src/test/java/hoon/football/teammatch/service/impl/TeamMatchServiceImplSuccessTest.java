package hoon.football.teammatch.service.impl;

import hoon.football.member.domain.Member;
import hoon.football.member.service.MemberService;
import hoon.football.team.domain.Team;
import hoon.football.team.service.TeamService;
import hoon.football.teammatch.domain.TeamMatch;
import hoon.football.teammatch.domain.TeamMatchStatus;
import hoon.football.teammatch.service.TeamMatchService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

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
    @DisplayName(value = "매칭 성공 _ (변경감지도 정상작동)")
    void acceptMatch_success() throws Exception {
        // given
        Member member = memberService.save(new Member("memberA", "1234"));
        Team team = teamService.createTeam("teamA", member.getId());
        Member memberB = memberService.save(new Member("memberB", "1234"));
        Team teamB = teamService.createTeam("teamB", memberB.getId());

        TeamMatch match = teamMatchService.createTeamMatch(team.getId(), member.getId()); // memberA -> 매칭등록
        // when
        TeamMatch acceptMatch = teamMatchService.acceptTeamMatch(match.getId(), teamB.getId());

        // then
        assertThat(match.getAwayTeam()).isNotNull();
        assertThat(match.getHomeTeam()).isEqualTo(team);
        assertThat(match.getAwayTeam()).isEqualTo(teamB);
        assertThat(match.getStatus()).isEqualTo(TeamMatchStatus.MATCHED);

        /*
        assertThat(acceptMatch.getAwayTeam()).isNotNull();
        assertThat(acceptMatch.getHomeTeam()).isEqualTo(team);
        assertThat(acceptMatch.getAwayTeam()).isEqualTo(teamB);
        assertThat(acceptMatch.getStatus()).isEqualTo(TeamMatchStatus.MATCHED);
         */
    }

}
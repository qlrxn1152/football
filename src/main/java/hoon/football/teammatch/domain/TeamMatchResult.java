package hoon.football.teammatch.domain;

import hoon.football.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @NoArgsConstructor
public class TeamMatchResult {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_match_result_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_match_id", nullable = false, unique = true)
    private TeamMatch teamMatch; // homeTeam, awayTeam 을 찾아가기 위한..

    private int homeScore;
    private int awayScore;

    // 나중에, 경기가 끝나고 일장시간이 지났음에도 결과를 입력하지않으면 어떻게할건지 ..
    public void inputMatchResult(TeamMatch teamMatch, int homeScore, int awayScore) {
        this.teamMatch = teamMatch;
        this.homeScore = homeScore;
        this.awayScore = awayScore;

        teamMatch.completeMatch();
        checkScoreAndReflectMatchResult(teamMatch, homeScore, awayScore);
    }

    private static void checkScoreAndReflectMatchResult(TeamMatch teamMatch, int homeScore, int awayScore) {
        if (homeScore == awayScore) {
            // 무승부 => 점수 + 10
            teamMatch.getHomeTeam().matchResultIsDraw();
            teamMatch.getAwayTeam().matchResultIsDraw();
        }

        // 승리팀 => 점수 + 30 , 패배팀 => 점수 - 30
        if ( homeScore > awayScore) {
            teamMatch.getHomeTeam().matchResultIsWin();
            teamMatch.getAwayTeam().matchResultIsLose();
        }

        if ( homeScore < awayScore) {
            teamMatch.getHomeTeam().matchResultIsLose();
            teamMatch.getAwayTeam().matchResultIsWin();
        }
    }
}

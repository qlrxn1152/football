package hoon.football.teammatch.domain;

import hoon.football.team.domain.Team;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter @NoArgsConstructor
public class TeamMatchRequest {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_match_request_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_match_id", nullable = false)
    private TeamMatch teamMatch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_team_id", nullable = false)
    private Team homeTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "away_team_id", nullable = false)
    private Team awayTeam;

    @Enumerated(EnumType.STRING)
    @Column(name = "match_request_status")
    private TeamMatchRequestStatus matchRequestStatus;

    public TeamMatchRequest(TeamMatch teamMatch, Team homeTeam, Team awayTeam) {
        this.teamMatch = teamMatch;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.matchRequestStatus = TeamMatchRequestStatus.PENDING;
    }

    public void matched() {
        this.matchRequestStatus = TeamMatchRequestStatus.MATCHED;
    }

    public void rejected() {
        this.matchRequestStatus = TeamMatchRequestStatus.REJECTED;
    }

}

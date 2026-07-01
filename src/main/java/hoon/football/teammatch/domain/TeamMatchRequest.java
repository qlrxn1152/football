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

    @JoinColumn(name = "away_team_id", nullable = false)
    private Team awayTeam;

    public TeamMatchRequest(Team awayTeam) {
        this.awayTeam = awayTeam;
    }
}

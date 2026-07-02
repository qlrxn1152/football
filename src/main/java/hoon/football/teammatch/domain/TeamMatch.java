package hoon.football.teammatch.domain;

import hoon.football.team.domain.Team;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class TeamMatch {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_match_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_team_id", nullable = false)
    private Team homeTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "away_team_id", nullable = true)
    private Team awayTeam;

    @Enumerated(EnumType.STRING)
    @Column(name = "team_match_status")
    private TeamMatchStatus status;

    // 매칭 등록 시점
    private LocalDateTime requestAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "teamMatch")
    private List<TeamMatchRequest> matchRequests = new ArrayList<>();


    // homeTeam 파라미터를 받는 생성자를 호출 -> 매칭등록 => awayTeam = null
    public TeamMatch(Team homeTeam) {
        this.homeTeam = homeTeam;
        this.status = TeamMatchStatus.PENDING;
        this.requestAt = LocalDateTime.now();
    }

    // acceptMatch -> 매칭성공 ==> awayTeam 값설정, status = MATCHED
    public void acceptMatch(Team awayTeam) {
        this.awayTeam = awayTeam;
        this.status = TeamMatchStatus.MATCHED;
    }

    // expireMatch -> 매칭요청하고 시간이 너무지나서 요청을 EXPIRED ...
    public void expireMatch() {
        this.status = TeamMatchStatus.EXPIRED;
    }

    public void completeMatch() {
        this.status = TeamMatchStatus.COMPLETED;
    }

}

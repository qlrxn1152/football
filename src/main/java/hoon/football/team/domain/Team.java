package hoon.football.team.domain;

import hoon.football.joinrequest.domain.TeamJoinRequest;
import hoon.football.member.domain.Member;
import hoon.football.teammatch.domain.TeamMatchRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Team {

    private static final Integer TEAM_RATING_VALUE = 1000;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id", nullable = false)
    private Long id;

    @Column(unique = true, nullable = false, name = "team_name")
    private String teamName;

    @Column(name = "team_rating", nullable = false)
    private Integer rating;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_member_id", nullable = false)
    private Member leaderMember;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "team")
    private List<Member> members = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "team")
    private List<TeamJoinRequest> teamJoinRequests = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "awayTeam")
    private List<TeamMatchRequest> teamMatchRequests = new ArrayList<>();

    public Team(String teamName, Member leaderMember) {
        this.teamName = teamName;
        this.leaderMember = leaderMember;
        this.rating = TEAM_RATING_VALUE;
    }

    public void changeTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void changeLeaderMember(Member leaderMember) {
        this.leaderMember = leaderMember;
    }


    public void matchResultIsDraw() {
        this.rating += 10;
    }

    public void matchResultIsWin() {
        this.rating += 30;
    }

    public void matchResultIsLose() {
        this.rating -= 30;
    }



}

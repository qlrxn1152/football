package hoon.football.team.domain;

import hoon.football.joinrequest.domain.TeamJoinRequest;
import hoon.football.member.domain.Member;
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

    public Team(String teamName, Member leaderMember) {
        this.teamName = teamName;
        this.leaderMember = leaderMember;
        this.rating = TEAM_RATING_VALUE;
    }

    public void changeTeamName(String teamName) {
        this.teamName = teamName;
    }



}

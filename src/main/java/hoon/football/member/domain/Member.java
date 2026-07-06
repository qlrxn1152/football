package hoon.football.member.domain;

import hoon.football.joinrequest.domain.TeamJoinRequest;
import hoon.football.member.exception.exceptions.AlreadyJoinedTeamException;
import hoon.football.team.domain.Team;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "rating"})
public class Member {

    private static final Integer MEMBER_RATING_VALUE = 1000;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true, nullable = false, length = 10)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(name = "member_rating", nullable = false)
    private Integer rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "team_role", nullable = false, length = 20)
    private TeamRole teamRole;

    // 어떤팀에 가입신청
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    private List<TeamJoinRequest> requests = new ArrayList<>();








    public Member(String username, String password) {
        this.username = username;
        this.password = password;
        this.rating = MEMBER_RATING_VALUE;
        this.teamRole = TeamRole.NONE;
    }

    public void createTeamAsLeaderMember(Team team) {
        this.team = team;
        this.teamRole = TeamRole.LEADER;
        team.getMembers().add(this);
        team.changeLeaderMember(this);
    }

    public void joinTeam(Team team) {
        this.team = team;
        this.teamRole = TeamRole.MEMBER;
        team.getMembers().add(this);
    }


}

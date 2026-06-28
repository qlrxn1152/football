package hoon.football.member.domain;

import hoon.football.member.exception.exceptions.AlreadyJoinedTeamException;
import hoon.football.team.domain.Team;
import jakarta.persistence.*;
import lombok.*;

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

    public Member(String username, String password) {
        this.username = username;
        this.password = password;
        this.rating = MEMBER_RATING_VALUE;
    }

    public void createTeamAsLeaderMember(Team team) {
        this.team = team;
    }


}

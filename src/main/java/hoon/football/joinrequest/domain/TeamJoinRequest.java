package hoon.football.joinrequest.domain;

import hoon.football.member.domain.Member;
import hoon.football.team.domain.Team;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class TeamJoinRequest {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_join_request_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Enumerated(EnumType.STRING)
    private TeamJoinRequestStatus status;

    @Column(name = "request_at")
    private LocalDateTime requestAt;

    public TeamJoinRequest(Member member, Team team) {
        this.member = member;
        this.team = team;
        this.status = TeamJoinRequestStatus.PENDING;
        this.requestAt = LocalDateTime.now();

        team.getTeamJoinRequests().add(this);
    }

    public void accept(Member member, Team team) {
        this.status = TeamJoinRequestStatus.ACCEPTED;
        member.joinTeam(team);
        team.getTeamJoinRequests().remove(this);
    }

    public void reject() {
//        team.getTeamJoinRequests().remove(this); --> 이 코드가 필요? 정책은 ? ==> 즉, 거절하면 가입신청도 없앨거냐?
        this.status = TeamJoinRequestStatus.REJECTED;
    }

}

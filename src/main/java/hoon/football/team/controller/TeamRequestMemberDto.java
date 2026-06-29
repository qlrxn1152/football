package hoon.football.team.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
public class TeamRequestMemberDto {

    private Long requestMemberId;
    private String requestMemberUsername;
    private Integer requestMemberRating;
    private LocalDateTime requestAt;

    public TeamRequestMemberDto(Long requestMemberId, String requestMemberUsername, Integer requestMemberRating, LocalDateTime requestAt) {
        this.requestMemberId = requestMemberId;
        this.requestMemberUsername = requestMemberUsername;
        this.requestMemberRating = requestMemberRating;
        this.requestAt = requestAt;
    }
}

package hoon.football.teammatch.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class AcceptRequestDto {

    private Long matchId;
    private Long awayTeamId;
    private String awayTeamName;
    private int awayTeamRating;
    private String awayTeamLeaderMemberUsername;

    public AcceptRequestDto(Long matchId, Long awayTeamId, String awayTeamName, int awayTeamRating, String awayTeamLeaderMemberUsername) {
        this.matchId = matchId;
        this.awayTeamId = awayTeamId;
        this.awayTeamName = awayTeamName;
        this.awayTeamRating = awayTeamRating;
        this.awayTeamLeaderMemberUsername = awayTeamLeaderMemberUsername;
    }
}

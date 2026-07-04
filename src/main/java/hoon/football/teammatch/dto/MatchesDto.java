package hoon.football.teammatch.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MatchesDto {

    private Long matchId;
    private Long awayTeamId;
    private String awayTeamName;
    private int awayTeamRating;
    private String awayTeamLeaderMemberUsername;

    public MatchesDto(Long matchId, Long awayTeamId, String awayTeamName, int awayTeamRating, String awayTeamLeaderMemberUsername) {
        this.matchId = matchId;
        this.awayTeamId = awayTeamId;
        this.awayTeamName = awayTeamName;
        this.awayTeamRating = awayTeamRating;
        this.awayTeamLeaderMemberUsername = awayTeamLeaderMemberUsername;
    }
}

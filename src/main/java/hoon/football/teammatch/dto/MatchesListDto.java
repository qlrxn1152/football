package hoon.football.teammatch.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class MatchesListDto {

    private Long matchId;
    private Long homeTeamId;
    private String homeTeamName;
    private int homeTeamRating;
    private String homeTeamLeaderMemberUsername;

    public MatchesListDto(Long matchId, Long homeTeamId, String homeTeamName, int homeTeamRating, String homeTeamLeaderMemberUsername) {
        this.matchId = matchId;
        this.homeTeamId = homeTeamId;
        this.homeTeamName = homeTeamName;
        this.homeTeamRating = homeTeamRating;
        this.homeTeamLeaderMemberUsername = homeTeamLeaderMemberUsername;
    }
}

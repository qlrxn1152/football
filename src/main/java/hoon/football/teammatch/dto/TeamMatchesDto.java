package hoon.football.teammatch.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class TeamMatchesDto {

    private Long homeTeamId; // => ?? => 팀상세로 이동해야하니까 필요할거같은데 ...
    private Integer homeTeamRating;
    private String homeTeamName;

    public TeamMatchesDto(Long homeTeamId, Integer homeTeamRating, String homeTeamName) {
        this.homeTeamId = homeTeamId;
        this.homeTeamRating = homeTeamRating;
        this.homeTeamName = homeTeamName;
    }

}

package hoon.football.teammatch.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MatchResultDto {

    private Integer homeScore;
    private Integer awayScore;

    public MatchResultDto(Integer homeScore, Integer awayScore) {
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }
}

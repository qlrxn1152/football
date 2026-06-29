package hoon.football.team.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class TeamDetailDto {

    private Long teamId;
    private String teamName;
    private Integer rating;
    private String leaderMemberUsername;

    public TeamDetailDto(Long teamId, String teamName, Integer rating, String leaderMemberUsername) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.rating = rating;
        this.leaderMemberUsername = leaderMemberUsername;
    }
}

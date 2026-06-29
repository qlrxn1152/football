package hoon.football.team.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class TeamListDto {

    private Long id;
    private String teamName;
    private Integer rating;
    private String leaderMemberUsername;

    public TeamListDto(Long id, String teamName, Integer rating, String leaderMemberUsername) {
        this.id = id;
        this.teamName = teamName;
        this.rating = rating;
        this.leaderMemberUsername = leaderMemberUsername;
    }
}

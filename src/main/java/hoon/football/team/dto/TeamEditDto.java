package hoon.football.team.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class TeamEditDto {

    private String teamName;

    public TeamEditDto(String teamName) {
        this.teamName = teamName;
    }

}

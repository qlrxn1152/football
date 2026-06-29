package hoon.football.team.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class TeamMemberDto {

    private String username;
    private Integer memberRating;

    public TeamMemberDto(String username, Integer memberRating) {
        this.username = username;
        this.memberRating = memberRating;
    }
}

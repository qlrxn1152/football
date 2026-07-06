package hoon.football.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class MemberHomeDto {

    private String username;
    private Integer rating;
    private String teamName;

    public MemberHomeDto(String username, Integer rating, String teamName) {
        this.username = username;
        this.rating = rating;
        this.teamName = teamName;
    }
}

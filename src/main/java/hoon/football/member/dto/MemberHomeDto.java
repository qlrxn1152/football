package hoon.football.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class MemberHomeDto {

    private String username;
    private Integer rating;

    public MemberHomeDto(String username, Integer rating) {
        this.username = username;
        this.rating = rating;
    }
}

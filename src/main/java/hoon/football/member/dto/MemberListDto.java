package hoon.football.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class MemberListDto {

    private Long id;
    private String username;
    private Integer rating;

    public MemberListDto(Long id, String username, Integer rating) {
        this.id = id;
        this.username = username;
        this.rating = rating;
    }
}

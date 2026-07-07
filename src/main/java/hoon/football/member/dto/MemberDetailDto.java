package hoon.football.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class MemberDetailDto {

    private Long teamId;
    private String teamName;
    private Integer memberRating;
    private String username;

    public MemberDetailDto(Long teamId, String teamName, Integer memberRating, String username) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.memberRating = memberRating;
        this.username = username;
    }

    public MemberDetailDto(Integer memberRating, String username) {
        this.memberRating = memberRating;
        this.username = username;
    }
}

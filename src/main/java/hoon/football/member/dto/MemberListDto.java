package hoon.football.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class MemberListDto {

    private Long memberId;
    private String username;
    private Integer rating;

    public MemberListDto(Long memberId, String username, Integer rating) {
        this.memberId = memberId;
        this.username = username;
        this.rating = rating;
    }
}

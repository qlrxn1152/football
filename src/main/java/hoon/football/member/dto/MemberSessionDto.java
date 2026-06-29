package hoon.football.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class MemberSessionDto {

    private Long loginMemberId;
    private String username;

    public MemberSessionDto(Long loginMemberId, String username) {
        this.loginMemberId = loginMemberId;
        this.username = username;
    }
}

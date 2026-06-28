package hoon.football.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class MemberSessionDto {

    private Long id;
    private String username;

    public MemberSessionDto(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}

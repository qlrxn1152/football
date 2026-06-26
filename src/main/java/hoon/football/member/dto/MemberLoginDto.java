package hoon.football.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class MemberLoginDto {

    private String username;
    private String password;

    public MemberLoginDto(String username, String password) {
        this.username = username;
        this.password = password;
    }


}

package hoon.football.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberLoginDto {

    private String username;
    private String password;

    public MemberLoginDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

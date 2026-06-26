package hoon.football.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberSaveDto {

    private String username;
    private String password;

    public MemberSaveDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

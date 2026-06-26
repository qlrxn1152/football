package hoon.football.member.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    private static final Integer MEMBER_RATING_VALUE = 1000;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String username;
    private String password;

    @Column(name = "member_rating")
    private Integer rating;

    public Member(String username, String password) {
        this.username = username;
        this.password = password;
        this.rating = MEMBER_RATING_VALUE;
    }


}

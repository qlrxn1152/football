package hoon.football.teammatch.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class TeamMatchesDto {
    // 매칭팀들 보여주는 페이지에서 사용할 dto
    private Long homeTeamId; // => ?? => 팀상세로 이동해야하니까 필요할거같은데 ...
    private Integer homeTeamRating;
    private String homeTeamName;
    private String homeTeamLeaderMemberUsername;

    public TeamMatchesDto(Long homeTeamId, Integer homeTeamRating, String homeTeamName, String homeTeamLeaderMemberUsername) {
        this.homeTeamId = homeTeamId;
        this.homeTeamRating = homeTeamRating;
        this.homeTeamName = homeTeamName;
        this.homeTeamLeaderMemberUsername = homeTeamLeaderMemberUsername;
    }

}

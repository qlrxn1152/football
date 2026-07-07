package hoon.football.team.controller;

import hoon.football.team.dto.TeamListDto;
import hoon.football.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TeamApiController {

    private final TeamService teamService;

    @GetMapping("/api/teams")
    public List<TeamListDto> teams() {
        return teamService.findAll()
                .stream()
                .map(team -> new TeamListDto(
                        team.getId(),
                        team.getTeamName(),
                        team.getRating(),
                        team.getLeaderMember().getUsername()
                ))
                .toList();
    }
}

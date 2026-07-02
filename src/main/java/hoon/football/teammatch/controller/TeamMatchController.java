package hoon.football.teammatch.controller;

import hoon.football.member.service.MemberService;
import hoon.football.team.service.TeamService;
import hoon.football.teammatch.domain.TeamMatch;
import hoon.football.teammatch.dto.MatchesListDto;
import hoon.football.teammatch.repository.TeamMatchRepository;
import hoon.football.teammatch.service.TeamMatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class TeamMatchController {

    private final MemberService memberService;
    private final TeamService teamService;
    private final TeamMatchService teamMatchService;
    private final TeamMatchRepository teamMatchRepository;

    @GetMapping("/matches")
    public String matchesForm(Model model) {
        // status = PENDING 인 매치들 조회해서 보내줌
        List<MatchesListDto> pendingMatches = teamMatchService.findPendingMatch()
                .stream()
                .map(match -> new MatchesListDto(match.getId(),match.getHomeTeam().getId(), match.getHomeTeam().getTeamName(), match.getHomeTeam().getRating(), match.getHomeTeam().getLeaderMember().getUsername()))
                .toList();

        model.addAttribute("pendingMatches", pendingMatches);

        return "matches/matchesList";
    }
}

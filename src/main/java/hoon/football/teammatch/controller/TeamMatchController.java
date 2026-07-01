package hoon.football.teammatch.controller;

import hoon.football.member.repository.MemberRepository;
import hoon.football.member.service.MemberService;
import hoon.football.team.repository.TeamRepository;
import hoon.football.team.service.TeamService;
import hoon.football.teammatch.domain.TeamMatch;
import hoon.football.teammatch.dto.TeamMatchesDto;
import hoon.football.teammatch.repository.TeamMatchRepository;
import hoon.football.teammatch.service.TeamMatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor

public class TeamMatchController {

    private final MemberService memberService;
    private final TeamService teamService;
    private final TeamMatchService teamMatchService;

    // 수락대기중인 매치들을 볼 수 있는 ...
    // matchStatus = PENDING,
    @GetMapping("/matchs")
    public String matchesForm(Model model) {
        // 수락대기중인 매치들을 가지고옴 => matchStatus = PENDING 인 teamMatch ..
        // teamMatchService.findPendingMatch(); => 반환값인 list 가 비어져있을경우 체크

        List<TeamMatchesDto> pendingMatches = teamMatchService.findPendingMatch()
                .stream()
                .map(pendingMatch -> new TeamMatchesDto(
                        pendingMatch.getHomeTeam().getId(),
                        pendingMatch.getHomeTeam().getRating(),
                        pendingMatch.getHomeTeam().getTeamName(),
                        pendingMatch.getHomeTeam().getLeaderMember().getUsername())
                )
                .toList();

        model.addAttribute("pendingMatches", pendingMatches);

        return "matches/matchsList";
    }


}

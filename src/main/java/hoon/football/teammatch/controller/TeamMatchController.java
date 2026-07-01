package hoon.football.teammatch.controller;

import hoon.football.member.domain.Member;
import hoon.football.member.domain.TeamRole;
import hoon.football.member.dto.MemberSessionDto;
import hoon.football.member.repository.MemberRepository;
import hoon.football.member.service.MemberService;
import hoon.football.team.domain.Team;
import hoon.football.team.repository.TeamRepository;
import hoon.football.team.service.TeamService;
import hoon.football.teammatch.domain.TeamMatch;
import hoon.football.teammatch.domain.TeamMatchRequest;
import hoon.football.teammatch.dto.TeamMatchesDto;
import hoon.football.teammatch.repository.TeamMatchRepository;
import hoon.football.teammatch.service.TeamMatchService;
import hoon.football.web.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    @GetMapping("/matches")
    public String matchesForm(Model model) {
        // 수락대기중인 매치들을 가지고옴 => matchStatus = PENDING 인 teamMatch ..
        // teamMatchService.findPendingMatch(); => 반환값인 list 가 비어져있을경우 체크

        List<TeamMatchesDto> pendingMatches = teamMatchService.findPendingMatch()
                .stream()
                .map(pendingMatch -> new TeamMatchesDto(
                        pendingMatch.getId(),
                        pendingMatch.getHomeTeam().getId(),
                        pendingMatch.getHomeTeam().getRating(),
                        pendingMatch.getHomeTeam().getTeamName(),
                        pendingMatch.getHomeTeam().getLeaderMember().getUsername())
                )
                .toList();

        model.addAttribute("pendingMatches", pendingMatches);

        return "matches/matchesList";
    }

    @PostMapping("/teams/{teamId}/match-requests")
    public String matchRequest(@PathVariable Long teamId, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) MemberSessionDto loginMember, RedirectAttributes redirectAttributes) {
        teamMatchService.createTeamMatch(teamId, loginMember.getLoginMemberId());

        redirectAttributes.addFlashAttribute("successMessage", "매칭 등록에 성공했습니다.");
        return "redirect:/matches";
    }

    // 버튼눌러서 여기로 PostMapping -> 이후에, 홈팀 팀장이 수락할건지? 말건지?
    @PostMapping("/matches/{matchId}/accept")
    @ResponseBody
    public String matchAccept(@PathVariable Long matchId, RedirectAttributes redirectAttributes, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) MemberSessionDto loginMember) {
        teamMatchService.requestTeamMatch(matchId, loginMember.getLoginMemberId());
        return "OK";
    }

    // 홈팀에서 수락 할지 말지 결정할수있는 ... /// 컨트롤러, 기능들 구현

    // 수락 -> teamMatchService.acceptTeamMatch()
//        teamMatchService.acceptTeamMatch(matchId, ) // 마지막 파라미터인 loginMemberId -> 홈팀 팀장..




}

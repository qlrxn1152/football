package hoon.football.teammatch.controller;

import hoon.football.member.domain.Member;
import hoon.football.member.dto.MemberSessionDto;
import hoon.football.member.service.MemberService;
import hoon.football.team.service.TeamService;
import hoon.football.teammatch.domain.TeamMatch;
import hoon.football.teammatch.domain.TeamMatchResult;
import hoon.football.teammatch.dto.MatchResultDto;
import hoon.football.teammatch.dto.MatchesListDto;
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
    private final TeamMatchRepository teamMatchRepository;

    @GetMapping("/matches")
    public String matchesForm(Model model) {
        // status = PENDING 인 매치들 조회해서 보내줌
        List<MatchesListDto> pendingMatches = teamMatchService.findPendingMatch()
                .stream()
                .map(match -> new MatchesListDto(match.getId(), match.getHomeTeam().getId(), match.getHomeTeam().getTeamName(), match.getHomeTeam().getRating(), match.getHomeTeam().getLeaderMember().getUsername()))
                .toList();

        model.addAttribute("pendingMatches", pendingMatches);

        return "matches/matchesList";
    }

    @PostMapping("/matches/new/{teamId}")
    public String createMatch(@PathVariable Long teamId, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) MemberSessionDto sessionMember) {
        Member loginMember = memberService.findById(sessionMember.getLoginMemberId());
        teamMatchService.createTeamMatch(teamId, loginMember.getId());

        return "redirect:/matches";
    }

    @PostMapping("/matches/{matchId}/request")
    public String requestTeamMatchRequest(@PathVariable Long matchId, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) MemberSessionDto sessionMember, RedirectAttributes redirectAttributes) {
        TeamMatch teamMatch = teamMatchService.findMatchById(matchId);
        Member loginMember = memberService.findById(sessionMember.getLoginMemberId());

        teamMatchService.acceptTeamMatchRequest(matchId, loginMember.getTeam().getId(), loginMember.getId()); // awayTeamId... -> loginMember TeamId

        redirectAttributes.addFlashAttribute("successMessage", "매칭요청에 성공했습니다.");
        return "redirect:/matches";
    }

    @PostMapping("/matches/{matchId}/accept/{awayTeamId}")
    public String acceptMatchRequest(@PathVariable Long matchId, @PathVariable Long awayTeamId, RedirectAttributes redirectAttributes, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) MemberSessionDto sessionMember) {
        Member loginMember = memberService.findById(sessionMember.getLoginMemberId());

        TeamMatch teamMatch = teamMatchService.acceptTeamMatch(matchId, awayTeamId, loginMember.getId());
        Long homeTeamId = teamMatch.getHomeTeam().getId();

        redirectAttributes.addFlashAttribute("successMessage", "매칭 성공!");

        return "redirect:/teams/" + homeTeamId;
    }

    @GetMapping("/matches/{matchId}/result/{awayTeamId}")
    public String matchResultForm(@ModelAttribute MatchResultDto matchResultDto) {
        return "matches/matchResult";
    }

    @PostMapping("/matches/{matchId}/result/{awayTeamId}")
    public String matchResult(@ModelAttribute MatchResultDto matchResultDto, @PathVariable Long matchId, @PathVariable Long awayTeamId) {
        // 팀장이 점수를 입력하고, 결과입력 버튼을 누름 ->
        TeamMatch match = teamMatchService.findMatchById(matchId);

        teamMatchService.resultTeamMatch(matchId, matchResultDto.getHomeScore(), matchResultDto.getAwayScore());

        return "redirect:/teams/" + match.getHomeTeam().getId();
    }
}

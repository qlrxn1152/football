package hoon.football.team.controller;

import hoon.football.joinrequest.service.TeamJoinRequestService;
import hoon.football.member.dto.MemberSessionDto;
import hoon.football.member.service.MemberService;
import hoon.football.team.domain.Team;
import hoon.football.team.dto.*;
import hoon.football.team.service.TeamService;
import hoon.football.teammatch.domain.TeamMatchRequest;
import hoon.football.teammatch.domain.TeamMatchStatus;
import hoon.football.teammatch.dto.AcceptRequestDto;
import hoon.football.teammatch.repository.TeamMatchRequestRepository;
import hoon.football.teammatch.service.TeamMatchService;
import hoon.football.web.SessionConst;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final MemberService memberService;
    private final TeamJoinRequestService teamJoinRequestService;
    private final TeamMatchService teamMatchService;
    private final TeamMatchRequestRepository teamMatchRequestRepository;

    private final EntityManager em;

    @GetMapping("/teams/new")
    public String teamSaveForm(@ModelAttribute TeamSaveDto teamSaveDto) {
        return "teams/saveTeam";
    }

    // loginMember -> null 일 가능성도 체크
    @PostMapping("/teams/new")
    public String teamSave(@ModelAttribute TeamSaveDto teamSaveDto, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) MemberSessionDto loginMember, RedirectAttributes redirectAttributes) {
        teamService.createTeam(teamSaveDto.getTeamName(), loginMember.getLoginMemberId());
        redirectAttributes.addFlashAttribute("successMessage", "팀 생성 성공!");

        return "redirect:/teams";
    }

    @GetMapping("/teams")
    public String teamsForm(Model model) {
        List<TeamListDto> dtoTeams = teamService.findAll().stream()
                .map(team -> new TeamListDto(team.getId(), team.getTeamName(), team.getRating(), team.getLeaderMember().getUsername()))
                .toList();

        model.addAttribute("teams", dtoTeams);
        return "teams/list";
    }

    @GetMapping("/teams/{teamId}")
    public String teamDetailForm(@PathVariable Long teamId, Model model) {
        TeamDetailDto findTeamDto = teamToTeamDetailDto(teamId);
        List<TeamMemberDto> membersDto = memberToTeamMemberDto(teamId);
        List<TeamRequestMemberDto> requestsDto = requestToTeamRequestMemberDto(teamId);

        // 요청팀이름, 요청팀 점수, 요청팀 팀장 // 수락 , 거절 버튼 // homeTeam 에 들어온 요청들 ... => teamMatchRepository 에서 homeTeam = teamId 로 있는거 다 가지고오면됨
        List<AcceptRequestDto> matchRequests = teamMatchRequestRepository.findByHomeTeamId(teamId) // => 해당팀에 있는 모든 요청들을 다 가지고옴 ...==> X => 대기중인 매칭들 ....... //// 파라미터에 matchId ?
                .stream()
                .map(matchRequest -> new AcceptRequestDto(
                        matchRequest.getTeamMatch().getId(),
                        matchRequest.getAwayTeam().getId(),
                        matchRequest.getAwayTeam().getTeamName(),
                        matchRequest.getAwayTeam().getRating(),
                        matchRequest.getAwayTeam().getLeaderMember().getUsername())
                )
                .toList();

        model.addAttribute("team", findTeamDto);
        model.addAttribute("members", membersDto);
        model.addAttribute("requests", requestsDto);
        model.addAttribute("matchRequests", matchRequests);
        return "teams/detail";
    }

    @GetMapping("/teams/{teamId}/edit")
    public String teamEditForm(@PathVariable Long teamId, @ModelAttribute TeamEditDto teamEditDto) {
        return "teams/edit";
    }

    @PostMapping("/teams/{teamId}/edit")
    public String teamEdit(@PathVariable Long teamId, @ModelAttribute TeamEditDto teamEditDto, RedirectAttributes redirectAttributes, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) MemberSessionDto loginMember) {
        teamService.updateTeamName(teamId, teamEditDto.getTeamName(), loginMember.getLoginMemberId());
        redirectAttributes.addFlashAttribute("successMessage", "팀 이름 변경 성공.");
        return "redirect:/teams";
    }

    // 비즈니스 로직
    private @NonNull List<TeamRequestMemberDto> requestToTeamRequestMemberDto(Long teamId) {
        return teamJoinRequestService.findAllRequestsByTeamId(teamId)
                .stream()
                .map(request -> new TeamRequestMemberDto(request.getMember().getId(), request.getMember().getUsername(), request.getMember().getRating(), request.getRequestAt()))
                .toList();
    }
    private @NonNull List<TeamMemberDto> memberToTeamMemberDto(Long teamId) {
        return memberService.findByTeamId(teamId)
                .stream()
                .map(member -> new TeamMemberDto(member.getUsername(), member.getRating()))
                .toList();
    }

    private @NonNull TeamDetailDto teamToTeamDetailDto(Long teamId) {
        Team findTeam = teamService.findDetailTeamByTeamId(teamId); // id, teamName, teamRating, teamLeaderMemberUsername, teamMatchRequests...

        return new TeamDetailDto(findTeam.getId(), findTeam.getTeamName(), findTeam.getRating(), findTeam.getLeaderMember().getUsername());
    }

}

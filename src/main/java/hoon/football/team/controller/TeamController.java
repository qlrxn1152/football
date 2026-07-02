package hoon.football.team.controller;

import hoon.football.joinrequest.service.TeamJoinRequestService;
import hoon.football.member.dto.MemberSessionDto;
import hoon.football.member.service.MemberService;
import hoon.football.team.domain.Team;
import hoon.football.team.dto.*;
import hoon.football.team.service.TeamService;
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

        model.addAttribute("team", findTeamDto);
        model.addAttribute("members", membersDto);
        model.addAttribute("requests", requestsDto);
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

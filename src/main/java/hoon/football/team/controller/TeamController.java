package hoon.football.team.controller;

import hoon.football.joinrequest.domain.TeamJoinRequest;
import hoon.football.joinrequest.service.TeamJoinRequestService;
import hoon.football.member.domain.Member;
import hoon.football.member.dto.MemberSessionDto;
import hoon.football.member.service.MemberService;
import hoon.football.team.domain.Team;
import hoon.football.team.dto.TeamEditDto;
import hoon.football.team.dto.TeamSaveDto;
import hoon.football.team.service.TeamService;
import hoon.football.web.SessionConst;
import jakarta.persistence.EntityManager;
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
public class TeamController {

    private final TeamService teamService;
    private final MemberService memberService;
    private final TeamJoinRequestService teamJoinRequestService;

    private final EntityManager em;

    @GetMapping("/teams/new")
    public String teamSaveForm(@ModelAttribute TeamSaveDto teamSaveDto) {
        return "teams/saveTeam";
    }

    // loginMember -> null 일 가능성도 체크
    @PostMapping("/teams/new")
    public String teamSave(@ModelAttribute TeamSaveDto teamSaveDto, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) MemberSessionDto loginMember, RedirectAttributes redirectAttributes) {
        teamService.createTeam(teamSaveDto.getTeamName(), loginMember.getId());
        redirectAttributes.addFlashAttribute("successMessage", "팀 생성 성공!");

        return "redirect:/teams";
    }

    @GetMapping("/teams")
    public String teamsForm(Model model) {
        model.addAttribute("teams", teamService.findAll());

        return "teams/list";
    }

    // Entity 대신, Dto 를 만들어서 model 에 넣을수있도록 Refactoring
    @GetMapping("/teams/{teamId}")
    public String teamDetailForm(@PathVariable Long teamId, Model model) {
        Team findTeam = teamService.findDetailTeamByTeamId(teamId);
        List<TeamJoinRequest> requests = teamJoinRequestService.findAllRequestsByTeamId(teamId);

        model.addAttribute("team", findTeam);
        model.addAttribute("requests", requests);
        return "teams/detail";
    }

    @GetMapping("/teams/{teamId}/edit")
    public String teamEditForm(@PathVariable Long teamId, @ModelAttribute TeamEditDto teamEditDto) {
        return "teams/edit";
    }

    @PostMapping("/teams/{teamId}/edit")
    public String teamEdit(@PathVariable Long teamId, @ModelAttribute TeamEditDto teamEditDto, RedirectAttributes redirectAttributes, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) MemberSessionDto loginMember) {
        teamService.updateTeamName(teamId, teamEditDto.getTeamName(), loginMember.getId());
        redirectAttributes.addFlashAttribute("successMessage", "팀 이름 변경 성공.");
        return "redirect:/teams";
    }


}

package hoon.football.team.controller;

import hoon.football.member.domain.Member;
import hoon.football.member.service.MemberService;
import hoon.football.team.dto.TeamSaveDto;
import hoon.football.team.service.TeamService;
import hoon.football.web.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final MemberService memberService;

    @GetMapping("/teams/new")
    public String teamSaveForm(@ModelAttribute TeamSaveDto teamSaveDto) {
        return "teams/saveTeam";
    }

    @PostMapping("/teams/new")
    public String teamSave(@ModelAttribute TeamSaveDto teamSaveDto, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, RedirectAttributes redirectAttributes) {
        teamService.createTeam(teamSaveDto.getTeamName(), loginMember.getId());
        redirectAttributes.addFlashAttribute("successMessage", "팀 생성 성공!");

        return "redirect:/teams";
    }

    @GetMapping("/teams")
    public String teamsForm(Model model) {
        model.addAttribute("teams", teamService.findAll());

        return "teams/list";
    }


}

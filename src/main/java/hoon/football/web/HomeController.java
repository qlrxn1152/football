package hoon.football.web;

import hoon.football.member.domain.Member;
import hoon.football.member.dto.MemberHomeDto;
import hoon.football.member.dto.MemberLoginDto;
import hoon.football.member.dto.MemberSessionDto;
import hoon.football.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final MemberService memberService;

    @GetMapping("/")
    public String home(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) MemberSessionDto loginMember, Model model) {
        if (loginMember == null) {
            return "home";
        }

        Member findMember = memberService.findById(loginMember.getLoginMemberId());
        MemberHomeDto member = new MemberHomeDto(findMember.getUsername(), findMember.getRating(), findMember.getTeam().getTeamName()); // 여기서, team 을 가지고올때 쿼리가 1번 더 나감.

        model.addAttribute("member", member);
        model.addAttribute("teamId", findMember.getTeam().getId());
        return "loginHome";
    }

}

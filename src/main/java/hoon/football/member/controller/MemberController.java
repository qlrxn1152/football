package hoon.football.member.controller;

import hoon.football.member.domain.Member;
import hoon.football.member.dto.MemberLoginDto;
import hoon.football.member.dto.MemberSaveDto;
import hoon.football.member.exception.*;
import hoon.football.member.service.MemberService;
import hoon.football.web.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String saveMemberForm(@ModelAttribute MemberSaveDto memberSaveDto) {
        return "members/saveMember";
    }

    @PostMapping("/members/new")
    public String saveMember(@ModelAttribute MemberSaveDto memberSaveDto) {
        Member member = new Member(memberSaveDto.getUsername(), memberSaveDto.getPassword());
        memberService.save(member);

        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginForm(@ModelAttribute MemberLoginDto memberLoginDto) {
        return "members/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute MemberLoginDto memberLoginDto, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Member loginMember = memberService.login(memberLoginDto.getUsername(), memberLoginDto.getPassword());
        request.getSession().setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        request.getSession(false).invalidate();
        return "redirect:/";
    }

    @GetMapping("/members")
    public String membersForm(Model model) {

        model.addAttribute("members", memberService.findAll());
        return "members/list";
    }


}

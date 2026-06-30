package hoon.football.member.controller;

import hoon.football.member.domain.Member;
import hoon.football.member.dto.MemberListDto;
import hoon.football.member.dto.MemberLoginDto;
import hoon.football.member.dto.MemberSaveDto;
import hoon.football.member.dto.MemberSessionDto;
import hoon.football.member.exception.*;
import hoon.football.member.service.MemberService;
import hoon.football.web.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

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
    public String loginForm(@RequestParam(name = "redirectURI", defaultValue = "/") String redirectURI, @ModelAttribute MemberLoginDto memberLoginDto, Model model) {
        model.addAttribute("redirectURI", redirectURI);
        log.info("[GET /login] redirectURI={}", redirectURI);
        return "members/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute MemberLoginDto memberLoginDto, HttpServletRequest request, @RequestParam(name = "redirectURI", defaultValue = "/") String redirectURI) {
        log.info("[POST /login] redirectURI={}", redirectURI);
        Member loginMember = memberService.login(memberLoginDto.getUsername(), memberLoginDto.getPassword());

        // 세션 전용 memberDto 저장
        request.getSession().setAttribute(SessionConst.LOGIN_MEMBER, new MemberSessionDto(loginMember.getId(), loginMember.getUsername()));

        return "redirect:" + redirectURI;
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "redirect:/";
        }

        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/members")
    public String membersForm(Model model) {
        List<MemberListDto> dtoMembers = memberService.findAll().stream()
                .map(member -> new MemberListDto(member.getId(), member.getUsername(), member.getRating()))
                .toList();

        // id, username, rating
        model.addAttribute("members", dtoMembers);
        return "members/list";
    }


}

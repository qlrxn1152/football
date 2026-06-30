package hoon.football.teammatch.controller;

import hoon.football.member.repository.MemberRepository;
import hoon.football.team.repository.TeamRepository;
import hoon.football.teammatch.repository.TeamMatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequiredArgsConstructor

public class TeamMatchController {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamMatchRepository teamMatchRepository;

    @GetMapping("/matchs")
    @ResponseBody
    public String matchesForm() {
        return "O";
    }
}

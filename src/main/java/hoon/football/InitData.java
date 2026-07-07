package hoon.football;

import hoon.football.member.domain.Member;
import hoon.football.member.service.MemberService;
import hoon.football.team.service.TeamService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitData {

    private final MemberService memberService;
    private final TeamService teamService;

//    @PostConstruct
    public void initForMember(){
        memberService.save(new Member("member1", "1234"));
        memberService.save(new Member("member2", "1234"));
        memberService.save(new Member("member3", "1234"));
        memberService.save(new Member("member4", "1234"));
        memberService.save(new Member("member5", "1234"));
        memberService.save(new Member("member6", "1234"));
        memberService.save(new Member("member7", "1234"));
        memberService.save(new Member("member8", "1234"));
        memberService.save(new Member("member9", "1234"));
        memberService.save(new Member("member10", "1234"));
        memberService.save(new Member("member11", "1234"));
        memberService.save(new Member("member12", "1234"));
    }

}

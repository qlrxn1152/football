package hoon.football.team.service.impl;

import hoon.football.member.repository.MemberRepository;
import hoon.football.team.repository.TeamRepository;
import hoon.football.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;


}

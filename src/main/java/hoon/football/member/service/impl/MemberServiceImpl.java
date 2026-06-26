package hoon.football.member.service.impl;

import hoon.football.member.domain.Member;
import hoon.football.member.exception.DuplicateUsernameException;
import hoon.football.member.exception.MemberNotFoundException;
import hoon.football.member.exception.MemberPasswordLengthException;
import hoon.football.member.exception.MemberUsernameLengthException;
import hoon.football.member.repository.MemberRepository;
import hoon.football.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository repository;

    @Override
    public Member save(Member member) {
        validateMemberSave(member);

        return repository.save(member);
    }

    @Override
    @Transactional(readOnly = true)
    public Member findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("[ID(pk)] 조회실패"));
    }

    /**
     * DB 에 username 을 가진 Member 가 존재하는지를 판단.
     * @param username
     * @return 존재 -> Member / 존재 X -> MemberNotFoundException
     */
    @Override
    @Transactional(readOnly = true)
    public Member findByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new MemberNotFoundException("[USERNAME] 중복"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Member> findAll() {
        return repository.findAll();
    }



    private void validateMemberSave(Member member) {
        // 아이디가 이미 존재한다면, 가입실패
        Optional<Member> optMember = repository.findByUsername(member.getUsername());
        if (optMember.isPresent()) {
            throw new DuplicateUsernameException("[USERNAME] 중복");
        }

        // 아이디 길이 조건을 충족하지못한다면, 가입실패 ( 최소 4글자 ~ 최대 10글자까지만 허용. )
        if (member.getUsername().length() < 4 || member.getUsername().length() > 10) {
            throw new MemberUsernameLengthException("[USERNAME] 길이조건 미충족");
        }

        // 비밀번호 길이 조건을 충족하지못한다면, 가입실패 ( 최소 4글자 ~ 최대 15글자까지만 허용. )
        if (member.getPassword().length() < 4 || member.getPassword().length() > 15) {
            throw new MemberPasswordLengthException("[PASSWORD] 길이조건 미충족");
        }

    }
}

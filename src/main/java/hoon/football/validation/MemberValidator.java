package hoon.football.validation;

import hoon.football.member.exception.exceptions.DuplicateUsernameException;
import hoon.football.member.exception.exceptions.MemberPasswordLengthException;
import hoon.football.member.exception.exceptions.MemberUsernameLengthException;
import hoon.football.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberValidator {

    private final MemberRepository memberRepository;

    public void validateForSignUp(String username, String password) {
        validateUsernameLength(username);
        validatePasswordLength(password);
        validateDuplicateUsername(username);
    }

    private void validateUsernameLength(String username) {
        if (username.length() < 4 || username.length() > 10) {
            throw new MemberUsernameLengthException("[USERNAME] 길이 조건 미충족");
        }
    }

    private void validatePasswordLength(String password) {
        // 비밀번호 길이 조건을 충족하지못한다면, 가입실패 ( 최소 4글자 ~ 최대 15글자까지만 허용. )
        if (password.length() < 4 || password.length() > 15) {
            throw new MemberPasswordLengthException("[PASSWORD] 길이조건 미충족");
        }
    }

    private void validateDuplicateUsername(String username) {
        if (memberRepository.findByUsername(username).isPresent()) {
            throw new DuplicateUsernameException("[USERNAME] 중복");
        }
    }

}

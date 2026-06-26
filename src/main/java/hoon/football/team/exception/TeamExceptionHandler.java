package hoon.football.team.exception;

import hoon.football.member.exception.exceptions.MemberLoginException;
import hoon.football.team.exception.exceptions.TeamCreateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@ControllerAdvice
public class TeamExceptionHandler {

    @ExceptionHandler(TeamCreateException.class)
    public String handleTeamCreateException(TeamCreateException e, RedirectAttributes redirectAttributes) {
        log.info("[TeamCreate Exception] : {}", e.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/";
    }

}

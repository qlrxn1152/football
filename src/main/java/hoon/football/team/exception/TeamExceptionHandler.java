package hoon.football.team.exception;

import hoon.football.member.exception.exceptions.MemberLoginException;
import hoon.football.team.exception.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@ControllerAdvice
public class TeamExceptionHandler {

    @ExceptionHandler(NotTeamLeaderException.class)
    public String handleNotTeamLeaderException(NotTeamLeaderException e, RedirectAttributes redirectAttributes) {
        log.error("[NotTeamLeader Exception] : {}", e.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler(TeamCreateException.class)
    public String handleTeamCreateException(TeamCreateException e, RedirectAttributes redirectAttributes) {
        log.error("[TeamCreate Exception] : {}", e.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler(TeamNotFoundException.class)
    public String handlerTeamNotFoundException(TeamNotFoundException e, RedirectAttributes redirectAttributes) {
        log.error("[TeamNotFound Exception] : {}", e.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler(TeamNameDuplicateException.class)
    public String handlerTeamNameDuplicateException(TeamNameDuplicateException e, RedirectAttributes redirectAttributes) {
        log.error("[TeamNameDuplicate Exception] : {}", e.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler(NotTeamMemberException.class)
    public String handlerNotTeamMemberException(NotTeamMemberException e, RedirectAttributes redirectAttributes) {
        log.error("[NotTeamMember Exception] : {}", e.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/";
    }

}

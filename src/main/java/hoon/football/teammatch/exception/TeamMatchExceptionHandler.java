package hoon.football.teammatch.exception;

import hoon.football.team.exception.exceptions.TeamCreateException;
import hoon.football.team.exception.exceptions.TeamNameDuplicateException;
import hoon.football.team.exception.exceptions.TeamNotFoundException;
import hoon.football.teammatch.exception.exceptions.NotFoundTeamMatchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@ControllerAdvice
public class TeamMatchExceptionHandler {

    @ExceptionHandler(NotFoundTeamMatchException.class)
    public String handleNotFoundTeamMatchException(NotFoundTeamMatchException e, RedirectAttributes redirectAttributes) {
        log.error("[NotFoundTeamMatch Exception] : {}", e.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/";
    }



}

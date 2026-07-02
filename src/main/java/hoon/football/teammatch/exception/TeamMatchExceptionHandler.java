package hoon.football.teammatch.exception;

import hoon.football.teammatch.exception.exceptions.TeamMatchAcceptToSelfTeamException;
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

    @ExceptionHandler(TeamMatchAcceptToSelfTeamException.class)
    public String handleDuplicateTeamMatchAcceptRequestException(TeamMatchAcceptToSelfTeamException e, RedirectAttributes redirectAttributes) {
        log.error("[DuplicateTeamMatchAcceptRequest Exception] : {}", e.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/";
    }




}

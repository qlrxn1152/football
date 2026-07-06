package hoon.football.teammatch.exception;

import hoon.football.teammatch.exception.exceptions.DuplicateTeamMatchRequestException;
import hoon.football.teammatch.exception.exceptions.NotFoundTeamMatchRequestException;
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

    @ExceptionHandler(NotFoundTeamMatchRequestException.class)
    public String handleNotFoundTeamMatchRequestException(NotFoundTeamMatchRequestException e, RedirectAttributes redirectAttributes) {
        log.error("[NotFoundTeamMatchRequest Exception] : {}", e.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler(DuplicateTeamMatchRequestException.class)
    public String handleDuplicateTeamMatchRequestException(DuplicateTeamMatchRequestException e, RedirectAttributes redirectAttributes) {
        log.error("[DuplicateTeamMatchRequest Exception] : {}", e.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/";
    }




}

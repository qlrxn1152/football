package hoon.football.joinrequest.exception;

import hoon.football.joinrequest.exception.exceptions.DuplicateTeamJoinRequestException;
import hoon.football.joinrequest.exception.exceptions.NotFoundTeamJoinRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@ControllerAdvice
public class TeamJoinRequestExceptionHandler {

    @ExceptionHandler(DuplicateTeamJoinRequestException.class)
    public String handleDuplicateTeamJoinRequestException(DuplicateTeamJoinRequestException e, RedirectAttributes redirectAttributes) {
        log.error("[DuplicateTeamJoinRequest Exception] : {}", e.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/teams";
    }

    @ExceptionHandler(NotFoundTeamJoinRequestException.class)
    public String handleNotFoundTeamJoinRequestException(NotFoundTeamJoinRequestException e, RedirectAttributes redirectAttributes) {
        log.error("[NotFoundTeamJoinRequest Exception] : {}", e.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/teams";
    }




}

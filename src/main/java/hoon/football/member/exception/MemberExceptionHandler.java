package hoon.football.member.exception;

import hoon.football.member.exception.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@ControllerAdvice
public class MemberExceptionHandler {

    // 멤버관련
    @ExceptionHandler(MemberLoginException.class)
    public String handleMemberLoginException(MemberLoginException e, RedirectAttributes redirectAttributes) {
        log.info("[MemberLogin Exception] : {}", e.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/login";
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public String handleMemberNotFoundException(MemberNotFoundException e, RedirectAttributes redirectAttributes) {
        log.info("[MemberNotFound Exception] : {}", e.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/login";
    }

    @ExceptionHandler(MemberUsernameLengthException.class)
    public String handlerMemberUsernameLengthException(MemberUsernameLengthException e, RedirectAttributes redirectAttributes) {
        log.info("[USERNAME Length Exception] : {}", e.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/login";
    }

    @ExceptionHandler(MemberPasswordLengthException.class)
    public String handlerMemberPasswordLengthException(MemberPasswordLengthException e, RedirectAttributes redirectAttributes) {
        log.info("[PASSWORD Length Exception] : {}", e.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/login";
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    public String handlerDuplicateUsernameException(DuplicateUsernameException e, RedirectAttributes redirectAttributes) {
        log.info("[DUPLICATE USERNAME Exception] : {}", e.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/login";
    }


}

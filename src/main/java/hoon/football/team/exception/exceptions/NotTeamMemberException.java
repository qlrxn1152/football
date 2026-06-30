package hoon.football.team.exception.exceptions;

public class NotTeamMemberException extends RuntimeException {
    public NotTeamMemberException(String message) {
        super(message);
    }
}

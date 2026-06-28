package hoon.football.member.exception.exceptions;

public class AlreadyJoinedTeamException extends RuntimeException {
    public AlreadyJoinedTeamException(String message) {
        super(message);
    }
}

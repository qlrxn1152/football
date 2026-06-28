package hoon.football.team.exception.exceptions;

public class TeamNameDuplicateException extends RuntimeException {
    public TeamNameDuplicateException(String message) {
        super(message);
    }
}

package hoon.football.validator.teammatch;

import hoon.football.teammatch.repository.TeamMatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamMatchValidator {

    private final TeamMatchRepository teamMatchRepository;


}

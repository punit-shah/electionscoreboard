package punitshah.electionscoreboard.restapplication;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import punitshah.electionscoreboard.scoreboard.controller.ScoreboardController;
import punitshah.electionscoreboard.scoreboard.model.Constituency;
import punitshah.electionscoreboard.scoreboard.model.ConstituencyResults;
import punitshah.electionscoreboard.scoreboard.model.Party;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ScoreboardRestController {
    private ScoreboardController scoreboardController;

    public ScoreboardRestController() {
        scoreboardController = new ScoreboardController();
    }

    @PostMapping("/constituency-results")
    public ResponseEntity postConstituencyResults(@Validated @RequestBody ConstituencyResults constituencyResults) {
        if (scoreboardController.updateConstituencies(constituencyResults)) {
            scoreboardController.updateParties(constituencyResults);
            return new ResponseEntity(HttpStatus.CREATED);
        } else {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/parties")
    public List<Party> getParties() {
        return scoreboardController.getSortedPartyList();
    }

    @GetMapping("/parties/{partyCode}")
    public ResponseEntity<Party> getParty(@PathVariable(name = "partyCode") String partyCode) {
        Party party = scoreboardController.getPartyMap().get(partyCode);

        return party == null ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(party, HttpStatus.OK);
    }

    @GetMapping("/constituencies")
    public List<Constituency> getConstituencies() {
        return scoreboardController.getSortedConstituencyList();
    }

    @GetMapping("/constituencies/{constituencyId}")
    public ResponseEntity<Constituency> getConstituency(@PathVariable(name = "constituencyId") int constituencyId) {
        Constituency constituency = scoreboardController.getConstituency(constituencyId);

        return constituency == null ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(constituency, HttpStatus.OK);
    }

    @GetMapping("/scoreboard")
    public String getScoreboard() {
        List<Party> topThreeParties = scoreboardController.getTopThreeParties();
        List<Party> partiesNotInTopThree = scoreboardController.getPartiesNotInTopThree();
        Map<String, Double> share = scoreboardController.calculateShare();

        String scoreboard = "<pre>";
        scoreboard += String.format("%s\t%5s\t%s<br>", "Party", "Seats", "Share");
        scoreboard += topThreePartiesToString(topThreeParties, share) + "<br>";
        scoreboard += partiesNotInTopThreeCombinedToString(partiesNotInTopThree, share) + "<br>";
        scoreboard += "</pre>";

        return scoreboard;
    }

    private String topThreePartiesToString(List<Party> topThreeParties, Map<String, Double> share) {
        return String.join(
                "<br>",
                topThreeParties.stream()
                        .map(party -> {
                            String partyCode = party.getPartyCode();
                            int seats = party.getSeats();

                            String partyResultString = String.format(
                                    "%-6s\t%5d\t%5.1f", partyCode, seats, share.get(partyCode)
                            );

                            return party.isMajorityWon() ? partyResultString + " - Majority won" : partyResultString;
                        })
                        .collect(Collectors.toList())
        );
    }

    private String partiesNotInTopThreeCombinedToString(List<Party> partiesNotInTopThree, Map<String, Double> share) {
        double partiesNotInTopThreeCombinedShare = 0.0;

        for (Party party: partiesNotInTopThree) {
            partiesNotInTopThreeCombinedShare += share.get(party.getPartyCode());
        }

        return String.format(
                "%-6s\t%5d\t%5.1f",
                "Others",
                scoreboardController.getTotalSeats(partiesNotInTopThree),
                partiesNotInTopThreeCombinedShare
        );
    }
}

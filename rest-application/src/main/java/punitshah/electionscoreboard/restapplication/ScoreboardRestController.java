package punitshah.electionscoreboard.restapplication;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import punitshah.electionscoreboard.scoreboard.controller.ScoreboardController;
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
    public void postConstituencyResults(@Validated @RequestBody ConstituencyResults constituencyResults) {
        scoreboardController.updateParties(constituencyResults);
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

    @GetMapping("/parties")
    public List<Party> getParties() {
        return scoreboardController.getPartyList();
    }

    @GetMapping("/parties/{partyCode}")
    public ResponseEntity<Party> getParty(@PathVariable(name = "partyCode") String partyCode) {
        return scoreboardController.getPartyList().stream()
                .filter(party -> party.getPartyCode().equals(partyCode))
                .findFirst()
                .map(party -> new ResponseEntity<>(party, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
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

                            return seats >= 326 ? partyResultString + " - Majority won" : partyResultString;
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

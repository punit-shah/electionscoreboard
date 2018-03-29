package punitshah.electionscoreboard.restapplication;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import punitshah.electionscoreboard.scoreboard.controller.ScoreboardController;
import punitshah.electionscoreboard.scoreboard.model.Constituency;
import punitshah.electionscoreboard.scoreboard.model.ConstituencyResults;
import punitshah.electionscoreboard.scoreboard.model.Party;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ScoreboardRestController {
    private ScoreboardController scoreboardController;

    public ScoreboardRestController() {
        scoreboardController = new ScoreboardController();
    }

    @PostMapping("/constituency-results")
    public ResponseEntity postConstituencyResults(@Validated @RequestBody ConstituencyResults constituencyResults) {
        try {
            if (scoreboardController.updateConstituencies(constituencyResults)) {
                return new ResponseEntity(HttpStatus.CREATED);
            } else {
                return new ResponseEntity(HttpStatus.CONFLICT);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    @GetMapping("/parties")
    public List<Party> getParties() {
        try {
            return scoreboardController.getSortedPartyList();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/parties/{partyCode}")
    public ResponseEntity<Party> getParty(@PathVariable(name = "partyCode") String partyCode) {
        Party party = null;
        try {
            party = scoreboardController.getPartyMap().get(partyCode);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return party == null ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(party, HttpStatus.OK);
    }

    @GetMapping("/constituencies")
    public List<Constituency> getConstituencies() {
        try {
            return scoreboardController.getConstituencyList();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/constituencies/{constituencyId}")
    public ResponseEntity<Constituency> getConstituency(@PathVariable(name = "constituencyId") int constituencyId) {
        Constituency constituency = null;
        try {
            constituency = scoreboardController.getConstituency(constituencyId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return constituency == null ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(constituency, HttpStatus.OK);
    }

    @GetMapping("/scoreboard")
    public Map<String, Map<String, Object>> getScoreboard() {
         try {
            List<Party> topThreeParties = scoreboardController.getTopThreeParties();
            List<Party> partiesNotInTopThree = scoreboardController.getPartiesNotInTopThree();
            Map<String, Double> share = scoreboardController.calculateShare();

            Map<String, Map<String, Object>> scoreboard = new LinkedHashMap<>();

            topThreeParties.forEach(party -> {
                Map<String, Object> scoreboardEntry = new LinkedHashMap<>();

                String partyCode = party.getPartyCode();

                scoreboardEntry.put("seats", party.getSeats());
                scoreboardEntry.put("share", share.get(partyCode));
                scoreboardEntry.put("majorityWon", party.isMajorityWon());

                scoreboard.put(partyCode, scoreboardEntry);
            });

            Map<String, Object> partiesNotInTopThreeCombinedScoreboardEntry = new LinkedHashMap<>();
            double partiesNotInTopThreeCombinedShare = 0.0;

            for (Party party: partiesNotInTopThree) {
                partiesNotInTopThreeCombinedShare += share.get(party.getPartyCode());
            }

            partiesNotInTopThreeCombinedScoreboardEntry.put(
                    "seats",
                    scoreboardController.getTotalSeats(partiesNotInTopThree)
            );
            partiesNotInTopThreeCombinedScoreboardEntry.put("share", partiesNotInTopThreeCombinedShare);

            scoreboard.put("Others", partiesNotInTopThreeCombinedScoreboardEntry);

            return scoreboard;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

package punitshah.electionscoreboard.restapplication;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import punitshah.electionscoreboard.scoreboard.controller.ScoreboardController;
import punitshah.electionscoreboard.scoreboard.model.ConstituencyResults;

@RestController
public class ScoreboardRestController {
    private ScoreboardController scoreboardController;

    public ScoreboardRestController() {
        scoreboardController = new ScoreboardController();
    }

    @PostMapping("/constituency-results")
    public void postConstituencyResults(@RequestBody ConstituencyResults constituencyResults) {
        scoreboardController.updateParties(constituencyResults);
    }
}

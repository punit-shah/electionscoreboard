package uk.co.bbc.datasystems.electionscoreboard.view;

import uk.co.bbc.datasystems.electionscoreboard.controller.ScoreboardController;
import uk.co.bbc.datasystems.electionscoreboard.domain.Party;
import uk.co.bbc.datasystems.electionscoreboard.xml.ConstituencyResults;
import uk.co.bbc.datasystems.electionscoreboard.xml.XmlFile;
import uk.co.bbc.datasystems.electionscoreboard.xml.XmlFileIterator;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScoreboardView {
    private ScoreboardController scoreboardController;

    public ScoreboardView() {
        scoreboardController = new ScoreboardController();
    }

    public void start() {
        XmlFileIterator xmlFileIterator = new XmlFileIterator();

        Runnable runnable = () -> {
            XmlFile xmlFile = xmlFileIterator.requestNextFile();

            if (xmlFile.isValid()) {
                ConstituencyResults constituencyResults = xmlFile.toObject();
                scoreboardController.updateParties(constituencyResults);
                printTable();
            } else {
                System.out.println(xmlFile.getFilename() + " is not valid - skipping.");
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(runnable, 0, 3, TimeUnit.SECONDS);
    }

    private void printTable() {
        List<Party> topThreeParties = scoreboardController.getTopThreeParties();
        List<Party> partiesNotInTopThree = scoreboardController.getPartiesNotInTopThree();
        Map<String, Double> share = scoreboardController.calculateShare();
        double partiesNotInTopThreeCombinedShare = 0.0;

        System.out.println(String.format("%s\t%6s\t%s", "Party", "Votes", "Share"));
        topThreeParties.forEach(party ->
            System.out.println(String.format(
                    "%-6s\t%6d\t%5.1f", party.getPartyCode(), party.getVotes(), share.get(party.getPartyCode())
            ))
        );

        for (Party party: partiesNotInTopThree) {
            partiesNotInTopThreeCombinedShare += share.get(party.getPartyCode());
        }

        System.out.println(String.format(
                "%-6s\t%6d\t%5.1f",
                "Others",
                scoreboardController.getTotalVotes(partiesNotInTopThree),
                partiesNotInTopThreeCombinedShare
        ));

        System.out.println("----------------------------------------");
    }
}

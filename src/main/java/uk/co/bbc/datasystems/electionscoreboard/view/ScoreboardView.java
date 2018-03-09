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
        executor.scheduleAtFixedRate(runnable, 0, 500, TimeUnit.MILLISECONDS);
    }

    private void printTable() {
        List<Party> topThreeParties = scoreboardController.getTopThreeParties();
        List<Party> partiesNotInTopThree = scoreboardController.getPartiesNotInTopThree();
        Map<String, Double> share = scoreboardController.calculateShare();
        double partiesNotInTopThreeCombinedShare = 0.0;

        System.out.println(String.format("%s\t%5s\t%s", "Party", "Seats", "Share"));
        topThreeParties.forEach(party -> {
            String partyCode = party.getPartyCode();
            int seats = party.getSeats();

            String partyResultString = String.format(
                    "%-6s\t%5d\t%5.1f", partyCode, party.getSeats(), share.get(partyCode)
            );

            System.out.println(seats >= 326 ? partyResultString + " - Majority won" : partyResultString);
        });

        for (Party party: partiesNotInTopThree) {
            partiesNotInTopThreeCombinedShare += share.get(party.getPartyCode());
        }

        System.out.println(String.format(
                "%-6s\t%5d\t%5.1f",
                "Others",
                scoreboardController.getTotalSeats(partiesNotInTopThree),
                partiesNotInTopThreeCombinedShare
        ));

        System.out.println("----------------------------------------");
    }
}

package uk.co.bbc.datasystems.electionscoreboard.view;

import uk.co.bbc.datasystems.electionscoreboard.controller.ScoreboardController;
import uk.co.bbc.datasystems.electionscoreboard.domain.Party;
import uk.co.bbc.datasystems.electionscoreboard.xml.ConstituencyResults;
import uk.co.bbc.datasystems.electionscoreboard.xml.XmlFile;
import uk.co.bbc.datasystems.electionscoreboard.xml.XmlFileIterator;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScoreboardView {
    private ScoreboardController scoreboardController;

    public ScoreboardView() {
        scoreboardController = new ScoreboardController();
    }

    public void start() throws InterruptedException {
        XmlFileIterator xmlFileIterator = new XmlFileIterator();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        CountDownLatch countDownLatch = new CountDownLatch(1);

        executor.scheduleAtFixedRate(() -> {
            XmlFile xmlFile = xmlFileIterator.requestNextFile();
            if (xmlFile == null) {
                countDownLatch.countDown();
                return;
            }

            System.out.println(xmlFile.getFilename());

            if (xmlFile.isValid()) {
                ConstituencyResults constituencyResults = xmlFile.toObject();
                scoreboardController.updateParties(constituencyResults);
                printTable();
            } else {
                System.out.println(xmlFile.getFilename() + " is not valid - skipping.");
            }
        }, 0, 500, TimeUnit.MILLISECONDS);

        countDownLatch.await();
        executor.shutdown();
    }

    private void printTable() {
        List<Party> topThreeParties = scoreboardController.getTopThreeParties();
        List<Party> partiesNotInTopThree = scoreboardController.getPartiesNotInTopThree();
        Map<String, Double> share = scoreboardController.calculateShare();

        System.out.println(String.format("%s\t%5s\t%s", "Party", "Seats", "Share"));
        printTopThreeParties(topThreeParties, share);
        printPartiesNotInTopThreeCombined(partiesNotInTopThree, share);
        System.out.println("----------------------------------------");
    }

    private void printTopThreeParties(List<Party> topThreeParties, Map<String, Double> share) {
        topThreeParties.forEach(party -> {
            String partyCode = party.getPartyCode();
            int seats = party.getSeats();

            String partyResultString = String.format(
                    "%-6s\t%5d\t%5.1f", partyCode, party.getSeats(), share.get(partyCode)
            );

            System.out.println(seats >= 326 ? partyResultString + " - Majority won" : partyResultString);
        });
    }

    private void printPartiesNotInTopThreeCombined(List<Party> partiesNotInTopThree, Map<String, Double> share) {
        double partiesNotInTopThreeCombinedShare = 0.0;

        for (Party party: partiesNotInTopThree) {
            partiesNotInTopThreeCombinedShare += share.get(party.getPartyCode());
        }

        System.out.println(String.format(
                "%-6s\t%5d\t%5.1f",
                "Others",
                scoreboardController.getTotalSeats(partiesNotInTopThree),
                partiesNotInTopThreeCombinedShare
        ));
    }
}

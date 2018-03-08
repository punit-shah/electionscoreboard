package uk.co.bbc.datasystems.electionscoreboard;

import uk.co.bbc.datasystems.electionscoreboard.xml.*;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        XmlFileIterator xmlFileIterator = new XmlFileIterator();

        Runnable runnable = () -> {
            XmlFile xmlFile = xmlFileIterator.requestNextFile();

            ConstituencyResults constituencyResults = xmlFile.toObject();
            printConstituencyResults(constituencyResults);
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(runnable, 0, 3, TimeUnit.SECONDS);
    }

    private static void printConstituencyResults(ConstituencyResults constituencyResults) {
        List<Constituency> constituencyResultsList = constituencyResults.getConstituencies();

        constituencyResultsList.forEach(constituency -> {
            System.out.println(constituency.getConstituencyName());

            System.out.println(String.format("%s\t%6s\t%s", "Party", "Votes", "Share"));

            List<Party> parties = constituency.getResults();
            parties.sort((party1, party2) -> party2.getVotes() - party1.getVotes());
            parties.forEach(party -> System.out.println(
                String.format("%-5s\t%6d\t%5.1f", party.getPartyCode(), party.getVotes(), party.getShare()))
            );
            System.out.println("--------------------------------");
        });
    }
}

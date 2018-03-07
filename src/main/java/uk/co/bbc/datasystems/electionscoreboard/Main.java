package uk.co.bbc.datasystems.electionscoreboard;

import uk.co.bbc.datasystems.electionscoreboard.xml.*;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        XmlFileIterator xmlFileIterator = new XmlFileIterator();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                XmlFile xmlFile = xmlFileIterator.requestNextFile();
                System.out.println(xmlFile.getFilename());

                ConstituencyResults constituencyResults = xmlFile.toObject();
                printConstituencyResults(constituencyResults);
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(runnable, 0, 3, TimeUnit.SECONDS);
    }

    private static void printConstituencyResults(ConstituencyResults constituencyResults) {
        List<Constituency> constituencyResultsList = constituencyResults.getConstituencies();

        constituencyResultsList.forEach(constituency -> {
            System.out.println(constituency.getConstituencyName());

            List<Party> parties = constituency.getResults();
            parties.forEach(party -> {
                System.out.println(party.getPartyCode() + "\t" + party.getVotes() + "\t" + party.getShare());
            });
        });

        System.out.println();
    }
}

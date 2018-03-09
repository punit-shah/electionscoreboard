package uk.co.bbc.datasystems.electionscoreboard;

import uk.co.bbc.datasystems.electionscoreboard.view.ScoreboardView;
import uk.co.bbc.datasystems.electionscoreboard.xml.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            new ScoreboardView().start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void printConstituencyResults(ConstituencyResults constituencyResults) {
        List<Constituency> constituencyResultsList = constituencyResults.getConstituencies();

        constituencyResultsList.forEach(constituency -> {
            System.out.println(constituency.getConstituencyName());

            System.out.println(String.format("%s\t%6s\t%s", "Party", "Votes", "Share"));

            List<ConstituencyParty> parties = constituency.getParties();
            parties.sort((party1, party2) -> party2.getVotes() - party1.getVotes());
            parties.forEach(party -> System.out.println(
                String.format("%-5s\t%6d\t%5.1f", party.getPartyCode(), party.getVotes(), party.getShare()))
            );
            System.out.println("--------------------------------");
        });
    }
}

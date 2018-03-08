package uk.co.bbc.datasystems.electionscoreboard.controller;

import uk.co.bbc.datasystems.electionscoreboard.domain.Party;
import uk.co.bbc.datasystems.electionscoreboard.xml.Constituency;
import uk.co.bbc.datasystems.electionscoreboard.xml.ConstituencyResults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreboardController {
    private ArrayList<Party> parties;

    public ScoreboardController() {
        parties = new ArrayList<>();
        parties.add(new Party("LAB"));
        parties.add(new Party("CON"));
        parties.add(new Party("LD"));
        parties.add(new Party("PC"));
        parties.add(new Party("UKIP"));
        parties.add(new Party("GRN"));
        parties.add(new Party("OTH"));
    }

    public List<Party> getParties() {
        sortParties();
        return parties;
    }

    public List<Party> getTopThreeParties() {
        sortParties();
        return new ArrayList<>(parties.subList(0, 3));
    }

    public List<Party> getPartiesNotInTopThree() {
        sortParties();
        return new ArrayList<>(parties.subList(3, parties.size()));
    }

    public Map<String, Double> calculateShare() {
        Map<String, Double> share = new HashMap<>();
        double totalVotes = getTotalVotes(parties);

        parties.forEach(party ->
                share.put(party.getPartyCode(), (double) party.getVotes() / totalVotes * 100.0)
        );

        return share;
    }

    public void updateParties(ConstituencyResults constituencyResults) {
        Constituency constituency = constituencyResults.getConstituencies().get(0);
        List<uk.co.bbc.datasystems.electionscoreboard.xml.Party> constituencyParties = constituency.getParties();

        constituencyParties.forEach(constituencyParty ->
            parties.stream()
                    .filter(party -> party.getPartyCode().trim().equals(constituencyParty.getPartyCode().trim()))
                    .forEach(matchingParty -> matchingParty.updateVotes(constituencyParty.getVotes()))
        );
    }

    public int getTotalVotes(List<Party> parties) {
        return parties.stream().mapToInt(Party::getVotes).sum();
    }

    private void sortParties() {
        parties.sort((party1, party2) -> party2.getVotes() - party1.getVotes());
    }
}

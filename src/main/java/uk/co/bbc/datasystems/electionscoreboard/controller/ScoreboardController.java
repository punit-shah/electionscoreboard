package uk.co.bbc.datasystems.electionscoreboard.controller;

import uk.co.bbc.datasystems.electionscoreboard.domain.Party;
import uk.co.bbc.datasystems.electionscoreboard.xml.Constituency;
import uk.co.bbc.datasystems.electionscoreboard.xml.ConstituencyParty;
import uk.co.bbc.datasystems.electionscoreboard.xml.ConstituencyResults;

import java.util.*;

public class ScoreboardController {
    private ArrayList<Party> parties;

    public ScoreboardController() {
        parties = new ArrayList<>();
        parties.add(new Party("LAB"));
        parties.add(new Party("CON"));
        parties.add(new Party("LD"));
        parties.add(new Party("GRN"));
        parties.add(new Party("UKIP"));
        parties.add(new Party("PC"));
        parties.add(new Party("SNP"));
        parties.add(new Party("SSP"));
        parties.add(new Party("IND"));
        parties.add(new Party("OCV"));
        parties.add(new Party("OTH"));
        parties.add(new Party("PPS"));
        parties.add(new Party("SGP"));
        parties.add(new Party("FSP"));
        parties.add(new Party("SLP"));
        parties.add(new Party("SSCP"));
        parties.add(new Party("PIP"));
        parties.add(new Party("RES"));
        parties.add(new Party("SIP"));
        parties.add(new Party("DDT"));
        parties.add(new Party("BNP"));
        parties.add(new Party("CPB"));
        parties.add(new Party("IKHH"));
        parties.add(new Party("SUP"));
        parties.add(new Party("IGV"));
        parties.add(new Party("PubP"));
        parties.add(new Party("LIB"));
        parties.add(new Party("LC"));
        parties.add(new Party("SF"));
        parties.add(new Party("SDLP"));
        parties.add(new Party("DUP"));
        parties.add(new Party("UUP"));
        parties.add(new Party("WP"));
        parties.add(new Party("VFY"));
        parties.add(new Party("AP"));
        parties.add(new Party("SEA"));
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
        List<ConstituencyParty> constituencyParties = constituency.getParties();

        updateVotesForEachParty(constituencyParties);
        updateSeatsForWinningParty(constituencyParties);
    }

    public int getTotalVotes(List<Party> parties) {
        return parties.stream().mapToInt(Party::getVotes).sum();
    }

    public int getTotalSeats(List<Party> parties) {
        return parties.stream().mapToInt(Party::getSeats).sum();
    }

    private void updateVotesForEachParty(List<ConstituencyParty> constituencyParties) {
        constituencyParties.forEach(constituencyParty ->
                parties.stream()
                        .filter(party -> party.getPartyCode().trim().equals(constituencyParty.getPartyCode().trim()))
                        .findFirst().orElse(null)
                        .updateVotes(constituencyParty.getVotes())
        );
    }

    private void updateSeatsForWinningParty(List<ConstituencyParty> constituencyParties) {
        parties.stream()
                .filter(party ->
                        party.getPartyCode().trim().equals(
                                getWinningConstituencyParty(constituencyParties).getPartyCode().trim()
                        )
                )
                .findFirst().orElse(null)
                .winSeat();
    }

    private ConstituencyParty getWinningConstituencyParty(List<ConstituencyParty> parties) {
        return parties.stream().max(Comparator.comparingDouble(ConstituencyParty::getShare)).orElse(null);
    }

    private void sortParties() {
        parties.sort((party1, party2) -> party2.getSeats() - party1.getSeats());
    }
}

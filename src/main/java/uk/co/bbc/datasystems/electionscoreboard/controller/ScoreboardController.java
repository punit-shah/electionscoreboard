package uk.co.bbc.datasystems.electionscoreboard.controller;

import uk.co.bbc.datasystems.electionscoreboard.domain.Party;
import uk.co.bbc.datasystems.electionscoreboard.xml.Constituency;
import uk.co.bbc.datasystems.electionscoreboard.xml.ConstituencyParty;
import uk.co.bbc.datasystems.electionscoreboard.xml.ConstituencyResults;

import java.util.*;
import java.util.stream.Collectors;

public class ScoreboardController {
    private Map<String, Party> parties;

    public ScoreboardController() {
        parties = new HashMap<>();
    }

    public List<Party> getTopThreeParties() {
        List<Party> partyList = getSortedPartyList();
        return new ArrayList<>(partyList.subList(0, 3));
    }

    public List<Party> getPartiesNotInTopThree() {
        List<Party> partyList = getSortedPartyList();
        return new ArrayList<>(partyList.subList(3, partyList.size()));
    }

    public Map<String, Double> calculateShare() {
        Map<String, Double> share = new HashMap<>();
        ArrayList<Party> partyList = new ArrayList<>(parties.values());
        double totalVotes = getTotalVotes(partyList);

        partyList.forEach(party ->
                share.put(party.getPartyCode(), (double) party.getVotes() / totalVotes * 100.0)
        );

        return share;
    }

    public void updateParties(ConstituencyResults constituencyResults) {
        Constituency constituency = constituencyResults.getConstituencies().get(0);
        List<ConstituencyParty> constituencyParties = constituency.getParties();

        addPartiesToMapIfNotPresent(constituencyParties);
        updateVotesForEachParty(constituencyParties);
        updateSeatsForWinningParty(constituencyParties);
    }

    public int getTotalVotes(List<Party> parties) {
        return parties.stream().mapToInt(Party::getVotes).sum();
    }

    public int getTotalSeats(List<Party> parties) {
        return parties.stream().mapToInt(Party::getSeats).sum();
    }

    private void addPartiesToMapIfNotPresent(List<ConstituencyParty> constituencyParties) {
        constituencyParties.stream()
                .map(ConstituencyParty::getPartyCode)
                .forEach(partyCode -> {
                    if (parties.get(partyCode) == null) {
                        parties.put(partyCode, new Party(partyCode));
                    }
                });
    }

    private void updateVotesForEachParty(List<ConstituencyParty> constituencyParties) {
        constituencyParties.forEach(constituencyParty ->
                parties.values().stream()
                        .filter(party -> party.getPartyCode().trim().equals(constituencyParty.getPartyCode().trim()))
                        .findFirst().orElse(null)
                        .updateVotes(constituencyParty.getVotes())
        );
    }

    private void updateSeatsForWinningParty(List<ConstituencyParty> constituencyParties) {
        parties.values().stream()
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

    private List<Party> getSortedPartyList() {
        return parties.values().stream()
                .sorted((party1, party2) -> party2.getSeats() - party1.getSeats())
                .collect(Collectors.toList());
    }
}

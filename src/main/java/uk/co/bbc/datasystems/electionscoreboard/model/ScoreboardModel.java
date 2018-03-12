package uk.co.bbc.datasystems.electionscoreboard.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ScoreboardModel {
    private Map<String, Party> parties;

    public ScoreboardModel() {
        parties = new HashMap<>();
    }

    private void addParty(String partyCode) {
        parties.put(partyCode, new Party(partyCode));
    }

    public boolean checkPartyIsPresent(String partyCode) {
        return parties.get(partyCode) != null;
    }

    public void addPartyIfNotPresent(String partyCode) {
        if (!checkPartyIsPresent(partyCode)) {
            addParty(partyCode);
        }
    }

    public Map<String, Party> getParties() {
        return parties;
    }

    public List<Party> getPartyList() {
        return new ArrayList<>(parties.values());
    }

    public List<Party> getSortedPartyList() {
        return parties.values().stream()
                .sorted((party1, party2) -> party2.getSeats() - party1.getSeats())
                .collect(Collectors.toList());
    }

    public void updateVotesForParty(String partyCode, int votesToAdd) {
        parties.get(partyCode).updateVotes(votesToAdd);
    }

    public void incrementSeatsForParty(String partyCode) {
        parties.get(partyCode).winSeat();
    }
}

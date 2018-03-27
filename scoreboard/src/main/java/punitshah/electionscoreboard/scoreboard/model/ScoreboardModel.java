package punitshah.electionscoreboard.scoreboard.model;

import java.util.*;
import java.util.stream.Collectors;

public class ScoreboardModel {
    private Map<String, Party> parties;
    private List<Constituency> constituencies;

    public ScoreboardModel() {
        parties = new HashMap<>();
        constituencies = new ArrayList<>();
    }

    private void addPartyToMap(String partyCode) {
        parties.put(partyCode, new Party(partyCode));
    }

    public boolean checkPartyIsPresent(String partyCode) {
        return parties.get(partyCode) != null;
    }

    public void addParty(String partyCode) {
        if (!checkPartyIsPresent(partyCode)) {
            addPartyToMap(partyCode);
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
                .sorted((party1, party2) -> {
                    int seatsComparison = party2.getSeats() - party1.getSeats();
                    if (seatsComparison != 0) {
                        return seatsComparison;
                    } else {
                        return party2.getVotes() - party1.getVotes();
                    }
                })
                .collect(Collectors.toList());
    }

    public void updateVotesForParty(String partyCode, int votesToAdd) {
        parties.get(partyCode).updateVotes(votesToAdd);
    }

    public void incrementSeatsForParty(String partyCode) {
        parties.get(partyCode).winSeat();
    }

    public List<Constituency> getConstituencyList() {
        return constituencies;
    }

    public List<Constituency> getSortedConstituencyList() {
        return constituencies.stream()
                .sorted(Comparator.comparingInt(Constituency::getConstituencyId))
                .collect(Collectors.toList());
    }

    public void addConstituency(Constituency constituency) {
        constituencies.add(constituency);
    }

    public Constituency getConstituencyById(int constituencyId) {
        return constituencies.stream()
                .filter(constituency -> constituency.getConstituencyId() == constituencyId)
                .findFirst()
                .orElse(null);
    }

    public boolean checkConstituencyIsPresent(int constituencyId) {
        return getConstituencyById(constituencyId) != null;
    }
}

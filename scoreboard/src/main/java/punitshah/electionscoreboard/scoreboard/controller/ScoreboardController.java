package punitshah.electionscoreboard.scoreboard.controller;

import punitshah.electionscoreboard.scoreboard.model.*;

import java.util.*;

public class ScoreboardController {
    private ScoreboardModel scoreboardModel;

    public ScoreboardController() {
        scoreboardModel = new ScoreboardModel();
    }

    public List<Constituency> getConstituencyList() {
        return scoreboardModel.getConstituencyList();
    }

    public List<Constituency> getSortedConstituencyList() {
        return scoreboardModel.getSortedConstituencyList();
    }

    public Constituency getConstituency(int constituencyId) {
        return scoreboardModel.getConstituencyById(constituencyId);
    }

    public boolean updateConstituencies(ConstituencyResults constituencyResults) {
        Constituency constituency = constituencyResults.getConstituencies().get(0);

        if (!scoreboardModel.checkConstituencyIsPresent(constituency.getConstituencyId())) {
            scoreboardModel.addConstituency(constituency);
            return true;
        } else {
            return false;
        }
    }

    public Map<String, Party> getPartyMap() {
        return scoreboardModel.getParties();
    }

    public List<Party> getPartyList() {
        return scoreboardModel.getPartyList();
    }

    public List<Party> getSortedPartyList() {
        return scoreboardModel.getSortedPartyList();
    }

    public List<Party> getTopThreeParties() {
        List<Party> partyList = scoreboardModel.getSortedPartyList();
        return new ArrayList<>(partyList.subList(0, 3));
    }

    public List<Party> getPartiesNotInTopThree() {
        List<Party> partyList = scoreboardModel.getSortedPartyList();
        return new ArrayList<>(partyList.subList(3, partyList.size()));
    }

    public Map<String, Double> calculateShare() {
        Map<String, Double> share = new HashMap<>();
        List<Party> partyList = scoreboardModel.getPartyList();
        double totalVotes = getTotalVotes(partyList);

        partyList.forEach(party ->
                share.put(party.getPartyCode(), (double) party.getVotes() / totalVotes * 100.0)
        );

        return share;
    }

    public void updateParties(ConstituencyResults constituencyResults) {
        Constituency constituency = constituencyResults.getConstituencies().get(0);
        List<ConstituencyParty> constituencyParties = constituency.getParties();

        addParties(constituencyParties);
        updateVotesForEachParty(constituencyParties);
        updateSeatsForWinningParty(constituencyParties);
    }

    public int getTotalVotes(List<Party> parties) {
        return parties.stream().mapToInt(Party::getVotes).sum();
    }

    public int getTotalSeats(List<Party> parties) {
        return parties.stream().mapToInt(Party::getSeats).sum();
    }

    private void addParties(List<ConstituencyParty> constituencyParties) {
        constituencyParties.stream()
                .map(ConstituencyParty::getPartyCode)
                .forEach(partyCode -> scoreboardModel.addParty(partyCode));
    }

    private void updateVotesForEachParty(List<ConstituencyParty> constituencyParties) {
        constituencyParties.forEach(constituencyParty ->
                scoreboardModel.updateVotesForParty(constituencyParty.getPartyCode(), constituencyParty.getVotes())
        );
    }

    private void updateSeatsForWinningParty(List<ConstituencyParty> constituencyParties) {
        ConstituencyParty winningParty = getWinningConstituencyParty(constituencyParties);
        scoreboardModel.incrementSeatsForParty(winningParty.getPartyCode());
    }

    private ConstituencyParty getWinningConstituencyParty(List<ConstituencyParty> parties) {
        return parties.stream().max(Comparator.comparingDouble(ConstituencyParty::getShare)).orElse(null);
    }
}

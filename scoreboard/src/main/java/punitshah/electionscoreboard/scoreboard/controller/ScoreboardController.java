package punitshah.electionscoreboard.scoreboard.controller;

import punitshah.electionscoreboard.scoreboard.model.*;

import java.sql.SQLException;
import java.util.*;

public class ScoreboardController {
    private ScoreboardModel scoreboardModel;

    public ScoreboardController() {
        scoreboardModel = new ScoreboardModel();
    }

    public List<Constituency> getConstituencyList() throws SQLException {
        return scoreboardModel.getConstituencyList();
    }

    public Constituency getConstituency(int constituencyId) throws SQLException {
        return scoreboardModel.getConstituencyById(constituencyId);
    }

    public boolean updateConstituencies(ConstituencyResults constituencyResults) throws SQLException {
        Constituency constituency = constituencyResults.getConstituencies().get(0);

        if (!scoreboardModel.checkConstituencyIsPresent(constituency.getConstituencyId())) {
            scoreboardModel.addConstituency(constituency);
            return true;
        } else {
            return false;
        }
    }

    public Map<String, Party> getPartyMap() throws SQLException {
        return scoreboardModel.getParties();
    }

    public List<Party> getPartyList() throws SQLException {
        return scoreboardModel.getPartyList();
    }

    public List<Party> getSortedPartyList() throws SQLException {
        return scoreboardModel.getSortedPartyList();
    }

    public List<Party> getTopThreeParties() throws SQLException {
        List<Party> partyList = scoreboardModel.getSortedPartyList();
        return new ArrayList<>(partyList.subList(0, 3));
    }

    public List<Party> getPartiesNotInTopThree() throws SQLException {
        List<Party> partyList = scoreboardModel.getSortedPartyList();
        return new ArrayList<>(partyList.subList(3, partyList.size()));
    }

    public Map<String, Double> calculateShare() throws SQLException {
        Map<String, Double> share = new HashMap<>();
        List<Party> partyList = scoreboardModel.getPartyList();
        double totalVotes = getTotalVotes(partyList);

        partyList.forEach(party ->
                share.put(party.getPartyCode(), (double) party.getVotes() / totalVotes * 100.0)
        );

        return share;
    }

    public int getTotalVotes(List<Party> parties) {
        return parties.stream().mapToInt(Party::getVotes).sum();
    }

    public int getTotalSeats(List<Party> parties) {
        return parties.stream().mapToInt(Party::getSeats).sum();
    }
}

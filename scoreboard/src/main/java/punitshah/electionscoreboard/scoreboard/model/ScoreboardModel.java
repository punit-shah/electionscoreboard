package punitshah.electionscoreboard.scoreboard.model;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class ScoreboardModel {
    private static final String DB_DRIVER = "org.postgresql.Driver";
    private static final String DB_CONNECTION = "jdbc:postgresql:electionscoreboard";
    private static final String DB_USER = "punit";
    private static final String DB_PASSWORD = "password123";

    private Connection getDBConnection() throws SQLException {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
    }

    public void addConstituency(Constituency constituency) throws SQLException {
        int constituencyId = constituency.getConstituencyId();
        String constituencyName = constituency.getConstituencyName();
        List<ConstituencyParty> parties = constituency.getParties();
        ConstituencyParty winningParty = getWinningConstituencyParty(parties);

        Connection connection = getDBConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate(
                "INSERT INTO constituency(constituency_id, constituency_name) " +
                "VALUES (" + constituencyId + ", '" + constituencyName + "');"
        );

        for (ConstituencyParty party : parties) {
            boolean winsSeat = party.getPartyCode().equals(winningParty.getPartyCode());
            addParty(party, constituencyId, winsSeat);
        }

        statement.close();
        connection.close();
    }

    public void addParty(
            ConstituencyParty party,
            int constituencyId,
            boolean winsSeat
    ) throws SQLException {
        String partyCode = party.getPartyCode();
        int votes = party.getVotes();
        double share = party.getShare();

        Connection connection = getDBConnection();
        Statement statement = connection.createStatement();

        statement.executeUpdate(
                "INSERT INTO party(party_code, votes, share, constituency_id, wins_seat) " +
                        "VALUES ('" + partyCode + "', " + votes + ", " + share + ", " +
                        constituencyId + ", " + winsSeat + ")"
        );

        statement.close();
        connection.close();
    }

    public ConstituencyParty getWinningConstituencyParty(List<ConstituencyParty> parties) {
        return parties.stream()
                .max(Comparator.comparingDouble(ConstituencyParty::getShare))
                .orElse(null);
    }

    private void updateParty(Party party, int votes, boolean winsSeat) {
        party.updateVotes(votes);
        if (winsSeat) {
            party.winSeat();
        }
    }

    public Map<String, Party> getParties() throws SQLException {
        Map<String, Party> parties = new HashMap<>();

        Connection connection = getDBConnection();
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery(
                "SELECT party_code, votes, wins_seat FROM party;"
        );

        while (resultSet.next()) {
            String partyCode = resultSet.getString("party_code");
            int votes = resultSet.getInt("votes");
            boolean winsSeat = resultSet.getBoolean("wins_seat");

            if (parties.get(partyCode) == null) {
                Party party = new Party(partyCode);
                updateParty(party, votes, winsSeat);
            } else {
                Party party = parties.get(partyCode);
                updateParty(party, votes, winsSeat);
            }
        }

        statement.close();
        connection.close();

        return parties;
    }

    public List<Party> getPartyList() throws SQLException {
        return new ArrayList<>(getParties().values());
    }

    public List<Party> getSortedPartyList() throws SQLException {
        return getParties().values().stream()
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

    private List<ConstituencyParty> getConstituencyParties(int constituencyId) throws SQLException {
        List<ConstituencyParty> constituencyParties = new ArrayList<>();

        Connection connection = getDBConnection();
        Statement statement = connection.createStatement();

        ResultSet partyResultSet = statement.executeQuery(
                "SELECT party_code, votes, share FROM party " +
                        "WHERE constituency_id=" + constituencyId + ";"
        );

        while (partyResultSet.next()) {
            String partyCode = partyResultSet.getString("party_code");
            int votes = partyResultSet.getInt("votes");
            double share = partyResultSet.getDouble("share");

            ConstituencyParty party = new ConstituencyParty();
            party.setPartyCode(partyCode);
            party.setVotes(votes);
            party.setShare(share);

            constituencyParties.add(party);
        }

        statement.close();
        connection.close();

        return constituencyParties;
    }

    public List<Constituency> getConstituencyList() throws SQLException {
        List<Constituency> constituencies = new ArrayList<>();

        Connection connection = getDBConnection();
        Statement statement = connection.createStatement();

        ResultSet constituencyResultSet = statement.executeQuery(
                "SELECT constituency_id, constituency_name FROM constituency " +
                        "ORDER BY constituency_id;"
        );

        while (constituencyResultSet.next()) {
            int constituencyId = constituencyResultSet.getInt("constituency_id");
            String constituencyName = constituencyResultSet.getString("constituency_name");

            List<ConstituencyParty> parties = getConstituencyParties(constituencyId);

            Constituency constituency = new Constituency();
            constituency.setConstituencyId(constituencyId);
            constituency.setConstituencyName(constituencyName);
            constituency.setParties(parties);

            constituencies.add(constituency);
        }

        statement.close();
        connection.close();

        return constituencies;
    }

    public Constituency getConstituencyById(int constituencyId) throws SQLException {
        Constituency constituency;

        Connection connection = getDBConnection();
        Statement statement = connection.createStatement();

        ResultSet constituencyResultSet = statement.executeQuery(
                "SELECT constituency_name FROM constituency " +
                        "WHERE constituency_id=" + constituencyId + ";"
        );

        while (constituencyResultSet.next()) {
            String constituencyName = constituencyResultSet.getString("constituency_name");

            List<ConstituencyParty> parties = getConstituencyParties(constituencyId);

            constituency = new Constituency();
            constituency.setConstituencyId(constituencyId);
            constituency.setConstituencyName(constituencyName);
            constituency.setParties(parties);

            return constituency;
        }

        statement.close();
        connection.close();

        return null;
    }

    public boolean checkConstituencyIsPresent(int constituencyId) throws SQLException {
        return getConstituencyById(constituencyId) != null;
    }
}

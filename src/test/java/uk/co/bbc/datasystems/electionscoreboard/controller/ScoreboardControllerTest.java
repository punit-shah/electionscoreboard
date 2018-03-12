package uk.co.bbc.datasystems.electionscoreboard.controller;

import org.junit.Before;
import org.junit.Test;

import uk.co.bbc.datasystems.electionscoreboard.model.Party;
import uk.co.bbc.datasystems.electionscoreboard.xml.XmlFile;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ScoreboardControllerTest {
    private ScoreboardController scoreboardController;

    @Before
    public void setUp() {
        scoreboardController = new ScoreboardController();
        scoreboardController.updateParties(new XmlFile("test_result.xml").toObject());
    }

    @Test
    public void getTopThreeParties() {
        List<Party> topThreeParties = scoreboardController.getTopThreeParties();
        assertEquals(3, topThreeParties.size());
        assertEquals("P1", topThreeParties.get(0).getPartyCode());
        assertEquals("P4", topThreeParties.get(1).getPartyCode());
        assertEquals("P2", topThreeParties.get(2).getPartyCode());
    }

    @Test
    public void calculateShare() {
        Map<String, Double> share = scoreboardController.calculateShare();
        assertEquals(new Double(40.0), share.get("P1"));
        assertEquals(new Double(20.0), share.get("P2"));
        assertEquals(new Double(15.0), share.get("P3"));
        assertEquals(new Double(25.0), share.get("P4"));
    }

    @Test
    public void updateParties() {
        assertEquals(1, scoreboardController.getTopThreeParties().get(0).getSeats());
        assertEquals(400, scoreboardController.getTopThreeParties().get(0).getVotes());

        scoreboardController.updateParties(new XmlFile("test_result.xml").toObject());
        assertEquals(2, scoreboardController.getTopThreeParties().get(0).getSeats());
        assertEquals(800, scoreboardController.getTopThreeParties().get(0).getVotes());
    }

    @Test
    public void getTotalVotes() {
        int topThreeTotalVotes = scoreboardController.getTotalVotes(scoreboardController.getTopThreeParties());
        assertEquals(850, topThreeTotalVotes);
    }

    @Test
    public void getTotalSeats() {
        int topThreeTotalSeats = scoreboardController.getTotalSeats(scoreboardController.getTopThreeParties());
        assertEquals(1, topThreeTotalSeats);
    }
}
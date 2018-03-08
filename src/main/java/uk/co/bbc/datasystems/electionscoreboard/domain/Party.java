package uk.co.bbc.datasystems.electionscoreboard.domain;

public class Party {
    private String partyCode;
    private int votes;

    public Party(String partyCode) {
        this.partyCode = partyCode;
        votes = 0;
    }

    public String getPartyCode() {
        return partyCode;
    }

    public int getVotes() {
        return votes;
    }

    public void updateVotes(int votesToAdd) {
        votes += votesToAdd;
    }
}

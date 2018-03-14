package punitshah.electionscoreboard.scoreboard.model;

public class Party {
    private String partyCode;
    private int votes;
    private int seats;

    Party(String partyCode) {
        this.partyCode = partyCode;
        votes = 0;
        seats = 0;
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

    public int getSeats() {
        return seats;
    }

    public void winSeat() {
        seats++;
    }
}

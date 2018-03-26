package punitshah.electionscoreboard.scoreboard.model;

public class Party {
    private String partyCode;
    private int votes;
    private int seats;
    private boolean majorityWon;

    private static final int MAJORITY_THRESHOLD = 326;

    Party(String partyCode) {
        this.partyCode = partyCode;
        votes = 0;
        seats = 0;
        majorityWon = false;
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
        updateMajorityWon();
    }

    public boolean isMajorityWon() {
        return majorityWon;
    }

    public void updateMajorityWon() {
        majorityWon = seats >= MAJORITY_THRESHOLD;
    }
}

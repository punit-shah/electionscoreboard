package punitshah.electionscoreboard.scoreboard.model;

import javax.validation.constraints.NotBlank;

import javax.xml.bind.annotation.XmlElement;

public class ConstituencyParty {
    @NotBlank(message = "partyCode cannot be blank")
    private String partyCode;

    private int votes;
    private double share;

    @XmlElement
    public String getPartyCode() {
        return partyCode;
    }

    public void setPartyCode(String partyCode) {
        this.partyCode = partyCode.trim();
    }

    @XmlElement
    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    @XmlElement
    public double getShare() {
        return share;
    }

    public void setShare(double share) {
        this.share = share;
    }
}

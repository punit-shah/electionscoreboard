package uk.co.bbc.datasystems.electionscoreboard.xml;

import javax.xml.bind.annotation.XmlElement;

public class Party {
    private String partyCode;
    private int votes;
    private double share;

    @XmlElement
    public String getPartyCode() {
        return partyCode;
    }

    public void setPartyCode(String partyCode) {
        this.partyCode = partyCode;
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

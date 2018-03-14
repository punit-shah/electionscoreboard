package punitshah.electionscoreboard.scoreboard.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

public class Constituency {
    private int seqNo;
    private int constituencyId;
    private String constituencyName;
    private List<ConstituencyParty> parties;

    @XmlAttribute
    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    @XmlElement(name = "consituencyId")
    public int getConstituencyId() {
        return constituencyId;
    }

    public void setConstituencyId(int consituencyId) {
        this.constituencyId = consituencyId;
    }

    @XmlElement
    public String getConstituencyName() {
        return constituencyName;
    }

    public void setConstituencyName(String constituencyName) {
        this.constituencyName = constituencyName;
    }

    @XmlElementWrapper(name = "results")
    @XmlElement(name = "result")
    public List<ConstituencyParty> getParties() {
        return parties;
    }

    public void setParties(List<ConstituencyParty> parties) {
        this.parties = parties;
    }
}

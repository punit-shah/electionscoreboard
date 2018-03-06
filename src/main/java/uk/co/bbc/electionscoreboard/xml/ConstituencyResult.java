package uk.co.bbc.electionscoreboard.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

public class ConstituencyResult {
    private int seqNo;
    private int consituencyId;
    private String constituencyName;
    private List<Result> results;

    @XmlAttribute
    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    @XmlElement
    public int getConsituencyId() {
        return consituencyId;
    }

    public void setConsituencyId(int consituencyId) {
        this.consituencyId = consituencyId;
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
    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }
}

package uk.co.bbc.electionscoreboard.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class ConstituencyResults {
    private List<Constituency> constituencies;

    @XmlElement(name = "constituencyResult")
    public List<Constituency> getConstituencies() {
        return constituencies;
    }

    public void setConstituencies(List<Constituency> constituencies) {
        this.constituencies = constituencies;
    }
}

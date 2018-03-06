package uk.co.bbc.electionscoreboard.xmlfileunmarshaller;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class ConstituencyResults {
    private List<ConstituencyResult> constituencyResults;

    public List<ConstituencyResult> getConstituencyResult() {
        return constituencyResults;
    }

    public void setConstituencyResult(List<ConstituencyResult> constituencyResult) {
        this.constituencyResults = constituencyResult;
    }
}

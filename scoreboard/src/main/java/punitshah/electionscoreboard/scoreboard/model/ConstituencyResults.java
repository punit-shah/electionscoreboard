package punitshah.electionscoreboard.scoreboard.model;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class ConstituencyResults {
    @Size(max = 1, message = "constituencyResults cannot have more than 1 constituencyResult")
    @NotEmpty(message = "constituencyResults cannot be empty")
    @Valid
    private List<Constituency> constituencies;

    @XmlElement(name = "constituencyResult")
    public List<Constituency> getConstituencies() {
        return constituencies;
    }

    public void setConstituencies(List<Constituency> constituencies) {
        this.constituencies = constituencies;
    }
}

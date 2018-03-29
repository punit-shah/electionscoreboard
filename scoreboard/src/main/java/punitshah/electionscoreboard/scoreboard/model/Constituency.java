package punitshah.electionscoreboard.scoreboard.model;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

public class Constituency {
    private int constituencyId;

    @NotBlank(message = "consituencyName cannot be blank")
    private String constituencyName;

    @NotEmpty(message = "results cannot be empty")
    @Valid
    private List<ConstituencyParty> parties;

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

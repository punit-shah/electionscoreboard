package punitshah.electionscoreboard.scoreboard.xml;

import org.junit.Test;
import punitshah.electionscoreboard.scoreboard.model.Constituency;
import punitshah.electionscoreboard.scoreboard.model.ConstituencyResults;

import static org.junit.Assert.assertEquals;

public class XmlFileTest {
    @Test
    public void toObject() {
        ConstituencyResults constituencyResults = new XmlFile("test_result.xml").toObject();
        Constituency constituency = constituencyResults.getConstituencies().get(0);

        assertEquals(123, constituency.getConstituencyId());
        assertEquals("TestConstituency", constituency.getConstituencyName());
        assertEquals(400, constituency.getParties().get(0).getVotes());
        assertEquals(40.0, constituency.getParties().get(0).getShare(), 0.1);
    }

    @Test
    public void isValid() {
        XmlFile validFile = new XmlFile("test_result.xml");
        XmlFile invalidFile = new XmlFile("test_result_invalid.xml");

        assertEquals(true, validFile.isValid());
        assertEquals(false, invalidFile.isValid());
    }

}

package uk.co.bbc.electionscoreboard;

import uk.co.bbc.electionscoreboard.xml.*;

import javax.xml.bind.JAXBException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        XmlFile xmlFile = new XmlFile("src/main/resources/xml-files/result001.xml");
        ConstituencyResults constituencyResults = xmlFile.toObject();

        List<Constituency> constituencyResultsList = constituencyResults.getConstituencies();

        constituencyResultsList.forEach(constituency -> {
            System.out.println("seqNo: " + constituency.getSeqNo());
            System.out.println("constituencyId: " + constituency.getConstituencyId());
            System.out.println(constituency.getConstituencyName());

            List<Result> results = constituency.getResults();
            System.out.println(results.size());
            results.forEach(result -> {
                System.out.println(result.getPartyCode());
                System.out.println("votes: " + result.getVotes());
                System.out.println("share: " + result.getShare());
            });
        });
    }
}

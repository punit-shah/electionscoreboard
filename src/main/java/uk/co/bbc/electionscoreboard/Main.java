package uk.co.bbc.electionscoreboard;

import uk.co.bbc.electionscoreboard.xml.ConstituencyResult;
import uk.co.bbc.electionscoreboard.xml.ConstituencyResults;
import uk.co.bbc.electionscoreboard.xml.Result;
import uk.co.bbc.electionscoreboard.xml.XmlFileUnmarshaller;

import javax.xml.bind.JAXBException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws JAXBException {
        XmlFileUnmarshaller xmlFileUnmarshaller = new XmlFileUnmarshaller();
        ConstituencyResults constituencyResults = xmlFileUnmarshaller.convertXmlFileToObject(
            "src/main/resources/xml-files/result001.xml"
        );

        List<ConstituencyResult> constituencyResultsList = constituencyResults.getConstituencyResult();

        constituencyResultsList.forEach(constituencyResult -> {
            System.out.println("seqNo: " + constituencyResult.getSeqNo());
            System.out.println("consituencyId: " + constituencyResult.getConsituencyId());
            System.out.println(constituencyResult.getConstituencyName());

            List<Result> results = constituencyResult.getResults();
            System.out.println(results.size());
            results.forEach(result -> {
                System.out.println(result.getPartyCode());
                System.out.println("votes: " + result.getVotes());
                System.out.println("share: " + result.getShare());
                System.out.println("test: " + result.getShare());
            });
        });
    }
}

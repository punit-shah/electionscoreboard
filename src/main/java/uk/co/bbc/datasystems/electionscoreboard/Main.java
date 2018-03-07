package uk.co.bbc.datasystems.electionscoreboard;

import uk.co.bbc.datasystems.electionscoreboard.xml.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        XmlFile xmlFile = new XmlFile("xml-files/result001.xml");
        ConstituencyResults constituencyResults = xmlFile.toObject();

        List<Constituency> constituencyResultsList = constituencyResults.getConstituencies();

        constituencyResultsList.forEach(constituency -> {
            System.out.println("seqNo: " + constituency.getSeqNo());
            System.out.println("constituencyId: " + constituency.getConstituencyId());
            System.out.println(constituency.getConstituencyName());

            List<Party> parties = constituency.getResults();
            parties.forEach(party -> {
                System.out.println(party.getPartyCode());
                System.out.println("votes: " + party.getVotes());
                System.out.println("share: " + party.getShare());
            });
        });
    }
}

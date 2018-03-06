package uk.co.bbc.electionscoreboard.xmlfileunmarshaller;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class XmlFileUnmarshaller {
    public ConstituencyResults convertXmlFileToObject(String pathname) throws JAXBException {
        File file = new File(pathname);
        JAXBContext jaxbContext = JAXBContext.newInstance(ConstituencyResults.class);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (ConstituencyResults) unmarshaller.unmarshal(file);
    }
}

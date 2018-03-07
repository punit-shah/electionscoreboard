package uk.co.bbc.electionscoreboard.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class XmlFile {
    private File file;
    private boolean isValid;

    public XmlFile(String pathname) {
        file = new File(pathname);
    }

    public XmlFile(File file) {
        this.file = file;
    }

    public ConstituencyResults toObject() {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ConstituencyResults.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            isValid = true;

            return (ConstituencyResults) unmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            isValid = false;
            System.out.println("JAXBException thrown, file invalid?");
            return null;
        }
    }

    public boolean isValid() {
        return isValid;
    }
}
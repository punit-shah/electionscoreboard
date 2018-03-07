package uk.co.bbc.datasystems.electionscoreboard.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class XmlFile {
    private File file;
    private String filename;

    public XmlFile(String filename) {
        ClassLoader classLoader = getClass().getClassLoader();
        file = new File(classLoader.getResource(filename).getFile());
        this.filename = filename;
    }

    public ConstituencyResults toObject() {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ConstituencyResults.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            return (ConstituencyResults) unmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            System.out.println("JAXBException thrown, file invalid?");
            return null;
        }
    }

    // TODO: implement validation
    public boolean isValid() {
        return true;
    }

    public File getFile() {
        return file;
    }

    public String getFilename() {
        return filename;
    }
}

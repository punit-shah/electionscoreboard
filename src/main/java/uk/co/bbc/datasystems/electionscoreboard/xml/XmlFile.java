package uk.co.bbc.datasystems.electionscoreboard.xml;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

public class XmlFile {
    private File file;
    private String filename;
    private ClassLoader classLoader;

    public XmlFile(String filename) {
        classLoader = getClass().getClassLoader();
        try {
            file = new File(classLoader.getResource(filename).getFile());
        } catch (NullPointerException e) {
            System.err.println(filename + " not found in resources.");
            return;
        }
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

    public boolean isValid() {
        File xmlSchema = new File(classLoader.getResource("constituencyResults.xsd").getFile());
        Source xmlSource = new StreamSource(file);
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        try {
            Schema schema = schemaFactory.newSchema(xmlSchema);
            Validator validator = schema.newValidator();
            validator.validate(xmlSource);
            return true;
        } catch (SAXException | IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public File getFile() {
        return file;
    }

    public String getFilename() {
        return filename;
    }
}

package uk.co.bbc.datasystems.electionscoreboard.xml;

import java.util.PrimitiveIterator;
import java.util.stream.IntStream;

public class XmlFileIterator {
    private PrimitiveIterator.OfInt fileIterator;

    public XmlFileIterator() {
        fileIterator = IntStream.rangeClosed(1, 650).iterator();
    }

    public XmlFile requestNextFile() {
        if (fileIterator.hasNext()) {
            String filename = String.format("xml-files/result%03d.xml", fileIterator.nextInt());
            return new XmlFile(filename);
        } else {
            return null;
        }
    }
}

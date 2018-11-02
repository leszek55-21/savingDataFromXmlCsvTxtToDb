package com.leszekszymaszek.documentReading;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

public class LargeXmlDatSaver implements XmlDataSaver {

    @Override
    public void readAndSaveToDb (String filePath) {

        try {
            // obtain and configure SAX based parser
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

            // getting the object for SAX parser
            SAXParser parser = saxParserFactory.newSAXParser();
            parser.parse(filePath, new MySAXhandler());

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

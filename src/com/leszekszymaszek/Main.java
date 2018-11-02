package com.leszekszymaszek;

import com.leszekszymaszek.documentReading.CsvAndTxtDataSaver;
import com.leszekszymaszek.documentReading.LargeXmlDatSaver;
import com.leszekszymaszek.documentReading.SmallXmlDataSaver;
import com.leszekszymaszek.documentReading.XmlDataSaver;
import com.leszekszymaszek.utils.fileNameFilters.CsvFileNameFilter;
import com.leszekszymaszek.utils.TagNamesAndMessages;
import com.leszekszymaszek.utils.fileNameFilters.TxtFileNameFilter;
import com.leszekszymaszek.utils.fileNameFilters.XmlFileNameFilter;

import java.io.File;
import java.util.Scanner;

public class Main {

    // == CONSTANTS ==
    public static final int MAX_FILE_SIZE = 2000;

    public static final String ENDING_STRING = "q";
    public static final String STARTING = "Starting...";
    public static final String WELCOME_MESSAGE = "Welcome! This program will allow you " +
            "to read from .csv, .txt, or .xml file and store data to the database.";
    public static final String ENTER_FILE_PATH = "\nPlease enter valid file path, or press 'q' to exit...";
    public static final String NO_FILE_WRONG_FILE = "No such file, or wrong input file. Try again... ";
    public static final String BYE = "Good Bye!";

    public static void main (String[] args) {

        // scanner - for user input
        Scanner scanner = new Scanner(System.in);

        // variable holding user input
        String filePath;

        // creating our txt, csv and xml readers
        CsvAndTxtDataSaver csvTxt;
        XmlDataSaver xml;

        // welcome message
        System.out.println(WELCOME_MESSAGE);

        // looping until user decide to leave by pressing "q", or "Q"
        do {
            System.out.println(ENTER_FILE_PATH);
            filePath = scanner.nextLine();

            // breaking the loop if user enters ending string - "q" or "Q"
            if(filePath.equalsIgnoreCase(ENDING_STRING)) {
                break;
            }
            // variable storing file type
            String fileType = TagNamesAndMessages.EMPTY_STRING;

            File file = new File(filePath);

            // file length - value of this variable will decide which kind of XML parser use
            long fileLength = file.length();

            // checking if file exist and isn't a directory
            if(file.exists() && !file.isDirectory()) {

                // checking what type of file it is
                if(new TxtFileNameFilter().accept(file, file.getName())) {
                    fileType = TagNamesAndMessages.TXT_FILE_TYPE;

                } else if (new CsvFileNameFilter().accept(file, file.getName())) {
                    fileType = TagNamesAndMessages.CSV_FILE_TYPE;

                } else if (new XmlFileNameFilter().accept(file, file.getName())) {
                    fileType = TagNamesAndMessages.XML_FILE_TYPE;

                } else {
                    System.out.println(NO_FILE_WRONG_FILE);
                }
            } else {
                System.out.println(NO_FILE_WRONG_FILE);
            }

            // switch case - selecting right file reader-saver depending on file extension and size
            switch (fileType) {
                case TagNamesAndMessages.XML_FILE_TYPE:
                    if(fileLength > MAX_FILE_SIZE) {
                        xml = new LargeXmlDatSaver();
                    } else {
                        xml = new SmallXmlDataSaver();
                    }
                    System.out.println(STARTING);
                    xml.readAndSaveToDb(filePath);
                    break;
                case TagNamesAndMessages.TXT_FILE_TYPE:
                    csvTxt = new CsvAndTxtDataSaver();
                    System.out.println(STARTING);
                    csvTxt.readAndSaveToDb(filePath);
                    break;
                case TagNamesAndMessages.CSV_FILE_TYPE:
                    csvTxt = new CsvAndTxtDataSaver();
                    System.out.println(STARTING);
                    csvTxt.readAndSaveToDb(filePath);
                    break;
            }

        // condition to break out
        } while (!filePath.equalsIgnoreCase(ENDING_STRING));
        System.out.println(BYE);
    }
}

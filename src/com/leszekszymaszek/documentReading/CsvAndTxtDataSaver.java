package com.leszekszymaszek.documentReading;

import com.leszekszymaszek.DAO.ContactDAO;
import com.leszekszymaszek.DAO.ContactDAOimpl;
import com.leszekszymaszek.DAO.CustomerDAO;
import com.leszekszymaszek.DAO.CustomerDAOimpl;
import com.leszekszymaszek.utils.TagNamesAndMessages;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CsvAndTxtDataSaver {

    // == FIELDS ==
    private CustomerDAO customerDAO;
    private ContactDAO contactDAO;

    // == CONSTRUCOTRS ==
    public CsvAndTxtDataSaver () {
        this.customerDAO = new CustomerDAOimpl();
        this.contactDAO = new ContactDAOimpl();
    }

    // == PUBLIC METHODS ==
    public void readAndSaveToDb(String filePath){

        // getting the buffered reader
        try (BufferedReader bReader = new BufferedReader(new FileReader(new File(filePath)))) {

            // each buffered reader line...
            String line;
            while ((line = bReader.readLine()) != null) {
                try {
                    if (!line.trim().equalsIgnoreCase(TagNamesAndMessages.EMPTY_STRING)) {

                        // putting each line content into an array (coma splits the data)
                        String[] array = line.split(",");

                        // smallest possible size of an array is 4 (it must contains at least
                        // name, surname, age and city otherwise file format is wrong
                        if(array.length >= 4) {

                            // getting name from position 0
                            String name = array[0];

                            // getting surname from position 1
                            String surname = array[1];

                            // getting age from position 2, if value is empty, we assign null to age
                            String age = array[2];
                            if (age.equals(TagNamesAndMessages.EMPTY_STRING) || !isNumber(age)) {
                                age = null;
                            }

                            // saving customer here
                            boolean success = customerDAO.saveCustomer(name, surname, age);

                            // for user console output purposes
                            if (success) {
                                System.out.println(TagNamesAndMessages.CUSTOMER_ADDED + name + " " + surname);
                            } else {
                                System.out.println(TagNamesAndMessages.SOMETHING_WENT_WRONG);
                            }

                            // getting last added customer ID
                            Integer currentCustomerID = customerDAO.lastCustomerID(name);

                            // lists for different type of contacts
                            List<String> phoneList = new ArrayList<>();
                            List<String> emailList = new ArrayList<>();
                            List<String> jabberList = new ArrayList<>();
                            List<String> otherContactsList = new ArrayList<>();

                            // looping through the rest of the array from 5th element to the end
                            // 4th element is city which we don't need in db
                            // we are looping only when array length is bigger than 4, which means
                            // there will be at least 1 contact
                            if (array.length > 4) {
                                for (int i = 4; i < array.length; i++) {
                                    String contact = array[i];
                                    if (contactType(contact).equals(TagNamesAndMessages.UNKNOWN)) {
                                        otherContactsList.add(contact);
                                    } else if (contactType(contact).equals(TagNamesAndMessages.EMAIL)) {
                                        emailList.add(contact);
                                    } else if (contactType(contact).equals(TagNamesAndMessages.PHONE)) {
                                        phoneList.add(contact);
                                    } else if (contactType(contact).equals(TagNamesAndMessages.JABBER)) {
                                        jabberList.add(contact);
                                    }
                                }
                            }

                            // for user output purposes
                            success = contactDAO.saveContacts(currentCustomerID, emailList, phoneList,
                                    jabberList, otherContactsList);
                            if (success) {
                                System.out.println(TagNamesAndMessages.NEW_CONTACTS_ADDED + name + " " + surname);
                            } else {
                                System.out.println(TagNamesAndMessages.SOMETHING_WENT_WRONG);
                            }
                        } else {
                            System.out.println(TagNamesAndMessages.WRONG_FILE_FORMAT);
                        }

                    }
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
            System.out.println(TagNamesAndMessages.DONE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // PRIVATE METHODS ==

    //returns 0, if contact is unknown, 1 if contact is an email, 2 if it's phone contact
    // and 3, if it's jabber contact
    private static Integer contactType(String contactString){

        // checking if it's phone number
        if(isPhone(contactString)) {

            String noSpacesContactString = contactString.replaceAll("\\s+", TagNamesAndMessages.EMPTY_STRING);
            if(noSpacesContactString.length() == TagNamesAndMessages.MIN_PHONE_NUMBER_LENGTH &&
               isNumber(noSpacesContactString)) {
               return TagNamesAndMessages.PHONE;
            }

        //checking if it's an email
        } else if(isEmail(contactString)) {
            return TagNamesAndMessages.EMAIL;

        // checking if it's jabber contact
        } else if (isJabber(contactString)) {
            return TagNamesAndMessages.JABBER;
        } else return TagNamesAndMessages.UNKNOWN;
        return TagNamesAndMessages.UNKNOWN;
    }

    // checking if number String is an Integer format number
    private static boolean isNumber(String numberString) {
        try {
            Integer.parseInt(numberString);
            return true;
        } catch(NumberFormatException nfe) {
            return false;
        }
    }

    // I assume that jabber contact is valid always, if there is NO white space between chars in it
    // and first character is small letter between a and z - hope this is correct approach :)
    private static boolean isJabber (String contactString) {
        return (!contactString.contains(" ") &&
                contactString.charAt(0) > 96 &&
                contactString.charAt(0) < 123);
    }

    // checking if contact is an email with special regular Expression
    private static boolean isEmail(String contactString) {
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(contactString);
        return matcher.matches();
    }

    // checking if it's phone number - (9 digits continuously or with space after 3rd
    // and after 6th digit. I assume those are the only valid formats for phone number
    // in this exercise
    private static boolean isPhone(String contactString) {
        return (contactString.length() == TagNamesAndMessages.MIN_PHONE_NUMBER_LENGTH ||
          (contactString.length() == TagNamesAndMessages.MAX_PHONE_NUMBER_LENGTH &&
           contactString.charAt(3) == TagNamesAndMessages.BACKSPACE_VALUE_IN_ASCII &&
           contactString.charAt(7) == TagNamesAndMessages.BACKSPACE_VALUE_IN_ASCII));
    }
}
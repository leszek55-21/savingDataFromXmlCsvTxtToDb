package com.leszekszymaszek.documentReading;

import com.leszekszymaszek.DAO.ContactDAO;
import com.leszekszymaszek.DAO.ContactDAOimpl;
import com.leszekszymaszek.DAO.CustomerDAO;
import com.leszekszymaszek.DAO.CustomerDAOimpl;
import com.leszekszymaszek.utils.TagNamesAndMessages;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class MySAXhandler extends DefaultHandler {

    // == FIELDS ==

    // flags - each will be set to true if parser will find the right element
    private boolean boolName;
    private boolean boolSurname;
    private boolean boolAge;
    private boolean boolContacts;
    private boolean boolEmail;
    private boolean boolPhone;
    private boolean boolJabber;
    private boolean boolOther;

    // fields storing matching tag contents
    private String name;
    private String surname;
    private String age;
    private String email;
    private String phone;
    private String jabber;
    private String otherContact;

    // list will contain proper contacts during parsing
    private List<String> emails;
    private List<String> phones;
    private List<String> jabbers;
    private List<String> otherContacts;

    // DAO fields for saving the data
    private CustomerDAO customerDAO;
    private ContactDAO contactDAO;

    // == CONSTRUCTORS ==
    public MySAXhandler () {
        this.customerDAO = new CustomerDAOimpl();
        this.contactDAO = new ContactDAOimpl();
    }

    // == PUBLIC METHODS ==
    @Override
    public void startElement (String uri, String localName,
                              String qName, Attributes attributes) throws SAXException {
        // when opening <name> tag found...
        if(qName.equalsIgnoreCase(TagNamesAndMessages.NAME_TAG)) {
            boolName = true;
        }

        // when opening <surname> tag found...
        if(qName.equalsIgnoreCase(TagNamesAndMessages.SURNAME_TAG)) {
            boolSurname = true;
        }

        // when opening <age> tag found...
        if(qName.equalsIgnoreCase(TagNamesAndMessages.AGE_TAG)) {
            boolAge = true;
        }

        // when opening <contacts> tag found...
        if(qName.equalsIgnoreCase(TagNamesAndMessages.CONTACTS_TAG)) {
            boolContacts = true;

            // initializing out lists for contacts so we can add found contacts to them
            emails = new ArrayList<>();
            phones = new ArrayList<>();
            jabbers = new ArrayList<>();
            otherContacts = new ArrayList<>();
        }
        // when opening <phone> tag found...
        if(qName.equalsIgnoreCase(TagNamesAndMessages.PHONE_TAG)) {
            boolPhone = true;

        // when opening <email> tag found...
        } else if(qName.equalsIgnoreCase(TagNamesAndMessages.EMAIL_TAG)) {
            boolEmail = true;

        // when opening <jabber> tag found...
        } else if(qName.equalsIgnoreCase(TagNamesAndMessages.JABBER_TAG)) {
            boolJabber = true;

        // when something else found...
        } else {
            boolOther = true;
        }
    }

    @Override
    public void endElement (String uri, String localName, String qName) throws SAXException {

        // when closing </preson> tag is found...
        if(qName.equalsIgnoreCase(TagNamesAndMessages.PERSON_TAG)) {

            if(name != null && surname != null) {
                // saving customer and do some console output for user
                if(customerDAO.saveCustomer(name, surname, age)) {
                    System.out.println(TagNamesAndMessages.CUSTOMER_ADDED + name + " " + surname);
                    age = null;
                } else {
                    System.out.println(TagNamesAndMessages.SOMETHING_WENT_WRONG);
                }
            } else {
                throw new SAXException(TagNamesAndMessages.WRONG_FILE_FORMAT);
            }


            // getting last added customer id
            Integer currentCustomerId = customerDAO.lastCustomerID(this.name);

            // saving name and surname into temp variables, so they can be used for console output
            String tempName = name;
            String tempSurname = surname;

            // resetting name and surname
            name = null;
            surname = null;

            // saving contacts for customer and do some console output for user
            // all lists must NOT be equal to null. If they are null means that there was no
            // <contacts> tag. Only when the <contacts> tag exist lists will be instantiated
            // and it was done in start element method
            if(emails != null && phones != null && jabbers != null && otherContacts != null) {

                if(contactDAO.saveContacts(currentCustomerId, emails, phones, jabbers, otherContacts)) {
                    System.out.println(TagNamesAndMessages.NEW_CONTACTS_ADDED + tempName + " " + tempSurname);
                    boolContacts = false;
                }
            } else {
                System.out.println(TagNamesAndMessages.NO_CONTACTS_DETECTED + tempName + " " + tempSurname);
            }
        }
    }

    @Override
    public void characters (char[] ch, int start, int length) throws SAXException {
        if(boolName) {
            name = new String(ch, start, length);
            boolName = false;
        }
        if(boolSurname) {
            surname = new String(ch, start, length);
            boolSurname = false;
        }
        if(boolAge) {
            age = new String(ch, start, length);
            if(age.equalsIgnoreCase("")) {
                age = null;
            }
            boolAge = false;
        }
        if(boolContacts && boolEmail) {
            email = new String(ch, start, length);
            emails.add(email);
            boolEmail = false;
        }
        if(boolContacts && boolPhone) {
            phone = new String(ch, start, length);
            phones.add(phone);
            boolPhone = false;
        }
        if(boolContacts && boolJabber) {
            jabber = new String(ch, start, length);
            jabbers.add(jabber);
            boolJabber = false;
        }
        if(boolContacts && boolOther) {
            otherContact = new String(ch, start, length);
            if(!otherContact.trim().equalsIgnoreCase(TagNamesAndMessages.EMPTY_STRING)) {
                otherContacts.add(otherContact);
            }
            boolOther = false;
        }
    }
}

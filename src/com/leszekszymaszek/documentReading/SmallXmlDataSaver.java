package com.leszekszymaszek.documentReading;

import com.leszekszymaszek.DAO.ContactDAO;
import com.leszekszymaszek.DAO.ContactDAOimpl;
import com.leszekszymaszek.DAO.CustomerDAO;
import com.leszekszymaszek.DAO.CustomerDAOimpl;
import com.leszekszymaszek.utils.TagNamesAndMessages;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SmallXmlDataSaver implements XmlDataSaver {

    // == FIELDS ==
    private CustomerDAO customerDAO;
    private ContactDAO contactDAO;

    // == CONSTRUCTORS ==
    public SmallXmlDataSaver () {
        this.customerDAO = new CustomerDAOimpl();
        this.contactDAO = new ContactDAOimpl();
    }

    // == PUBLIC METHODS ==
    @Override
    public void readAndSaveToDb(String filePath) {

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new File(filePath));
            document.normalize();

            NodeList personList = document.getElementsByTagName(TagNamesAndMessages.PERSON_TAG);

            // if xml contains <person> tags, we loop throuhg them
            if(personList.getLength() > 0) {
                for(int i = 0; i < personList.getLength(); i++) {
                    Node personNode = personList.item(i);
                    if(personNode.getNodeType() == Node.ELEMENT_NODE) {

                        // getting single person
                        Element person = (Element) personNode;

                        // getting persons child nodes
                        NodeList personDataList = person.getChildNodes();

                        // list of all tags person have
                        List<String> tagNames = new ArrayList<>();

                        // looping through person tags and add them to tagNames ArrayList
                        for(int j = 0; j < personDataList.getLength(); j++) {
                            Node personDataNode = personDataList.item(j);
                            if(personDataNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element personData = (Element) personDataNode;

                                // adding all tags to the list, later we will check, if list contains age
                                //tag, because if there will be no age tag, we need to assign null to age
                                tagNames.add(personData.getTagName());
                            }
                        }
                        // locals vars that will hold name, surname, age and contacts for the customer
                        String name = TagNamesAndMessages.EMPTY_STRING;
                        String surname = TagNamesAndMessages.EMPTY_STRING;
                        String age = null;
                        List<String> phoneList = new ArrayList<>();
                        List<String> emailList = new ArrayList<>();
                        List<String> jabberList = new ArrayList<>();
                        List<String> otherContactsList = new ArrayList<>();

                        // looping through all tags person(customer) have and assign their text content
                        // to the proper local var
                        for(int j = 0; j < personDataList.getLength(); j++) {
                            Node personDataNode = personDataList.item(j);

                            if(personDataNode.getNodeType() == Node.ELEMENT_NODE) {

                                Element personData = (Element) personDataNode;
                                String tagName = personData.getTagName();
                                String textContent = personData.getTextContent();

                                if(tagName.equals(TagNamesAndMessages.NAME_TAG)) {
                                    name = textContent;
                                } else if (tagName.equals(TagNamesAndMessages.SURNAME_TAG)) {
                                    surname = textContent;
                                } else if (tagName.equals(TagNamesAndMessages.AGE_TAG)) {
                                    age = textContent;
                                } else if (tagName.equals(TagNamesAndMessages.CONTACTS_TAG)) {
                                    NodeList contactList = personData.getChildNodes();

                                    if(contactList.getLength() > 0) {

                                        for(int k = 0; k < contactList.getLength(); k++) {
                                            Node contactNode = contactList.item(k);
                                            if(contactNode.getNodeType() == Node.ELEMENT_NODE) {
                                                Element contact = (Element) contactNode;
                                                if(contact.getTagName().equals(TagNamesAndMessages.PHONE_TAG)) {
                                                    phoneList.add(contact.getTextContent());
                                                } else if (contact.getTagName().equals(TagNamesAndMessages.EMAIL_TAG)) {
                                                    emailList.add(contact.getTextContent());
                                                } else if (contact.getTagName().equals(TagNamesAndMessages.JABBER_TAG)) {
                                                    jabberList.add(contact.getTextContent());
                                                } else {
                                                    otherContactsList.add(contact.getTextContent());
                                                }
                                            }
                                        }
                                    }
                                } else if (!tagNames.contains(TagNamesAndMessages.AGE_TAG)){
                                    age = null;
                                }
                            }
                        }

                        // saving customer and his contacts using customerDAO and contactDAO
                        // for user output purposes
                        // I assume that excercise requires that customer has at least name and surname
                        if(!name.equals(TagNamesAndMessages.EMPTY_STRING) &&
                           !surname.equals(TagNamesAndMessages.EMPTY_STRING)) {

                            // saving customer and do some console output for user
                            boolean success = customerDAO.saveCustomer(name, surname, age);
                            if(success) {
                                System.out.println(TagNamesAndMessages.CUSTOMER_ADDED + name + " " + surname);
                            } else {
                                System.out.println(TagNamesAndMessages.SOMETHING_WENT_WRONG);
                            }

                            // getting last added customer id
                            Integer lastCustomerId = customerDAO.lastCustomerID(name);

                            // saving contact and do some console output for user
                            success = contactDAO.saveContacts(lastCustomerId, emailList, phoneList,
                                    jabberList, otherContactsList);
                            if(success) {
                                System.out.println(TagNamesAndMessages.NEW_CONTACTS_ADDED + name + " " + surname);
                            } else {
                                System.out.println(TagNamesAndMessages.SOMETHING_WENT_WRONG);
                            }

                        } else {
                            System.out.println(TagNamesAndMessages.WRONG_FILE_FORMAT);
                        }
                    }
                }
                System.out.println(TagNamesAndMessages.DONE);
            } else {
                System.out.println(TagNamesAndMessages.WRONG_FILE_FORMAT);
            }


        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

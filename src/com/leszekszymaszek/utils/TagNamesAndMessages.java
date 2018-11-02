package com.leszekszymaszek.utils;

public final class TagNamesAndMessages {

    // == CONSTANTS ==
    public static final String PERSON_TAG = "person";
    public static final String NAME_TAG = "name";
    public static final String SURNAME_TAG = "surname";
    public static final String AGE_TAG = "age";
    public static final String CONTACTS_TAG = "contacts";
    public static final String EMAIL_TAG = "email";
    public static final String PHONE_TAG = "phone";
    public static final String JABBER_TAG = "jabber";

    public static final String CUSTOMER_ADDED = "Added new customer ";
    public static final String SOMETHING_WENT_WRONG = "Something went wrong";
    public static final String NEW_CONTACTS_ADDED = "Added contacts for customer ";
    public static final String WRONG_FILE_FORMAT = "Wrong file format!";
    public static final String DONE = "Done!";
    public static final String EMPTY_STRING = "";
    public static final String NO_CONTACTS_DETECTED = "No contacts detected for user ";

    public static final String XML_FILE_TYPE = ".xml";
    public static final String TXT_FILE_TYPE = ".txt";
    public static final String CSV_FILE_TYPE = ".csv";

    // values representing contact types 0 - unknown, 1 - email, 2 - phone, 3- jabber
    public static final Integer UNKNOWN = 0;
    public static final Integer EMAIL = 1;
    public static final Integer PHONE = 2;
    public static final Integer JABBER = 3;

    public static final Integer BACKSPACE_VALUE_IN_ASCII = 32;
    public static final Integer MIN_PHONE_NUMBER_LENGTH = 9;
    public static final Integer MAX_PHONE_NUMBER_LENGTH = 11;

    // == CONSTRUCTORS ==
    private TagNamesAndMessages() {}

}

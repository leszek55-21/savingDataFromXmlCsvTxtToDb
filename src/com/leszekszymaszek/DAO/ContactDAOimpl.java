package com.leszekszymaszek.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import com.leszekszymaszek.dbConnectionSetup.ConnectionManager;
import com.leszekszymaszek.utils.TagNamesAndMessages;

public class ContactDAOimpl implements ContactDAO {

    // == CONSTANTS ==
    // creating sql string for adding to contacts table
    private static final String INSERT_CONTACTS = "INSERT INTO customer_data.contacts (ID_CUSTOMER, TYPE, CONTACT)" +
            " VALUES(?,?,?)";

    // == FIELDS ==
    private Connection connection = ConnectionManager.getConnection();

    // == PUBLIC METHODS ==
    @Override
    public boolean saveContacts (Integer customerID, List<String> emails,
                                List<String> phones, List<String> jabbers, List<String> otherContacts) {

        // saving contacts type by type
        boolean emailsSaved = saveSingleTypeContacts(customerID, TagNamesAndMessages.EMAIL, emails);
        boolean phonesSaved = saveSingleTypeContacts(customerID, TagNamesAndMessages.PHONE, phones);
        boolean jabbersSaved = saveSingleTypeContacts(customerID, TagNamesAndMessages.JABBER, jabbers);
        boolean otherContactsSaved = saveSingleTypeContacts(customerID, TagNamesAndMessages.UNKNOWN, otherContacts);

        return emailsSaved && phonesSaved && jabbersSaved && otherContactsSaved;
    }

    //  saving single type contacts (those inside a contactList)
    private boolean saveSingleTypeContacts(Integer customerId, Integer contactType,
                                        List<String> contactList){
        PreparedStatement preparedStatement;
        if(!contactList.isEmpty()) {
            try {
                for(String contact: contactList) {

                    preparedStatement = connection.prepareStatement(INSERT_CONTACTS);
                    preparedStatement.setInt(1, customerId);
                    preparedStatement.setInt(2, contactType);
                    preparedStatement.setString(3, contact);

                    // executing and closing
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        } else {
            return true;
        }
    }
}

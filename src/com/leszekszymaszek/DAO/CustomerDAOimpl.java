package com.leszekszymaszek.DAO;

import com.leszekszymaszek.dbConnectionSetup.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAOimpl implements CustomerDAO{

    // == CONSTANTS ==
    // creating sql string for adding to customers table
    private static final String INSERT_CUSTOMER = "INSERT INTO customer_data.customers (NAME, SURNAME, Age)" +
            " VALUES(?,?,?)";

    // creating sql string for getting the ID of last added person
    private static final String GET_CUSTOMER_ID = "SELECT ID FROM customer_data.customers WHERE name=? ORDER BY ID DESC LIMIT 1";

    // == FILEDS ++
    private Connection connection = ConnectionManager.getConnection();
    private PreparedStatement preparedStatement = null;

    // == PUBLIC METHODS ==

    // saving customer - pretty self-explanatory - preparing statement, setting params that will
    // replace "?" in query string, then executing update and closing statement
    @Override
    public boolean saveCustomer (String name, String surname, String age) {
        try {
            preparedStatement = connection.prepareStatement(INSERT_CUSTOMER);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, age);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // getting id of last added customer
    @Override
    public Integer lastCustomerID (String name) {
        Integer currentCustomerID = null;
        try {
            preparedStatement = connection.prepareStatement(GET_CUSTOMER_ID);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            // getting last added user ID and store it in currentCustomerID
            while(resultSet.next()) {
                currentCustomerID = resultSet.getInt(1);

            }
            return currentCustomerID;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

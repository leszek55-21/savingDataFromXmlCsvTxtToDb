package com.leszekszymaszek.DAO;

public interface CustomerDAO {

    boolean saveCustomer(String name, String surname, String age);

    Integer lastCustomerID(String name);
}

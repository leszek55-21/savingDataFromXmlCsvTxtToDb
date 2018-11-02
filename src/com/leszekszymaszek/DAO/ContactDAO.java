package com.leszekszymaszek.DAO;

import java.util.List;

public interface ContactDAO {

    boolean saveContacts(Integer customerID, List<String> emails, List<String> phones,
                        List<String> jabbers, List<String> other);

}

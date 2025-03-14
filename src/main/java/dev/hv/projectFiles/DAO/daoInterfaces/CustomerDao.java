package dev.hv.projectFiles.DAO.daoInterfaces;

import dev.hv.model.ICustomer;

import java.util.List;

/**
 * Interface welches die Funktionen für die CustomerDao beschreibt.
 */
public interface CustomerDao<User> {
    void addCustomer(User user);
    ICustomer getCustomerById(String id);
    List<User> getAllCustomers();
    void updateCustomer(User user);
    void deleteCustomer(String id);
}

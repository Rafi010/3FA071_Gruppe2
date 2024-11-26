package dev.hv.projectFiles.DAO.daoInterfaces;

import dev.hv.model.ICustomer;

import java.util.List;

public interface CustomerDao<User> {
    void addUser(User user);
    ICustomer getUserById(String id);
    List<User> getAllUsers();
    void updateUser(User user);
    void deleteUser(String id);
}

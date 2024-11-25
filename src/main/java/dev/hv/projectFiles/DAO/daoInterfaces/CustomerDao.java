package dev.hv.projectFiles.DAO.daoInterfaces;

import java.util.List;

public interface CustomerDao<User> {
    void addUser(User user);
    User getUserById(int id);
    List<User> getAllUsers();
    void updateUser(User user);
    void deleteUser(int id);
}

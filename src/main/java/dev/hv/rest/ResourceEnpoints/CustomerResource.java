package dev.hv.rest.ResourceEnpoints;

import dev.hv.model.ICustomer;
import dev.hv.projectFiles.DAO.daoImplementation.CustomerDaoImpl;
import dev.hv.projectFiles.DAO.daoInterfaces.CustomerDao;
import dev.hv.projectFiles.DAO.entities.User;
import dev.hv.projectFiles.DatabaseConnection;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.time.LocalDate;
import java.util.Properties;
import java.util.UUID;

@Path("/customer")
public class CustomerResource {
    DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
    Properties properties = new Properties();


    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Customer Page";
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/sayHello")
    public String sayHello(){
        databaseConnection.openConnection(properties);
        CustomerDao customerDao = new CustomerDaoImpl(databaseConnection.getConnection());
        User user1 = new User();
        user1.setId(UUID.fromString("e00b1287-40a9-452a-8277-018e1682f9e0"));
        user1.setFirstName("Rafi");
        user1.setLastName("Rauch");
        user1.setGender(ICustomer.Gender.M);
        user1.setBirthDate(LocalDate.of(2006, 5, 2));
        customerDao.addUser(user1);
        databaseConnection.closeConnection();
        return "added user";
    }

}

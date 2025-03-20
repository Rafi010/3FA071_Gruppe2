package dev.hv.projectFiles.DAO.entities;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.hv.model.ICustomer;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.UUID;

@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonTypeName(value = "customer")
public class Customer implements ICustomer {

    @NotEmpty(message = "First name cannot be empty")  // Hibernate-specific annotation
    @Length(min = 1, max = 50, message = "First name must be between 1 and 50 characters")  // Hibernate-specific annotation
    String firstName;

    @NotEmpty(message = "Last name cannot be empty")  // Hibernate-specific annotation
    @Length(min = 1, max = 50, message = "Last name must be between 1 and 50 characters")  // Hibernate-specific annotation
    String lastName;

    LocalDate birthdate;

    @NotNull(message = "Gender cannot be empty")  // Hibernate-specific annotation
    Gender gender;

    UUID id;

    @Override
    public LocalDate getBirthDate() {
        return birthdate;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public Gender getGender() {
        return gender;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setBirthDate(LocalDate birthDate) {
        this.birthdate = birthDate;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public java.util.UUID getId() {
        return id;
    }
    
    @Override
    public void setId(java.util.UUID id) {
        this.id = id;
    }
}

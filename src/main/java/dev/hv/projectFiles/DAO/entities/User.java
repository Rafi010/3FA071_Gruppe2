package dev.hv.projectFiles.DAO.entities;

import dev.hv.model.ICustomer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

public class User implements ICustomer {

    @NotEmpty(message = "First name cannot be empty")  // Hibernate-specific annotation
    @Length(min = 1, max = 50, message = "First name must be between 1 and 50 characters")  // Hibernate-specific annotation
    String firstName;

    @NotEmpty(message = "Last name cannot be empty")  // Hibernate-specific annotation
    @Length(min = 1, max = 50, message = "Last name must be between 1 and 50 characters")  // Hibernate-specific annotation
    String lastName;

    LocalDate birthdate;

    @NotNull(message = "Gender cannot be empty")  // Hibernate-specific annotation
    Gender gender;

    java.util.UUID id;

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

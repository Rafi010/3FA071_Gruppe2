package dev.hv.projectFiles.DAO.entities;

import dev.hv.model.ICustomer;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public class User implements ICustomer {

    @NotNull(message = "First name cannot be null")
    @Size(min = 1, message = "First name cannot be empty")
    String firstName;

    @NotNull(message = "Last name cannot be null")
    @Size(min = 1, message = "Last name cannot be empty")
    String lastName;

    @NotNull(message = "Birthdate cannot be null")
    @Past(message = "Birthdate must be in the past")
    LocalDate birthdate;

    @NotNull(message = "Gender cannot be null")
    Gender gender;

    @NotNull(message = "ID cannot be null")
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
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }
}

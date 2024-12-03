package dev.hv.projectFiles.DAO.entities;

import dev.hv.model.ICustomer;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public class User implements ICustomer {

        String firstName;
        String lastName;
        LocalDate birthdate;
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
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }
}

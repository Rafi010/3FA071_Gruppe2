package dev.hv.projectFiles.DAO.entities;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.hv.model.ICustomer;
import dev.hv.model.IReading;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonTypeName(value = "reading")
public class Reading implements IReading {

    String comment;

    @JsonDeserialize(as = Customer.class)
    @NotNull(message = "Customer cannot be empty")  // Hibernate-specific annotation
    ICustomer customer;

    @NotNull(message = "dateOfReading cannot be empty")  // Hibernate-specific annotation
    LocalDate dateOfReading;

    @NotNull(message = "kindOfMeter cannot be empty")  // Hibernate-specific annotation
    KindOfMeter kindOfMeter;

    @NotNull(message = "meterCount cannot be empty")  // Hibernate-specific annotation
    Double meterCount;

    @NotNull(message = "meterId cannot be empty")  // Hibernate-specific annotation
    String meterId;

    @NotNull(message = "substitute cannot be empty")  // Hibernate-specific annotation
    Boolean substitute;

    UUID id;


    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public ICustomer getCustomer() {
        return customer;
    }

    @Override
    public LocalDate getDateOfReading() {
        return dateOfReading;
    }

    @Override
    public KindOfMeter getKindOfMeter() {
        return kindOfMeter;
    }

    @Override
    public Double getMeterCount() {
        return meterCount;
    }

    @Override
    public String getMeterId() {
        return meterId;
    }

    @Override
    public Boolean getSubstitute() {
        return substitute;
    }

    @Override
    public String printDateOfReading() {
        System.out.println(toString(dateOfReading));
        return toString(dateOfReading);
    }

    private String toString(LocalDate dateOfReading) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return dateOfReading.format(formatter);
    }


    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public void setCustomer(ICustomer customer) {
        this.customer = customer;
    }

    @Override
    public void setDateOfReading(LocalDate dateOfReading) {
        this.dateOfReading = dateOfReading;
    }

    @Override
    public void setKindOfMeter(KindOfMeter kindOfMeter) {
        this.kindOfMeter = kindOfMeter;
    }

    @Override
    public void setMeterCount(Double meterCount) {
        this.meterCount = meterCount;
    }

    @Override
    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }

    @Override
    public void setSubstitute(Boolean substitute) {
        this.substitute = substitute;
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

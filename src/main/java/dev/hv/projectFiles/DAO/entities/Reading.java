package dev.hv.projectFiles.DAO.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.hv.model.ICustomer;
import dev.hv.model.IReading;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Reading implements IReading {


    @JsonProperty("comment")
    String comment;

    @NotNull(message = "Customer cannot be empty")  // Hibernate-specific annotation
    @JsonProperty("customer")
    ICustomer customer;

    @NotNull(message = "dateOfReading cannot be empty")  // Hibernate-specific annotation
    @JsonProperty("dateOfReading")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate dateOfReading;

    @NotNull(message = "kindOfMeter cannot be empty")  // Hibernate-specific annotation
    @JsonProperty("kindOfMeter")
    KindOfMeter kindOfMeter;

    @NotNull(message = "meterCount cannot be empty")  // Hibernate-specific annotation
    @JsonProperty("meterCount")
    Double meterCount;

    @NotNull(message = "meterId cannot be empty")  // Hibernate-specific annotation
    @JsonProperty("meterId")
    String meterId;

    @NotNull(message = "substitute cannot be empty")  // Hibernate-specific annotation
    @JsonProperty("substitute")
    Boolean substitute;

    @JsonProperty("id")
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

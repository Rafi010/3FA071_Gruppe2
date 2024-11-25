package dev.hv.projectFiles.DAO;

import java.time.LocalDate;

public class test {

    public static void main(String[] args) {
        Reading reading = new Reading();
        reading.setDateOfReading(LocalDate.parse("2023-12-21"));
        reading.printDateOfReading();
    }

}

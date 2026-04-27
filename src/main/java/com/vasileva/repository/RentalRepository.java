package com.vasileva.repository;

import com.vasileva.config.SessionCreator;
import com.vasileva.entity.Customer;
import com.vasileva.entity.Inventory;
import com.vasileva.entity.Rental;
import com.vasileva.entity.Staff;
import org.hibernate.Session;

import java.time.LocalDateTime;

public class RentalRepository extends BaseRepository<Rental> {

    public RentalRepository(SessionCreator sessionCreator) {
        super(sessionCreator, Rental.class);
    }

    public Rental createRental(Customer customer, Staff staff, Inventory inventory) {
        Session session = getSession();
        Rental rental = Rental.builder()
                .rentalDate(LocalDateTime.now())
                .customer(customer)
                .staff(staff)
                .inventory(inventory)
                .build();
        session.persist(rental);
        session.flush();
        return rental;
    }

    public void updateReturnDate(Rental rental) {
        Session session = getSession();
        if (rental.getReturnDate() == null) {
            rental.setReturnDate(LocalDateTime.now());
            session.merge(rental);
            System.out.println("Film successfully returned: rental ID = " + rental.getId());
        } else {
            System.out.println("Film has already been returned on " + rental.getReturnDate());
        }
    }
}

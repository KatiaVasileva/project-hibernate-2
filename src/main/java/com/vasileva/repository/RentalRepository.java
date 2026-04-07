package com.vasileva.repository;

import com.vasileva.config.SessionCreator;
import com.vasileva.entity.Customer;
import com.vasileva.entity.Inventory;
import com.vasileva.entity.Rental;
import com.vasileva.entity.Staff;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;

public class RentalRepository extends BaseRepository<Rental> {

    public RentalRepository(SessionCreator sessionCreator) {
        super(sessionCreator, Rental.class);
    }

    @Transactional
    public Rental createRental(Customer customer, Staff staff, Inventory inventory) {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Rental rental = Rental.builder()
                .rentalDate(LocalDateTime.now())
                .customer(customer)
                .staff(staff)
                .inventory(inventory)
                .build();
        session.persist(rental);
        session.flush();
        transaction.commit();
        return rental;
    }

    @Transactional
    public void updateReturnDate(Rental rental) {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        if (rental.getReturnDate() == null) {
            rental.setReturnDate(LocalDateTime.now());
            session.merge(rental);
            transaction.commit();
            System.out.println("Film successfully returned: rental ID = " + rental.getId());
        } else {
            System.out.println("Film has already been returned on " + rental.getReturnDate());
        }
    }
}

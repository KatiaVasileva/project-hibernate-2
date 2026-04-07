package com.vasileva.repository;

import com.vasileva.config.SessionCreator;
import com.vasileva.entity.Customer;
import com.vasileva.entity.Payment;
import com.vasileva.entity.Rental;
import com.vasileva.entity.Staff;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;

public class PaymentRepository extends BaseRepository<Payment> {

    public PaymentRepository(SessionCreator sessionCreator) {
        super(sessionCreator, Payment.class);
    }

    @Transactional
    public void createPayment(Customer customer, Staff staff, Rental rental, BigDecimal rentalRate) {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Payment payment = Payment.builder()
                .customer(customer)
                .staff(staff)
                .rental(rental)
                .amount(rentalRate)
                .build();
        session.persist(payment);
        session.flush();
        transaction.commit();
    }
}

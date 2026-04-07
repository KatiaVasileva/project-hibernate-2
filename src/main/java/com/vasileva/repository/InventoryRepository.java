package com.vasileva.repository;

import com.vasileva.config.SessionCreator;
import com.vasileva.entity.Inventory;
import com.vasileva.entity.Rental;
import org.hibernate.Session;

public class InventoryRepository extends BaseRepository<Inventory> {

    public InventoryRepository(SessionCreator sessionCreator) {
        super(sessionCreator, Inventory.class);
    }

    public boolean isInventoryAvailable(int inventoryId) {
        Session session = getCurrentSession();
        Rental latestRental = session.createQuery(
                        "select r from Rental r where inventory.id = :inventoryId " +
                                "order by rentalDate desc", Rental.class)
                .setParameter("inventoryId", inventoryId)
                .setMaxResults(1)
                .uniqueResult();
        return latestRental == null || latestRental.getReturnDate() != null;
    }
}

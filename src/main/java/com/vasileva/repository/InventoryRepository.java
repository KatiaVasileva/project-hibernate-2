package com.vasileva.repository;

import com.vasileva.config.SessionCreator;
import com.vasileva.entity.Film;
import com.vasileva.entity.Inventory;
import com.vasileva.entity.Rental;
import com.vasileva.entity.Store;
import org.hibernate.Session;

public class InventoryRepository extends BaseRepository<Inventory> {

    public InventoryRepository(SessionCreator sessionCreator) {
        super(sessionCreator, Inventory.class);
    }

    public void createInventory(Film film, Store store) {
        Session session = getSession();
        Inventory inventory = Inventory.builder()
                .film(film)
                .store(store)
                .build();
        session.persist(inventory);
        session.flush();
    }

    public boolean isInventoryAvailable(int inventoryId) {
        Session session = getSession();
        Rental latestRental = session.createQuery(
                        "select r from Rental r where inventory.id = :inventoryId " +
                                "order by rentalDate desc", Rental.class)
                .setParameter("inventoryId", inventoryId)
                .setMaxResults(1)
                .uniqueResult();
        return latestRental == null || latestRental.getReturnDate() != null;
    }
}

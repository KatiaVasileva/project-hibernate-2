package com.vasileva.repository;

import com.vasileva.config.SessionCreator;
import com.vasileva.entity.Staff;

public class StaffRepository extends BaseRepository<Staff> {
    public StaffRepository(SessionCreator sessionCreator) {
        super(sessionCreator, Staff.class);
    }
}

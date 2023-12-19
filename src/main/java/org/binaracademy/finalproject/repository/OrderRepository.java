package org.binaracademy.finalproject.repository;

import org.binaracademy.finalproject.model.Course;
import org.binaracademy.finalproject.model.Order;
import org.binaracademy.finalproject.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    Boolean existsByUsers(Users users);

    Boolean existsByCourse(Course course);

    @Modifying
    void deleteByUsers(Users users);
}
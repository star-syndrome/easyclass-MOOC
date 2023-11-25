package org.binaracademy.finalproject.repository;

import org.binaracademy.finalproject.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {

}
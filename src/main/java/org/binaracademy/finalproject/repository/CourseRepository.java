package org.binaracademy.finalproject.repository;

import org.binaracademy.finalproject.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {

    Optional<Course> findByCodeCourse(String codeCourse);

    Optional<Course> findByTitleCourse(String titleCourse);

    @Modifying
    @Query(nativeQuery = true, value = "delete from course where code_course = :codeCourse")
    void deleteByCode(@Param("codeCourse") String codeCourse);

    @Query(nativeQuery = true, value = "select c.* from course c join orders o on o.course_id = c.id where o.user_id = :userId")
    List<Optional<Course>> getCourse(@Param("userId") Long id);

    @Query(nativeQuery = true, value = "select count(*) > 0 from orders o where o.user_id = :userId and o.course_id = :courseId")
    Boolean hasOrder(@Param("userId") Long id, @Param("courseId") String courseId);

    @Query(nativeQuery = true, value = "select * from course c where c.title_course like %:title%")
    List<Course> searchingCourse(@Param("title") String title);
}
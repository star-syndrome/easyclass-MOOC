package org.binaracademy.finalproject.repository;

import org.binaracademy.finalproject.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {

    Optional<Course> findByCode(String codeCourse);

    Boolean existsByCode(String code);

    @Modifying
    void deleteByCode(String code);

    @Query("SELECT c FROM Course c JOIN Order o ON o.course.id = c.id WHERE o.users.id = :id")
    List<Course> getCourse(Long id);

    @Query("SELECT COUNT(*) > 0 FROM Order o WHERE o.users.id = :user AND o.course.id = :course")
    Boolean hasOrder(Long user, String course);

    @Query(nativeQuery = true, value = "select * from course c where c.title_course like %:title%")
    List<Course> searchingCourse(String title);

    @Query(nativeQuery = true, value = "select c.* from course c join orders o on o.course_id = c.id where o.user_id = :userId and c.title_course like %:title%")
    List<Course> searchingCourseAfterOrder(Long userId, String title);

    @Query(nativeQuery = true, value = "select c.* from course c join course_category cc on cc.course_id = c.id where cc.category_id = 1")
    List<Course> filterBackEnd();

    @Query(nativeQuery = true, value = "select c.* from course c join course_category cc on cc.course_id = c.id where cc.category_id = 2")
    List<Course> filterFrontEnd();

    @Query(nativeQuery = true, value = "select c.* from course c join course_category cc on cc.course_id = c.id where cc.category_id = 3")
    List<Course> filterFullStack();

    @Query(nativeQuery = true, value = "select * from course")
    Page<Course> getAllCourseWithPagination(Pageable pageable);

    @Query(nativeQuery = true, value = "select * from course c where c.level_course = 'Advanced'")
    List<Course> filterAdvanced();

    @Query(nativeQuery = true, value = "select * from course c where c.level_course = 'Beginner'")
    List<Course> filterBeginner();

    @Query(nativeQuery = true, value = "select * from course c where c.level_course = 'Intermediate'")
    List<Course> filterIntermediate();

    @Query(nativeQuery = true, value = "select * from course c where c.is_premium = true")
    List<Course> filterCoursePremium();

    @Query(nativeQuery = true, value = "select * from course c where c.is_premium = false")
    List<Course> filterCourseFree();

    @Query(nativeQuery = true, value = "select c.* from course c join orders o on o.course_id = c.id join course_category cc on cc.course_id = c.id where o.user_id = :userId and cc.category_id = 1")
    List<Course> filterBackendAfterOrder(Long userId);

    @Query(nativeQuery = true, value = "select c.* from course c join orders o on o.course_id = c.id join course_category cc on cc.course_id = c.id where o.user_id = :userId and cc.category_id = 2")
    List<Course> filterFrontendAfterOrder(Long userId);

    @Query(nativeQuery = true, value = "select c.* from course c join orders o on o.course_id = c.id join course_category cc on cc.course_id = c.id where o.user_id = :userId and cc.category_id = 3")
    List<Course> filterFullstackAfterOrder(Long userId);
}
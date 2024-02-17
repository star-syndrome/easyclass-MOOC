package org.binaracademy.finalproject.repository;

import org.binaracademy.finalproject.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {

    Optional<Course> findByCode(String codeCourse);

    Boolean existsByCode(String code);

    Optional<Course> getCourseByCode(String codeCourse);

    @Modifying
    @Query("DELETE FROM Course WHERE code = :code")
    void deleteByCode(@Param("code") String codeCourse);

    @Query("SELECT c FROM Course c JOIN Order o ON o.course.id = c.id WHERE o.users.id = :ID")
    List<Course> getCourse(@Param("ID") Long id);

    @Query("SELECT COUNT(*) > 0 FROM Order o WHERE o.users.id = :ID AND o.course.id = :ID")
    Boolean hasOrder(@Param("ID") Long id, @Param("ID") String courseId);

    @Query(nativeQuery = true, value = "select * from course c where c.title_course like %:title%")
    List<Course> searchingCourse(@Param("title") String title);

    @Query(nativeQuery = true, value = "select c.* from course c join orders o on o.course_id = c.id where o.user_id = :userId and c.title_course like %:title%")
    List<Course> searchingCourseAfterOrder(@Param("userId") Long userId, @Param("title") String title);

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
    List<Course> filterBackendAfterOrder(@Param("userId") Long userId);

    @Query(nativeQuery = true, value = "select c.* from course c join orders o on o.course_id = c.id join course_category cc on cc.course_id = c.id where o.user_id = :userId and cc.category_id = 2")
    List<Course> filterFrontendAfterOrder(@Param("userId") Long userId);

    @Query(nativeQuery = true, value = "select c.* from course c join orders o on o.course_id = c.id join course_category cc on cc.course_id = c.id where o.user_id = :userId and cc.category_id = 3")
    List<Course> filterFullstackAfterOrder(@Param("userId") Long userId);
}
package org.binaracademy.finalproject.repository;

import org.binaracademy.finalproject.model.Course;
import org.binaracademy.finalproject.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, String> {

    Optional<Subject> findByCode(String code);

    Boolean existsByCode(String code);

    @Modifying
    void deleteByCourse(Course course);

    @Modifying
    @Query("DELETE FROM Subject WHERE code = :code")
    void deleteSubjectByCode(@Param("code") String code);
}
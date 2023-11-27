package org.binaracademy.finalproject.repository;

import org.binaracademy.finalproject.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, String> {

    Optional<Subject> findByTitle(String title);

    @Modifying
    @Query(nativeQuery = true, value = "delete from subject where title = :title")
    void deleteSubjectByTitle(@Param("title") String title);
}
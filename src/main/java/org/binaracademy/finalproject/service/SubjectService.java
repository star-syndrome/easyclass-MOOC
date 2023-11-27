package org.binaracademy.finalproject.service;

import org.binaracademy.finalproject.DTO.SubjectDTO;
import org.binaracademy.finalproject.model.Subject;

import java.util.List;

public interface SubjectService {

    void addSubject (Subject subject);

    List<SubjectDTO> getAllSubject();

    SubjectDTO updateSubject(Subject subject, String title);

    void deleteSubjectByTitle(String title);
}
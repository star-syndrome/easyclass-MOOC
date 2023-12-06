package org.binaracademy.finalproject.service;

import org.binaracademy.finalproject.model.response.SubjectResponse;
import org.binaracademy.finalproject.model.Subject;

import java.util.List;

public interface SubjectService {

    SubjectResponse addSubject (Subject subject);

    List<SubjectResponse> getAllSubject();

    SubjectResponse updateSubject(Subject subject, String code);

    void deleteSubjectByCode(String code);
}
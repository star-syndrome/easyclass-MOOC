package org.binaracademy.finalproject.service;

import org.binaracademy.finalproject.model.request.UpdateSubjectRequest;
import org.binaracademy.finalproject.model.response.SubjectResponse;
import org.binaracademy.finalproject.model.Subject;
import org.binaracademy.finalproject.model.response.SubjectResponseAdmin;

import java.util.List;

public interface SubjectService {

    SubjectResponse addSubject (Subject subject);

    List<SubjectResponseAdmin> getAllSubject();

    SubjectResponseAdmin getSubject(String code);

    SubjectResponse updateSubject(UpdateSubjectRequest subject, String code);

    void deleteSubjectByCode(String code);
}
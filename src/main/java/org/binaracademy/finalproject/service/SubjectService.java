package org.binaracademy.finalproject.service;

import org.binaracademy.finalproject.model.request.CreateSubjectRequest;
import org.binaracademy.finalproject.model.request.UpdateSubjectRequest;
import org.binaracademy.finalproject.model.response.SubjectResponse;
import org.binaracademy.finalproject.model.response.SubjectResponseAdmin;

import java.util.List;

public interface SubjectService {

    SubjectResponse addSubject (CreateSubjectRequest request);

    List<SubjectResponseAdmin> getAllSubject();

    SubjectResponseAdmin getSubject(String code);

    SubjectResponse updateSubject(UpdateSubjectRequest request, String code);

    void deleteSubjectByCode(String code);

    void deleteByCourseCode(String code);
}
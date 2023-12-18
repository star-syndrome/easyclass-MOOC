package org.binaracademy.finalproject.service.implement;

import lombok.extern.slf4j.Slf4j;
import org.binaracademy.finalproject.model.request.UpdateSubjectRequest;
import org.binaracademy.finalproject.model.response.SubjectResponse;
import org.binaracademy.finalproject.model.Subject;
import org.binaracademy.finalproject.model.response.SubjectResponseAdmin;
import org.binaracademy.finalproject.repository.SubjectRepository;
import org.binaracademy.finalproject.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class SubjectServiceImplements implements SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    private SubjectResponse toSubjectResponse(Subject subject) {
        return SubjectResponse.builder()
                .title(subject.getTitle())
                .code(subject.getCode())
                .description(subject.getDescription())
                .isPremium(subject.getIsPremium())
                .link(subject.getLinkVideo())
                .build();
    }

    @Override
    public SubjectResponse addSubject(Subject subject) {
        log.info("Process of adding new subject");
        Optional.ofNullable(subject)
                .map(newProduct -> subjectRepository.save(subject))
                .map(result -> {
                    boolean isSuccess = true;
                    log.info("Successfully added a new subject with name: {}", subject.getTitle());
                    return isSuccess;
                })
                .orElseGet(() -> {
                    log.info("Failed to add new subject");
                    return Boolean.FALSE;
                });
        assert subject != null;
        log.info("Process of adding a new subject is complete, new subject: {}", subject.getTitle());
        return toSubjectResponse(subject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubjectResponseAdmin> getAllSubject() {
        log.info("Getting all of list subject!");
        return subjectRepository.findAll().stream()
                .map(subject -> SubjectResponseAdmin.builder()
                        .id(subject.getId())
                        .code(subject.getCode())
                        .title(subject.getTitle())
                        .description(subject.getDescription())
                        .link(subject.getLinkVideo())
                        .isPremium(subject.getIsPremium())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SubjectResponseAdmin getSubject(String code) {
        log.info("Success getting subject where subject code: {}", code);
        return subjectRepository.findByCode(code)
                .map(subject -> SubjectResponseAdmin.builder()
                        .id(subject.getId())
                        .code(subject.getCode())
                        .title(subject.getTitle())
                        .description(subject.getDescription())
                        .link(subject.getLinkVideo())
                        .isPremium(subject.getIsPremium())
                        .build())
                .orElseThrow(() -> new RuntimeException("Subject not found!"));
    }

    @Override
    public SubjectResponse updateSubject(UpdateSubjectRequest updateSubject, String code) {
        try {
            log.info("Process updating subject");
            Subject subjects = subjectRepository.findByCode(code)
                    .orElseThrow(() -> new RuntimeException("Subject not found"));

            subjects.setTitle(updateSubject.getTitle() == null ? subjects.getTitle() : updateSubject.getTitle());
            subjects.setCode(updateSubject.getCode() == null ? subjects.getCode() : updateSubject.getCode());
            subjects.setDescription(updateSubject.getDescription() == null ? subjects.getDescription() : updateSubject.getDescription());
            subjects.setLinkVideo(updateSubject.getLinkVideo() == null ? subjects.getLinkVideo() : updateSubject.getLinkVideo());
            subjects.setIsPremium(updateSubject.getIsPremium() == null ? subjects.getIsPremium() : updateSubject.getIsPremium());
            subjectRepository.save(subjects);
            log.info("Updating subject with code: " + code + " successfully!");

            return toSubjectResponse(subjects);
        } catch (Exception e) {
            log.error("Update Subject Failed");
            throw e;
        }
    }

    @Override
    public void deleteSubjectByCode(String code) {
        try {
            if (!subjectRepository.existsByCode(code)) {
                throw new RuntimeException("Subject not found");
            }
            log.info("Deleted subject where subject code: " + code + " successfully!");
            subjectRepository.deleteSubjectByCode(code);
        } catch (Exception e) {
            log.error("Deleting subject failed, please try again!");
            throw e;
        }
    }
}
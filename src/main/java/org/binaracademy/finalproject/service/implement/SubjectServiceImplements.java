package org.binaracademy.finalproject.service.implement;

import lombok.extern.slf4j.Slf4j;
import org.binaracademy.finalproject.model.request.CreateSubjectRequest;
import org.binaracademy.finalproject.model.request.UpdateSubjectRequest;
import org.binaracademy.finalproject.model.response.SubjectResponse;
import org.binaracademy.finalproject.model.Subject;
import org.binaracademy.finalproject.model.response.SubjectResponseAdmin;
import org.binaracademy.finalproject.repository.CourseRepository;
import org.binaracademy.finalproject.repository.SubjectRepository;
import org.binaracademy.finalproject.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class SubjectServiceImplements implements SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private CourseRepository courseRepository;

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
    public SubjectResponse addSubject(CreateSubjectRequest request) {
        log.info("Process of adding new subject");
        try {
            if (!courseRepository.existsById(request.getCourse().getId())) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found!");
            }

            if (subjectRepository.existsByCode(request.getCode())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Subject code already exists");
            }

            Subject subject = new Subject();
            subject.setCourse(request.getCourse());
            subject.setTitle(request.getTitle());
            subject.setCode(request.getCode());
            subject.setLinkVideo(request.getLinkVideo());
            subject.setDescription(request.getDescription());
            subject.setIsPremium(request.getIsPremium());

            subjectRepository.save(subject);
            log.info("Success adding new subject");

            return toSubjectResponse(subject);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public SubjectResponse updateSubject(UpdateSubjectRequest request, String code) {
        try {
            log.info("Process updating subject");
            Subject subjects = subjectRepository.findByCode(code)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found"));

            if (subjectRepository.countByCodeForUpdate(request.getCode(), code)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Subject code already exists!");
            }

            subjects.setTitle(request.getTitle() == null ? subjects.getTitle() : request.getTitle());
            subjects.setCode(request.getCode() == null ? subjects.getCode() : request.getCode());
            subjects.setDescription(request.getDescription() == null ? subjects.getDescription() : request.getDescription());
            subjects.setLinkVideo(request.getLinkVideo() == null ? subjects.getLinkVideo() : request.getLinkVideo());
            subjects.setIsPremium(request.getIsPremium() == null ? subjects.getIsPremium() : request.getIsPremium());

            subjectRepository.save(subjects);
            log.info("Updating subject with code: {}", code + " successfully!");

            return toSubjectResponse(subjects);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw e;
        }
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
        try {
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
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found!"));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteSubjectByCode(String code) {
        try {
            log.info("Trying to delete the subject!");
            if (!subjectRepository.existsByCode(code)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found");
            }
            log.info("Deleted Subject where subject code: {}", code + " successfully!");
            subjectRepository.deleteSubjectByCode(code);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteByCourseCode(String code) {
        subjectRepository.deleteByCourse(courseRepository.findByCode(code)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found!")));
    }
}
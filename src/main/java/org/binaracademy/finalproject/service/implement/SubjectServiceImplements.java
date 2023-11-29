package org.binaracademy.finalproject.service.implement;

import lombok.extern.slf4j.Slf4j;
import org.binaracademy.finalproject.DTO.SubjectDTO;
import org.binaracademy.finalproject.model.Subject;
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
    SubjectRepository subjectRepository;

    private SubjectDTO toSubjectDTO(Subject subject) {
        return SubjectDTO.builder()
                .title(subject.getTitle())
                .code(subject.getCode())
                .description(subject.getDescription())
                .isPremium(subject.getIsPremium())
                .link(subject.getLinkVideo())
                .build();
    }

    @Override
    public void addSubject(Subject subject) {
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
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubjectDTO> getAllSubject() {
        log.info("Getting all of list subject!");
        return subjectRepository.findAll().stream()
                .map(this::toSubjectDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SubjectDTO updateSubject(Subject subject, String code) {
        log.info("Process updating subject");
         Subject subject1 = subjectRepository.findByCode(code)
                 .orElseThrow(() -> new RuntimeException("Subject not found"));

         subject1.setTitle(subject.getTitle());
         subject1.setCode(subject.getCode());
         subject1.setDescription(subject.getDescription());
         subject1.setLinkVideo(subject.getLinkVideo());
         subject1.setIsPremium(subject.getIsPremium());
         subjectRepository.save(subject1);
         log.info("Updating subject with code: " + code + " successfully!");

         return toSubjectDTO(subject1);
    }

    @Override
    public void deleteSubjectByCode(String code) {
        try {
            Subject subject = subjectRepository.findByCode(code).orElse(null);
            if (!Optional.ofNullable(subject).isPresent()){
                log.info("Subject is not available");
            }
            subjectRepository.deleteSubjectByCode(code);
            log.info("Deleted subject where subject code: " + code + " successfully!");
        } catch (Exception e) {
            log.error("Deleting subject failed, please try again!");
        }
    }
}
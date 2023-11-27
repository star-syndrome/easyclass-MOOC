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
                .map(subject -> SubjectDTO.builder()
                        .title(subject.getTitle())
                        .description(subject.getDescription())
                        .isPremium(subject.getIsPremium())
                        .link(subject.getLinkVideo())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public SubjectDTO updateSubject(Subject subject, String title) {
        log.info("Process updating subject");
         Subject subject1 = subjectRepository.findByTitle(title)
                 .orElseThrow(() -> new RuntimeException("Subject not found"));

         subject1.setTitle(subject.getTitle());
         subject1.setDescription(subject.getDescription());
         subject1.setLinkVideo(subject.getLinkVideo());
         subject1.setIsPremium(subject.getIsPremium());
         subjectRepository.save(subject1);
         log.info("Updating subject with title: " + title + " successfully!");

         return SubjectDTO.builder()
                 .title(subject1.getTitle())
                 .description(subject1.getDescription())
                 .link(subject1.getLinkVideo())
                 .isPremium(subject1.getIsPremium())
                 .build();
    }

    @Override
    public void deleteSubjectByTitle(String title) {
        try {
            Subject subject = subjectRepository.findByTitle(title).orElse(null);
            if (!Optional.ofNullable(subject).isPresent()){
                log.info("Subject is not available");
            }
            subjectRepository.deleteSubjectByTitle(title);
            log.info("Deleted subject successfully!");
        } catch (Exception e) {
            log.error("Deleting subject failed, please try again!");
        }
    }
}
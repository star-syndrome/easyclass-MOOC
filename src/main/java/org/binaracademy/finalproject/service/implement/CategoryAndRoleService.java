package org.binaracademy.finalproject.service.implement;

import lombok.extern.slf4j.Slf4j;
import org.binaracademy.finalproject.model.Category;
import org.binaracademy.finalproject.model.Roles;
import org.binaracademy.finalproject.repository.CategoryRepository;
import org.binaracademy.finalproject.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class CategoryAndRoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<Category> getCategory() {
        log.info("Success getting all category!");
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Roles> getRoles() {
        log.info("Success getting all roles!");
        return roleRepository.findAll();
    }
}
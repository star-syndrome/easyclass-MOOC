package org.binaracademy.finalproject.configuration;

import lombok.extern.slf4j.Slf4j;
import org.binaracademy.finalproject.enumeration.CategoryRole;
import org.binaracademy.finalproject.model.Category;
import org.binaracademy.finalproject.repository.CategoryRepository;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class CategoryConfig {
    CategoryConfig(CategoryRepository categoryRepository){
        log.info("Checking category roles presented");
        for (CategoryRole cr : CategoryRole.values()) {
            try {
                Category category = categoryRepository.findByCategoryName(cr)
                        .orElseThrow(() -> new RuntimeException("Category roles not found"));
                log.info("Category role {} has been found!", category.getCategoryName());
            } catch (RuntimeException rte) {
                log.info("Category role {} is not found, inserting to DB . . .", cr.name());
                Category category = new Category();
                category.setCategoryName(cr);
                categoryRepository.save(category);
            }
        }
    }
}
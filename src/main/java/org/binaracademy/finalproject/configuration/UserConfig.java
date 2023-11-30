package org.binaracademy.finalproject.configuration;

import lombok.extern.slf4j.Slf4j;
import org.binaracademy.finalproject.model.Roles;
import org.binaracademy.finalproject.repository.RoleRepository;
import org.binaracademy.finalproject.security.enumeration.ERole;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class UserConfig {
    UserConfig(RoleRepository roleRepository) {
        log.info("Checking roles presented");
        for(ERole c : ERole.values()) {
            try {
                Roles roles = roleRepository.findByRoleName(c)
                        .orElseThrow(() -> new RuntimeException("Roles not found"));
                log.info("Role {} has been found!", roles.getRoleName());
            } catch(RuntimeException rte) {
                log.info("Role {} is not found, inserting to DB . . .", c.name());
                Roles roles = new Roles();
                roles.setRoleName(c);
                roleRepository.save(roles);
            }
        }
    }
}
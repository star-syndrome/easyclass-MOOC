package org.binaracademy.finalproject.configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.binaracademy.finalproject.service.CloudinaryService;
import org.binaracademy.finalproject.service.implement.CloudinaryServiceImplements;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "easyclass",
                "api_key", "294599962564915",
                "api_secret", "FBQ4m8cQOjkx1g9Cb18lu9xRMKw"));
    }

    @Bean
    public CloudinaryService cloudinaryService() {
        return new CloudinaryServiceImplements(cloudinary());
    }
}
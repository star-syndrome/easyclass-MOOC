package org.binaracademy.finalproject.service.implement;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.binaracademy.finalproject.security.response.MessageResponse;
import org.binaracademy.finalproject.service.CloudinaryService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CloudinaryServiceImplements implements CloudinaryService {

    private final Cloudinary cloudinary;

    @Override
    public MessageResponse upload(MultipartFile multipartFile) throws IOException {
        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(multipartFile.getBytes(),
                    ObjectUtils.emptyMap());
            String imageURL = uploadResult.get("url").toString();
            return MessageResponse.builder()
                    .message(imageURL)
                    .build();
        } catch (IOException e) {
            log.error("Error uploading file");
            throw e;
        }
    }
}
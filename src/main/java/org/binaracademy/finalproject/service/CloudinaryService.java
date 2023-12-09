package org.binaracademy.finalproject.service;

import org.binaracademy.finalproject.security.response.MessageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {

    MessageResponse upload(MultipartFile multipartFile) throws IOException;
}
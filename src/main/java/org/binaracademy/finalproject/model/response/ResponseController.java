package org.binaracademy.finalproject.model.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class ResponseController {

    public static ResponseEntity<Object> statusResponse(HttpStatus code, String message, Object data) {
        Map<String, Object> bodyResponse = new HashMap<>();
        if (data != null) {
            bodyResponse.put("data", data);
        }
        bodyResponse.put("status", code.value());
        bodyResponse.put("message", message);
        return ResponseEntity.status(code).body(bodyResponse);
    }

    public static ResponseEntity<Object> statusResponsePaging(HttpStatus code, String message, Object data, PagingResponse response) {
        Map<String, Object> bodyResponse = new HashMap<>();
        if (data != null) {
            bodyResponse.put("data", data);
        }
        bodyResponse.put("status", code.value());
        bodyResponse.put("message", message);
        bodyResponse.put("paging", response);
        return ResponseEntity.status(code).body(bodyResponse);
    }

    public static ResponseEntity<Object> internalServerError(String message) {
        return statusResponse(HttpStatus.INTERNAL_SERVER_ERROR, message, null);
    }
}
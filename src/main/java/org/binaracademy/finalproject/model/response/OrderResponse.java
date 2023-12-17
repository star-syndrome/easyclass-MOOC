package org.binaracademy.finalproject.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date time;

    private String paymentMethod;
    private String courseId;
    private Boolean completed;
}
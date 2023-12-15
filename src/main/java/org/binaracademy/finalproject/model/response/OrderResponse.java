package org.binaracademy.finalproject.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private String title;
    private String category;
    private String teacher;
    private Double price;
    private Double ppn;
    private Double totalPrice;
}
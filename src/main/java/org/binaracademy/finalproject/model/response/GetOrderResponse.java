package org.binaracademy.finalproject.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.binaracademy.finalproject.model.Category;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetOrderResponse {

    private String title;
    private Set<Category> category;
    private String teacher;
    private Double price;
    private Double ppn;
    private Double totalPrice;
}
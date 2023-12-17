package org.binaracademy.finalproject.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.binaracademy.finalproject.model.response.OrderResponse;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Long id;
    private List<OrderResponse> orderResponses;
}
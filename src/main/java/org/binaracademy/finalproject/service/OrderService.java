package org.binaracademy.finalproject.service;

import org.binaracademy.finalproject.model.request.CreateOrderRequest;
import org.binaracademy.finalproject.model.response.OrderResponse;

public interface OrderService {

    OrderResponse getDataOrder(String code);

    void createOrder(CreateOrderRequest createOrderRequest);
}
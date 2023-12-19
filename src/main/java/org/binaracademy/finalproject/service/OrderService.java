package org.binaracademy.finalproject.service;

import org.binaracademy.finalproject.DTO.OrderDTO;
import org.binaracademy.finalproject.model.request.CreateOrderRequest;
import org.binaracademy.finalproject.model.response.GetOrderResponse;
import org.binaracademy.finalproject.security.response.MessageResponse;

import java.util.List;

public interface OrderService {

    GetOrderResponse getDataOrder(String title);

    MessageResponse createOrder(CreateOrderRequest createOrderRequest);

    OrderDTO getOrderTransactions();

    List<OrderDTO> getAllOrder();
}
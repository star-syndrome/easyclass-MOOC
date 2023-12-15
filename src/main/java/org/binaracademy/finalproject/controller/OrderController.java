package org.binaracademy.finalproject.controller;

import org.binaracademy.finalproject.model.request.CreateOrderRequest;
import org.binaracademy.finalproject.model.response.ResponseController;
import org.binaracademy.finalproject.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping(value = "/get")
    public ResponseEntity<Object> getOrder(String code) {
        try {
            return ResponseController.statusResponse(HttpStatus.OK,
                    "Success",
                    orderService.getDataOrder(code));
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }

    @PostMapping(value = "/create")
    public ResponseEntity<Object> createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        try {
            orderService.createOrder(createOrderRequest);
            return ResponseController.statusResponse(HttpStatus.OK, "Success", null);
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }
}

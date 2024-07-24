package org.binaracademy.finalproject.controller;

import org.binaracademy.finalproject.model.request.CreateOrderRequest;
import org.binaracademy.finalproject.model.response.ResponseController;
import org.binaracademy.finalproject.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping(
            path = "/{code}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> getOrder(@PathVariable String code) {
        return ResponseController.statusResponse(HttpStatus.OK,
                "Success getting data course for order!", orderService.getDataOrder(code));

    }

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> createOrder(@Validated @RequestBody CreateOrderRequest createOrderRequest) {
            return ResponseController.statusResponse(HttpStatus.OK,
                    "Success create an order!", orderService.createOrder(createOrderRequest));
    }

    @GetMapping(
            path = "/history",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> getOrderTransactions() {
        return ResponseController.statusResponse(HttpStatus.OK,
                "Success getting order transactions!", orderService.getOrderTransactions());
    }
}
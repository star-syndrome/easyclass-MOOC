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

    @GetMapping(value = "/getDataOrder")
    public ResponseEntity<Object> getOrder(@RequestParam String title) {
        try {
            return ResponseController.statusResponse(HttpStatus.OK,
                    "Success", orderService.getDataOrder(title));
        } catch (RuntimeException rte) {
            return ResponseController.statusResponse(HttpStatus.NOT_FOUND,
                    rte.getMessage(), null);
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }

    @PostMapping(value = "/create")
    public ResponseEntity<Object> createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        try {
            return ResponseEntity.ok().body(orderService.createOrder(createOrderRequest));
        } catch (RuntimeException rte) {
            return ResponseController.statusResponse(HttpStatus.NOT_FOUND,
                    rte.getMessage(), null);
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }

    @GetMapping(value = "/getOrderTransactions")
    public ResponseEntity<Object> getOrderTransactions() {
        try {
            return ResponseController.statusResponse(HttpStatus.OK,
                    "Success getting order transaction", orderService.getOrderTransactions());
        } catch (RuntimeException rte) {
            return ResponseController.statusResponse(HttpStatus.NOT_FOUND,
                    rte.getMessage(), null);
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }
}
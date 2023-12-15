package org.binaracademy.finalproject.service.implement;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.binaracademy.finalproject.model.Order;
import org.binaracademy.finalproject.model.Users;
import org.binaracademy.finalproject.model.request.CreateOrderRequest;
import org.binaracademy.finalproject.model.response.OrderResponse;
import org.binaracademy.finalproject.repository.CourseRepository;
import org.binaracademy.finalproject.repository.OrderRepository;
import org.binaracademy.finalproject.repository.UserRepository;
import org.binaracademy.finalproject.service.OrderService;
import org.binaracademy.finalproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Transactional
@Slf4j
@Service
public class OrderServiceImplements implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getDataOrder(String code) {
        log.info("Success getting data order");
        return courseRepository.findByCodeCourse(code)
                .map(course -> OrderResponse.builder()
                        .title(course.getTitleCourse())
                        .teacher(course.getTeacher())
                        .category(course.getCategories().toString())
                        .price(course.getPriceCourse())
                        .ppn(course.getPriceCourse() * 0.11)
                        .totalPrice(course.getPriceCourse() + (course.getPriceCourse() * 0.11))
                        .build())
                .orElse(null);
    }

    @Override
    @Transactional
    public void createOrder(CreateOrderRequest createOrderRequest) {
        try {
            log.info("Processing create order from course code: {}", createOrderRequest.getCourseCode());
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Order order = new Order();
            order.setPaymentMethod(createOrderRequest.getPaymentMethod());
            order.setOrderTime(new Date());
            order.setPaid(Boolean.FALSE);
            order.setUsers(userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found")));
            order.setCourse(courseRepository.findByCodeCourse(createOrderRequest.getCourseCode()).orElseThrow(() -> new RuntimeException("Course not found")));

            orderRepository.save(order);
            log.info("Create order successfully!");
        } catch (Exception e) {
            log.error("Create order where course code " + createOrderRequest.getCourseCode() + " failed");
            throw e;
        }
    }
}
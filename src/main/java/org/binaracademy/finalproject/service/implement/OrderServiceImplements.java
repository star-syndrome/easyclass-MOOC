package org.binaracademy.finalproject.service.implement;

import lombok.extern.slf4j.Slf4j;
import org.binaracademy.finalproject.DTO.OrderDTO;
import org.binaracademy.finalproject.model.Course;
import org.binaracademy.finalproject.model.Order;
import org.binaracademy.finalproject.model.Users;
import org.binaracademy.finalproject.model.request.CreateOrderRequest;
import org.binaracademy.finalproject.model.request.EmailRequest;
import org.binaracademy.finalproject.model.response.OrderResponse;
import org.binaracademy.finalproject.model.response.GetOrderResponse;
import org.binaracademy.finalproject.model.response.OrderResponseForGetOrderTransaction;
import org.binaracademy.finalproject.repository.CourseRepository;
import org.binaracademy.finalproject.repository.OrderRepository;
import org.binaracademy.finalproject.repository.UserRepository;
import org.binaracademy.finalproject.security.response.MessageResponse;
import org.binaracademy.finalproject.service.EmailService;
import org.binaracademy.finalproject.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Autowired
    private EmailService emailService;

    @Override
    @Transactional(readOnly = true)
    public GetOrderResponse getDataOrder(String code) {
        try {
            log.info("Success getting data order");
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));

            return courseRepository.findByCode(code)
                    .map(course -> GetOrderResponse.builder()
                            .title(course.getTitle())
                            .teacher(course.getTeacher())
                            .category(course.getCategories())
                            .price(course.getPrice())
                            .ppn(course.getPrice() * 0.11)
                            .totalPrice(course.getPrice() + (course.getPrice() * 0.11))
                            .build())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found!"));
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public MessageResponse createOrder(CreateOrderRequest createOrderRequest) {
        try {
            log.info("Processing create order from course code: {}", createOrderRequest.getCourseCode());
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Users users = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
            Course courses = courseRepository.findByCode(createOrderRequest.getCourseCode())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

            Boolean orderValidation = orderRepository.orderValidation(users.getId(), courses.getId());
            if (Boolean.TRUE.equals(orderValidation)) {
                return MessageResponse.builder()
                        .message("User already ordered this course!")
                        .build();
            }

            Order order = new Order();
            order.setPaymentMethod(createOrderRequest.getPaymentMethod());
            order.setOrderTime(new Date());
            order.setPaid(Boolean.TRUE);
            order.setUsers(users);
            order.setCourse(courses);
            orderRepository.save(order);

            emailService.sendEmail(EmailRequest.builder()
                            .recipient(users.getEmail())
                            .subject("E-receipt Easy Class")
                            .content("This is your receipt!" + "\nName: " + order.getUsers().getFullName() + "\nCourse: "
                                    + order.getCourse().getTitle() + "\nPayment: " + order.getPaymentMethod() + "\nPaid: "
                                    + order.getPaid() + "\nOrder Time: " + order.getOrderTime() + "\nOrder Id: " + order.getId()
                                    + "\nThank you!")
                    .build());

            return MessageResponse.builder()
                    .message("Create order successfully!")
                    .build();
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderTransactions() {
        try {
            log.info("Success getting transaction history!");
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Users users = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));

            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setId(users.getId());
            orderDTO.setOrderResponses(users.getOrders().stream()
                    .map(order -> OrderResponseForGetOrderTransaction.builder()
                            .time(order.getOrderTime())
                            .paymentMethod(order.getPaymentMethod())
                            .completed(order.getPaid())
                            .courseId(order.getCourse().getId())
                            .build())
                    .collect(Collectors.toList()));
            return orderDTO;
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrder() {
        log.info("Success getting all order from all user!");
        return orderRepository.findAll().stream()
                .map(this::getOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByEmail(String email) {
        try {
            orderRepository.deleteByUsers(userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!")));
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteByCode(String code) {
        try {
            orderRepository.deleteByCourse(courseRepository.findByCode(code)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found!")));
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            throw e;
        }
    }

    private OrderResponse getOrderResponse(Order order) {
        return OrderResponse.builder()
                .userId(order.getUsers().getId())
                .paymentMethod(order.getPaymentMethod())
                .time(order.getOrderTime())
                .completed(order.getPaid())
                .courseId(order.getCourse().getId())
                .build();
    }
}
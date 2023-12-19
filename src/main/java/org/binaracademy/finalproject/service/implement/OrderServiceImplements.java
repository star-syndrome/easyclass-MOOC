package org.binaracademy.finalproject.service.implement;

import lombok.extern.slf4j.Slf4j;
import org.binaracademy.finalproject.DTO.OrderDTO;
import org.binaracademy.finalproject.model.Course;
import org.binaracademy.finalproject.model.Order;
import org.binaracademy.finalproject.model.Users;
import org.binaracademy.finalproject.model.request.CreateOrderRequest;
import org.binaracademy.finalproject.model.response.GetOrderResponse;
import org.binaracademy.finalproject.model.response.OrderResponse;
import org.binaracademy.finalproject.repository.CourseRepository;
import org.binaracademy.finalproject.repository.OrderRepository;
import org.binaracademy.finalproject.repository.UserRepository;
import org.binaracademy.finalproject.security.response.MessageResponse;
import org.binaracademy.finalproject.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional(readOnly = true)
    public GetOrderResponse getDataOrder(String title) {
        log.info("Success getting data order");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found!"));
        return courseRepository.findByTitleCourse(title)
                .map(course -> GetOrderResponse.builder()
                        .title(course.getTitleCourse())
                        .teacher(course.getTeacher())
                        .category(course.getCategories())
                        .price(course.getPriceCourse())
                        .ppn(course.getPriceCourse() * 0.11)
                        .totalPrice(course.getPriceCourse() + (course.getPriceCourse() * 0.11))
                        .build())
                .orElseThrow(() -> new RuntimeException("Course not found!"));
    }

    @Override
    public MessageResponse createOrder(CreateOrderRequest createOrderRequest) {
        try {
            log.info("Processing create order from course: {}", createOrderRequest.getCourseTitle());
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<Users> users = userRepository.findByUsername(username);
            Optional<Course> course = courseRepository.findByTitleCourse(createOrderRequest.getCourseTitle());
            Users user = users.get();
            Course courses = course.get();

            Boolean existsByUser = orderRepository.existsByUsers(user);
            Boolean existsByCourse = orderRepository.existsByCourse(courses);
            if (Boolean.TRUE.equals(existsByUser) && Boolean.TRUE.equals(existsByCourse)) {
                return MessageResponse.builder()
                        .message("User already ordered this course!")
                        .build();
            }

            Order order = new Order();
            order.setPaymentMethod(createOrderRequest.getPaymentMethod());
            order.setOrderTime(new Date());
            order.setPaid(Boolean.TRUE);
            order.setUsers(userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found")));
            order.setCourse(courseRepository.findByTitleCourse(createOrderRequest.getCourseTitle()).orElseThrow(() -> new RuntimeException("Course not found")));

            orderRepository.save(order);
            log.info("Create order successfully");
            return MessageResponse.builder()
                    .message("Create order successfully!")
                    .build();
        } catch (Exception e) {
            log.error("Create order where course " + createOrderRequest.getCourseTitle() + " failed");
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderTransactions() {
        log.info("Success getting transaction history!");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Users> users = userRepository.findByUsername(username);
        Users user = users.get();

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(user.getId());
        orderDTO.setOrderResponses(user.getOrders().stream()
                .map(order -> OrderResponse.builder()
                        .time(order.getOrderTime())
                        .paymentMethod(order.getPaymentMethod())
                        .completed(order.getPaid())
                        .courseId(order.getCourse().getId())
                        .build())
                .collect(Collectors.toList()));
        return orderDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrder() {
        log.info("Success getting all order from all user!");
        return orderRepository.findAll().stream()
                .map(this::getOrderDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByUsername(String username) {
        orderRepository.deleteByUsers(userRepository.getUserByUsername(username).get());
    }

    private OrderDTO getOrderDTO(Order order) {
        return OrderDTO.builder()
                .id(order.getUsers().getId())
                .orderResponses(order.getCourse().getOrders().stream()
                        .map(orders -> {
                            OrderResponse orderResponse = new OrderResponse();
                            orderResponse.setCourseId(orders.getCourse().getId());
                            orderResponse.setTime(orders.getOrderTime());
                            orderResponse.setPaymentMethod(orders.getPaymentMethod());
                            orderResponse.setCompleted(orders.getPaid());
                            return orderResponse;
                        })
                        .collect(Collectors.toList()))
                .build();
    }
}
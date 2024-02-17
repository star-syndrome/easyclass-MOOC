package org.binaracademy.finalproject.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.binaracademy.finalproject.model.request.CreateCourseRequest;
import org.binaracademy.finalproject.model.request.CreateSubjectRequest;
import org.binaracademy.finalproject.model.request.UpdateCourseRequest;
import org.binaracademy.finalproject.model.request.UpdateSubjectRequest;
import org.binaracademy.finalproject.model.response.ResponseController;
import org.binaracademy.finalproject.service.CourseService;
import org.binaracademy.finalproject.service.OrderService;
import org.binaracademy.finalproject.service.SubjectService;
import org.binaracademy.finalproject.service.UserService;
import org.binaracademy.finalproject.service.implement.CategoryAndRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private CategoryAndRoleService categoryAndRoleService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @PostMapping(
            path = "/course/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> addCourse(@RequestBody CreateCourseRequest course){
        return ResponseController.statusResponse(HttpStatus.OK, "Adding new course successful!",
                courseService.addNewCourse(course));
    }

    @DeleteMapping(
            path = "/course/delete/{codeCourse}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> deleteCourse(@PathVariable String codeCourse){
        courseService.deleteCourseByCode(codeCourse);
        return ResponseController.statusResponse(HttpStatus.OK, "Deleting course successful!", null);
    }

    @PutMapping(
            path = "/course/update/{code}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> updateCourse(@PathVariable String code, @RequestBody UpdateCourseRequest request){
        courseService.updateCourse(request, code);
        return ResponseController.statusResponse(HttpStatus.OK,
                "Update course successful!", courseService.getCourse(code));
    }

    @GetMapping(
            path = "/course/getByCode",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> getCourse(@RequestParam String code) {
        return ResponseController.statusResponse(HttpStatus.OK,
                "Success get course " + code, courseService.getCourse(code));
    }

    @GetMapping(
            path = "/course/getAllCourse",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Get course yang ada ID-nya")
    public ResponseEntity<Object> getCourseAdmin() {
        try {
            return ResponseController.statusResponse(HttpStatus.OK,
                    "Success get all courses!", courseService.getAllCourseAdmin());
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }

    @PostMapping(
            path = "/subject/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> addSubject(@RequestBody CreateSubjectRequest request){
        return ResponseController.statusResponse(HttpStatus.OK, "Add new subject successful",
                subjectService.addSubject(request));
    }

    @PutMapping(
            path = "/subject/update/{code}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> updateSubject(@PathVariable String code, @RequestBody UpdateSubjectRequest request){
        subjectService.updateSubject(request, code);
        return ResponseController.statusResponse(HttpStatus.OK, "Update subject successful!",
                subjectService.getSubject(code));
    }

    @DeleteMapping(
            path = "/subject/delete/{code}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> deleteSubject(@PathVariable String code){
        subjectService.deleteSubjectByCode(code);
        return ResponseController.statusResponse(HttpStatus.OK, "Deleting subject successful!", null);
    }

    @GetMapping(
            path = "/subject/getAllSubject",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> getAllSubject(){
        return ResponseController.statusResponse(HttpStatus.OK,
                "Success get all subject", subjectService.getAllSubject());
    }

    @GetMapping(
            path = "/subject/getByCode",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> getSubjectByCode(@RequestParam String code){
        return ResponseController.statusResponse(HttpStatus.OK,
                "Success getting subject", subjectService.getSubject(code));
    }

    @GetMapping(
            path = "/category/get",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> getRoleCategory() {
        try {
            return ResponseController.statusResponse(HttpStatus.OK,
                    "Success get all category", categoryAndRoleService.getCategory());
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }

    @GetMapping(
            path = "/role/get",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> getRoles() {
        try {
            return ResponseController.statusResponse(HttpStatus.OK,
                    "Success get all roles!", categoryAndRoleService.getRoles());
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }

    @GetMapping(
            path = "/user/getAllUser",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> getAllCourse(){
        try {
            return ResponseController.statusResponse(HttpStatus.OK,
                    "Success get all users!", userService.getAllUser());
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }

    @DeleteMapping(
            path = "/user/deleteUserForAdmin/{email}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> deleteUserForAdmin(@PathVariable String email) {
        userService.deleteUserForAdmin(email);
        return ResponseController.statusResponse(HttpStatus.OK, "Deleting user successful!", null);
    }

    @GetMapping(
            path = "/order/getAllOrderTransactions",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> getAllOrder() {
        try {
            return ResponseController.statusResponse(HttpStatus.OK,
                    "Success getting all order transactions", orderService.getAllOrder());
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }
}
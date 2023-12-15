package org.binaracademy.finalproject.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "course", uniqueConstraints = @UniqueConstraint(columnNames = "codeCourse"))
public class Course {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Size(max = 1000)
    private String aboutCourse;

    private String titleCourse;

    private String codeCourse;

    private Double priceCourse;

    private String levelCourse;

    private String teacher;

    private Boolean isPremium;

    private String module;

    private String duration;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "course_category",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private List<Subject> subjects;

    @OneToMany(mappedBy = "course")
    private List<Order> orders;
}
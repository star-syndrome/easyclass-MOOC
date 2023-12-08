package org.binaracademy.finalproject.model;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
public class OneTimePassword {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users users;

    @Column(nullable = false, unique = true)
    private String otp;

    @Column(nullable = false)
    private Instant expiryDate;
}
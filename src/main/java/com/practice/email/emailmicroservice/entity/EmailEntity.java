package com.practice.email.emailmicroservice.entity;

import com.practice.email.emailmicroservice.dto.StatusEmail;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "EMAILS")
public class EmailEntity implements Serializable {

    private static final long serialVersionUID = -8548276808111944183L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID emailId;

    private String ownerRef;
    private String emailFrom;
    private String emailTo;
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String text;
    private LocalDateTime sendDateEmail;
    private StatusEmail statusEmail;
}

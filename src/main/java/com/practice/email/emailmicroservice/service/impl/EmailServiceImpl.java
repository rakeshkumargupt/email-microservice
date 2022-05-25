package com.practice.email.emailmicroservice.service.impl;

import com.practice.email.emailmicroservice.dto.StatusEmail;
import com.practice.email.emailmicroservice.entity.EmailEntity;
import com.practice.email.emailmicroservice.repository.EmailRepository;
import com.practice.email.emailmicroservice.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final EmailRepository emailRepository;
    private final JavaMailSender emailSender;

    @Override
    public EmailEntity sendEmail(EmailEntity emailEntity) {
        emailEntity.setSendDateEmail(LocalDateTime.now());
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailEntity.getEmailFrom());
            message.setTo(emailEntity.getEmailTo());
            message.setSubject(emailEntity.getSubject());
            message.setText(emailEntity.getText());
            emailSender.send(message);

            emailEntity.setStatusEmail(StatusEmail.SENT);
        } catch (MailException e) {
            emailEntity.setStatusEmail(StatusEmail.ERROR);
        }

        return emailRepository.save(emailEntity);
    }

    public Page<EmailEntity> findAll(Pageable pageable) {
        return emailRepository.findAll(pageable);
    }

    public Optional<EmailEntity> findById(UUID emailId) {
        return emailRepository.findById(emailId);
    }
}

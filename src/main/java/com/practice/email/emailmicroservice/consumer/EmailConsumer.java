package com.practice.email.emailmicroservice.consumer;


import com.practice.email.emailmicroservice.dto.EmailDto;
import com.practice.email.emailmicroservice.entity.EmailEntity;
import com.practice.email.emailmicroservice.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EmailConsumer {

    private final EmailService emailService;

    @RabbitListener(queues = "${spring.rabbitmq.email.queue}")
    public void listen(@Payload EmailDto emailDto) {
        EmailEntity emailEntity = new EmailEntity();
        BeanUtils.copyProperties(emailDto, emailEntity);
        emailService.sendEmail(emailEntity);
        System.out.println("Email Status: " + emailEntity.getStatusEmail().toString());
    }
}

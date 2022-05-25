# Email Microservice Consuming Queue(from rabbitMQ) to Send Email Asynchronously & PostgreSQL to Track Email Status


[![Build Status](https://travis-ci.org/codecentric/springboot-sample-app.svg?branch=master)](https://travis-ci.org/codecentric/springboot-sample-app)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

## About the Service

This service uses [Spring Boot 2.7](), [JPA](), [Hibernate](), [Java Mail API]() & [PostgreSQL 42.2]().
It offers below functionality:

* Sending emails using gmail.
* Rest API to send mail.
* It consumes queue from RabbitMQ to send mail asynchronously. 
* It uses cloudamqp SaaS for messaging purpose (for free access or more details visit [CloudAMQP](https://www.cloudamqp.com/))
* It uses postgres db to store emails tracking logs.

## How to Run

This application is packaged as a war which has Tomcat 8 embedded. No Tomcat or JBoss installation is necessary. You run it using the ```java -jar``` command.

* Clone this repository
* Make sure you are using JDK 1.8 and Maven 3.x
* You can build the project and run the tests by running ```mvn clean package```
* Once successfully built, you can run the service by one of these two methods:
```
        java -jar target/email-microservice-1.0.0-SNAPSHOT.jar
or
        mvn spring-boot:run
```

Once the application runs you should see something like this

```
2022-05-25 12:12:56.102  INFO 404 --- [  restartedMain] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path '/email-service'
2022-05-25 12:12:56.102  INFO 404 --- [  restartedMain] o.s.a.r.c.CachingConnectionFactory       : Attempting to connect to: [rattlesnake.rmq.cloudamqp.com:5671]
2022-05-25 12:12:57.920  INFO 404 --- [  restartedMain] o.s.a.r.c.CachingConnectionFactory       : Created new connection: rabbitConnectionFactory#720e04e0:0/SimpleConnection@1c7059ab [delegate=amqp://zacibnmp@18.135.60.254:5671/zacibnmp, localPort= 52111]
2022-05-25 12:12:59.159  INFO 404 --- [  restartedMain] c.p.e.e.EmailMicroserviceApplication     : Started EmailMicroserviceApplication in 8.154 seconds (JVM running for 11.469)
```

## Email Service API's

1. ### Send email using API

```
POST /email-service/emails
Accept: application/json
Content-Type: application/json

{
    "ownerRef": "Rakesh Gupta",
    "emailFrom": "senderEmail@gmail.com",
    "emailTo": "receiverEmail@gmail.com",
    "subject": "Email micro service with rabbit mq cloud & postgreSQL logs",
    "text": "Email micro service with rabbit mq cloud & postgreSQL logs"
}

RESPONSE: HTTP 201 (Created)

{
    "emailId": "a08c0cbd-7276-4b24-9089-98f2604a5a4f",
    "ownerRef": "Rakesh Gupta",
    "emailFrom": "senderEmail@gmail.com",
    "emailTo": "receiverEmail@gmail.com",
    "subject": "Email micro service with rabbit mq cloud & postgreSQL logs",
    "text": "Email micro service with rabbit mq cloud & postgreSQL logs",
    "sendDateEmail": "2022-05-25T11:07:00.256",
    "statusEmail": "SENT"
}
```

2. ### Send email by publishing emailDTO to `email-queue` on RabbitMQ

```
message = <email dto object>;
channel.basicPublish("", "email-queue", null, message.getBytes());

Once publish, below listener will trigger and send mail asynchronously     

@RabbitListener(queues = "${spring.rabbitmq.email.queue:email-queue}")
public void listen(@Payload EmailDto emailDto) {
      EmailEntity emailEntity = new EmailEntity();
      BeanUtils.copyProperties(emailDto, emailEntity);
      emailService.sendEmail(emailEntity);
      System.out.println("Email Status: " + emailEntity.getStatusEmail().toString());
}

```

3. ### Retrieve all email logs

```
GET /email-service/emails

Response: HTTP 200

{
    "content": [
        {
            "emailId": "a08c0cbd-7276-4b24-9089-98f2604a5a4f",
            "ownerRef": "Rakesh Gupta",
            "emailFrom": "senderEmail@gmail.com",
            "emailTo": "receiverEmail@gmail.com",
            "subject": "Email micro service with rabbit mq cloud & postgreSQL logs",
            "text": "Email micro service with rabbit mq cloud & postgreSQL logs",
            "sendDateEmail": "2022-05-25T11:07:00.256",
            "statusEmail": "SENT"
        }
    ],
    "pageable": {
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "offset": 0,
        "pageSize": 5,
        "pageNumber": 0,
        "unpaged": false,
        "paged": true
    },
    "last": true,
    "totalElements": 5,
    "totalPages": 1,
    "size": 5,
    "number": 0,
    "sort": {
        "empty": false,
        "sorted": true,
        "unsorted": false
    },
    "numberOfElements": 5,
    "first": true,
    "empty": false
}
```

4. ### Retrieve email status by email id

```
GET /email-service/emails/a08c0cbd-7276-4b24-9089-98f2604a5a4f

RESPONSE: HTTP 200

{
    "emailId": "a08c0cbd-7276-4b24-9089-98f2604a5a4f",
    "ownerRef": "Rakesh Gupta",
    "emailFrom": "senderEmail@gmail.com",
    "emailTo": "receiverEmail@gmail.com",
    "subject": "Email micro service with rabbit mq cloud & postgreSQL logs",
    "text": "Email micro service with rabbit mq cloud & postgreSQL logs",
    "sendDateEmail": "2022-05-25T11:07:00.256",
    "statusEmail": "SENT"
}
```

## To view Swagger 2 API docs

Run the server and browse to `localhost:8080/email-service/swagger-ui.html`

## Questions and Comments : rakeshgupta.contact@gmail.com

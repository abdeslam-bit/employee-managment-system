package com.myFullstack.employee_management.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Value("${app.base-url}")
    private String baseUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Sends a signup invitation email to a newly created employee.
     *
     * @param toEmail       the employee's email address
     * @param employeeName  the employee's first name (used in the greeting)
     * @param inviteToken   the unique token that identifies this employee's signup link
     */
    public void sendInvitationEmail(String toEmail, String employeeName, String inviteToken) {
        String signupLink = baseUrl + "/auth/signup?token=" + inviteToken;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(toEmail);
        message.setSubject("Welcome to Employee Management — Complete Your Registration");
        message.setText(
                "Hello " + employeeName + ",\n\n" +
                "You have been added to the Employee Management system.\n\n" +
                "Please use the link below to create your account. " +
                "You will need to provide a username and password.\n\n" +
                "Signup link:\n" + signupLink + "\n\n" +
                "This link is valid for a single use and will be invalidated after you register.\n\n" +
                "If you did not expect this email, please contact your administrator.\n\n" +
                "Best regards,\n" +
                "Employee Management System"
        );

        mailSender.send(message);
    }
}

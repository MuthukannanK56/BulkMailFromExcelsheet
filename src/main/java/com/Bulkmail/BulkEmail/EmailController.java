package com.Bulkmail.BulkEmail;

import com.Bulkmail.BulkEmail.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/email")
public class EmailController {


    private EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public String sendEmails(@RequestParam("file") MultipartFile file) {
        try {
            emailService.sendEmails(file);
            return "Emails sent successfully!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error sending emails: " + e.getMessage();
        }
    }
}

package com.Bulkmail.BulkEmail;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmailService {


    private JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmails(MultipartFile file) throws IOException {
        List<CustomerEmail> emails = readExcelFile(file);
        List<List<CustomerEmail>> batches = splitEmails(emails, 5);

        for (int i = 0; i < batches.size(); i++) {
            sendBatchEmails(batches.get(i), i % 2 == 0 ? "firstsender@example.com" : "secondsender@example.com");
        }
    }

    private List<CustomerEmail> readExcelFile(MultipartFile file) throws IOException {
        List<CustomerEmail> emails = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            String email = row.getCell(0).getStringCellValue();
            emails.add(new CustomerEmail(email));
        }

        workbook.close();
        return emails;
    }

    private List<List<CustomerEmail>> splitEmails(List<CustomerEmail> emails, int batchSize) {
        List<List<CustomerEmail>> batches = new ArrayList<>();
        for (int i = 0; i < emails.size(); i += batchSize) {
            batches.add(emails.subList(i, Math.min(i + batchSize, emails.size())));
        }

        return batches;
    }

    private void sendBatchEmails(List<CustomerEmail> batch, String from) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);


        for (CustomerEmail customerEmail : batch) {
            System.out.println(customerEmail.getEmail());
            message.setTo(customerEmail.getEmail());
            message.setSubject("Subject");
            message.setText("Body");
            mailSender.send(message);
        }
    }
}

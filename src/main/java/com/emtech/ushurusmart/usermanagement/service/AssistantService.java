package com.emtech.ushurusmart.usermanagement.service;

import com.emtech.ushurusmart.usermanagement.model.Assistant;
import com.emtech.ushurusmart.usermanagement.repository.AssistantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AssistantService {
    @Autowired
    private AssistantRepository assistantRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public Assistant findByEmail(String email) {
        return assistantRepository.findByEmail(email);
    }

    public Assistant save(Assistant assistant) {
        assistant.setPassword(passwordEncoder.encode(assistant.getPassword()));
        return assistantRepository.save(assistant);
    }

    public String generateRandomPassword(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            password.append(characters.charAt(randomIndex));
        }

        return password.toString();
    }

    public String createEmailBody(String name, String email, String password ) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Welcome to Ushuru Smart</title>\n" +
                "    <style>\n" +
                "        body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }\n" +
                "        .email-container { width: 80%; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 10px; }\n" +
                "        .email-header { text-align: center; padding: 20px 0; background-color: #f32525; color: #ffffff; border-radius: 10px 10px 0 0; }\n" +
                "        .email-body { padding: 20px; }\n" +
                "        .email-footer { text-align: center; padding: 20px; background-color: #f32525; color: #ffffff; border-radius: 0 0 10px 10px; }\n" +
                "        .email-footer a { color: #ffffff; text-decoration: none; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"email-container\">\n" +
                "        <div class=\"email-header\">\n" +
                "            <h1>Welcome to Ushuru Smart</h1>\n" +
                "        </div>\n" +
                "        <div class=\"email-body\">\n" +
                "            <p>Dear "+name+",</p>\n" +
                "            <p>We are excited to have you on Ushuru Smart! To get started, please use the following  email and  password to log in to your account:</p>\n" +
                "            <p><span> Email: <span> : <span> <strong>" + email + "</strong></span></p>\n" +
                "            <p><span> Password: <span> : <span> <strong>" + password + "</strong></span></p>\n" +
                "            <p>Please ensure to keep your password confidential and do not share it with anyone.</p>\n" +
                "            <p>If you have any questions or need further assistance, please do not hesitate to contact our support team.</p>\n" +
                "            <p>Thank you for choosing our service!</p>\n" +
                "        </div>\n" +
                "        <div class=\"email-footer\">\n" +
                "            <p>&copy; "+ LocalDate.now().getYear() +" Ushuru Smart. All rights reserved.</p>\n" +
                "            <p><a href=\"https://www.yourcompany.com/support\">Support</a></p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }

    public Assistant findById(long assistantId) {
        Optional<Assistant> assistant = assistantRepository.findById(assistantId);
        return assistant.orElse(null);
    }
    public List<Assistant> findAll(){
        return assistantRepository.findAll();
    }

    public void deleteById(long assistantId) {
        assistantRepository.deleteById(assistantId);
    }

    public Assistant findByPhoneNumber(String phoneNumber) {
        return assistantRepository.findByPhoneNumber(phoneNumber);
    }
}
    
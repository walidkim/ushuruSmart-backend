package com.emtech.ushurusmart.utils.otp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.emtech.ushurusmart.config.LoggerSingleton;
import com.emtech.ushurusmart.utils.EmailService;

@Service
public class OTPService extends LoggerSingleton {
    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private EmailService emailService;

    private static final Logger log = LoggerFactory.getLogger(OTPService.class);

    private void saveOtp(String usertag, String otpcode) {
        OtpEntity otpEntity = new OtpEntity();
        otpEntity.setUserTag(usertag);
        otpEntity.setOtpCode(otpcode);
        otpEntity.setValidUntil(LocalDateTime.now().plusMinutes(10));
        otpRepository.save(otpEntity);
    }

    private String generateOTP() {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            otp.append(ThreadLocalRandom.current().nextInt(0, 10));
        }
        return otp.toString();
    }

    @SuppressWarnings("null")

    public boolean sendOTP(String phoneNo, String Name, String Email) throws Exception {
        if (phoneNo.isEmpty()) {
            throw new Exception("User tag or Phone number is missing");
        } else {

            if (otpRepository.findByUserTag(phoneNo) != null) {
                otpRepository.deleteByUserTag(phoneNo);
            }
            String otpCode = generateOTP();
            String messageBody = "Your UshuruSmart OTP code is: " + otpCode + ".\n Do not share this with anyone.";

            try {
                // Twilio.init(otpconfig.ACCOUNT_SID, otpconfig.AUTH_TOKEN);
                // Message message = Message.creator(
                // new PhoneNumber("+" + phoneNo),
                // new PhoneNumber(otpconfig.TWILIO_PHONE_NUMBER),
                // messageBody)
                // .create();
                // System.out.println(message);
                logger.info(otpCode + " " + phoneNo);

                String body = createEmailBody(Name, otpCode);
                emailService.sendEmail(Email, "Welcome To Ushuru Smart", body);
                logger.info("OTP code sent for" + Name);

                saveOtp(phoneNo, otpCode);
                return true;

            } catch (Exception e) {
                logger.error("Failed to send OTP via email: " + e);
                return false;
            }
        }

    }

    @SuppressWarnings("null")
    public boolean verifyOTP(String phoneNumber, String otpcode) {
        Optional<OtpEntity> otpEntity = otpRepository.findByUserTagAndOtpCode(phoneNumber, otpcode);
        if (otpEntity.isPresent()) {
            try {
                otpRepository.deleteByUserTag(phoneNumber);
                return true;
            } catch (Exception e) {
                logger.error("Error while deleting" + phoneNumber + " : " + e);
                return false;
            }

        } else {
            return false;
        }
    }

    // Delete expired OTPs, Run every minute
    @Scheduled(fixedDelay = 60000)
    private void cleanUpExpiredOTPs() {
        otpRepository.deleteByValidUntilOrBeforeNow();
    }

    public String createEmailBody(String name, String otpcode) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Welcome to Ushuru Smart</title>\n" +
                "    <style>\n" +
                "        body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }\n" +
                "        .email-container { width: 90%; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 10px; }\n"
                +
                "        .email-header { text-align: center; padding: 20px 0; background-color: #f32525; color: #ffffff; border-radius: 10px 10px 0 0; }\n"
                +
                "        .email-body { padding: 20px; }\n" +
                "        .email-footer { text-align: center; padding: 20px; background-color: #f32525; color: #ffffff; border-radius: 0 0 10px 10px; }\n"
                +
                "        .email-footer a { color: #ffffff; text-decoration: none; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"email-container\">\n" +
                "        <div class=\"email-header\">\n" +
                "            <h1>Ushuru Smart- OTP</h1>\n" +
                "        </div>\n" +
                "        <div class=\"email-body\">\n" +
                "            <p>Dear " + name + ",</p>\n" +
                "            <p> Use this code to complete your login to Ushuru Smart.</p>" +
                "            <p><span><strong> OTP: <span><span> " + otpcode + "</strong></span></p>\n" +
                "            <p>This code expires in 10 minutes. Never share it with anyone!</p>\n" +
                "            <p>If you have any questions or need further assistance, please do not hesitate to contact our support team.</p>\n"
                +
                "            <p>Thank you for choosing our service!</p>\n" +
                "        </div>\n" +
                "        <div class=\"email-footer\">\n" +
                "            <p>&copy; " + LocalDate.now().getYear() + " Ushuru Smart. All rights reserved.</p>\n" +
                "            <p><a href=\"https://www.yourcompany.com/support\">Support</a></p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }
}
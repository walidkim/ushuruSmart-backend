package com.emtech.ushurusmart.usermanagement.utils.otp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.emtech.ushurusmart.config.otpconfig;
import com.twilio.Twilio;
import com.twilio.exception.TwilioException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class OTPService {

    private final otpRepository otpRepository;

    public OTPService(otpRepository otpRepository) {
        this.otpRepository = otpRepository;
    }

    public otpEntity otpEntity = new otpEntity();

    public String otpCode;
    public String usertag;
    public LocalDateTime createdat;

    public otpEntity saveOtp(String usertag, String otpcode) {
        otpEntity otpEntity = new otpEntity();
        otpEntity.setUsertag(usertag);
        otpEntity.setOtpcode(otpCode);
        otpEntity.setCreatedAt(LocalDateTime.now());
        return otpRepository.save(otpEntity);
    }

    public String generateOTP() {

        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            otp.append(ThreadLocalRandom.current().nextInt(0, 10));
        }
        System.out.println("Generated OTP: " + otp.toString());
        return otp.toString();
    }

    @SuppressWarnings("null")
    public String sendOTP(String usertag, String phoneNo) {
        if (usertag.equals(null) || phoneNo.equals(null)) {
            System.out.println("User tag or Phone number is missing");
            return "Enter both fields";
        } else {

            phoneNo = "+" + phoneNo;
            otpCode = generateOTP();
            System.out.println("Generated OTP: " + otpCode);
            String messageBody = "Your UshuruSmart OTP code is: " + otpCode + ".\n Do not share this with anyone.";
            otpEntity.setUsertag(usertag);
            otpEntity.setOtpcode(otpCode);
            otpEntity.setCreatedAt(createdat);
            otpRepository.save(otpEntity);

            otpRepository.save(otpEntity);
            try {
                Twilio.init(otpconfig.ACCOUNT_SID, otpconfig.AUTH_TOKEN);
                Message message = Message.creator(
                        new PhoneNumber(phoneNo),
                        new PhoneNumber(otpconfig.TWILIO_PHONE_NUMBER),
                        messageBody)
                        .create();

                System.out.println("Message SID: " + message.getSid());
                return "OTP sent successfully!";

            } catch (TwilioException e) {
                System.out.println(e);
                return "Failed to send OTP!";
            }
        }

    }

    public void save(String usertag, String otpCode) {
        otpEntity.setUsertag(usertag);
        otpEntity.setOtpcode(otpCode);
        otpEntity.setCreatedAt(createdat);
        otpRepository.save(otpEntity);
    }

    @SuppressWarnings("null")
    public boolean verifyOTP(String usertag, String otpcode) {
        otpEntity.setUsertag(usertag);
        otpEntity.setOtpcode(otpcode);
        List<otpEntity> otpEntities = otpRepository.findByUsertagAndOtpcode(usertag, otpcode);
        if (!otpEntities.isEmpty()) {
            try {
                // delete the used code after verification
                // otpRepository.deleteByUsertag(usertag);
                otpEntity firsOtpEntity = otpEntities.get(0);
                otpRepository.delete(firsOtpEntity);
            } catch (Exception e) {
                System.out.println("Error while deleting" + usertag + " : " + e);
            }
            return true;
        } else {
            return false;
        }
    }

    // Delete expired OTPs, Run every minute
    @Scheduled(fixedDelay = 60000)
    public void cleanUpExpiredOTPs() {
        LocalDateTime expiryThreshold = LocalDateTime.now().minusMinutes(5); // 5 minutes expiry
        otpRepository.deleteByCreatedAtBefore(expiryThreshold);
    }
}

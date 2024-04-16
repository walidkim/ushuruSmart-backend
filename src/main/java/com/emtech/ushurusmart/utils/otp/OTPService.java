package com.emtech.ushurusmart.utils.otp;

import com.twilio.exception.TwilioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OTPService {
@Autowired
 private OtpRepository otpRepository;

    private void saveOtp(String usertag, String otpcode) {
        OtpEntity otpEntity = new OtpEntity();
        otpEntity.setUserTag(usertag);
        otpEntity.setOtpCode(otpcode);
        otpEntity.setValidUntil(LocalDateTime.now().plusMinutes(10) );
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
    public String sendOTP(String phoneNo) {

        if (phoneNo.isEmpty()) {
            System.out.println("User tag or Phone number is missing");
            return "Enter both fields";
        } else {

            if(otpRepository.findByUserTag(phoneNo)!=null){
                 otpRepository.deleteByUserTag(phoneNo);
            }
            String otpCode = generateOTP();

            String messageBody = "Your UshuruSmart OTP code is: " + otpCode + ".\n Do not share this with anyone.";

            try {
//                Twilio.init(otpconfig.ACCOUNT_SID, otpconfig.AUTH_TOKEN);
//                Message message = Message.creator(
//                        new PhoneNumber("+" + phoneNo),
//                        new PhoneNumber(otpconfig.TWILIO_PHONE_NUMBER),
//                        messageBody)
//                        .create();
//                System.out.println(message);
                System.out.println(otpCode + " "+phoneNo);
                saveOtp(phoneNo,otpCode);
                return "OTP sent successfully!";

            } catch (TwilioException e) {
                System.out.println("twillio" + e);
                return "Failed to send OTP!";
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
                System.out.println("Error while deleting" + phoneNumber + " : " + e);
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
}

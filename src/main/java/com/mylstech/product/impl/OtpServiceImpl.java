package com.mylstech.product.impl;

import com.mylstech.product.service.EmailService;
import com.mylstech.product.service.OtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpServiceImpl implements OtpService {

    private final Map<String, Map.Entry<String, LocalDateTime>> otpStorage = new ConcurrentHashMap<> ( );
    private final EmailService emailService;
    @Value("${otp.expiry.minutes}")
    private int otpExpiryMinutes;

    @Override
    public String sendOtp(String email) {
        otpStorage.remove ( email );

        String otp = generateSecureOtp ( );

        LocalDateTime expiryTime = LocalDateTime.now ( ).plusMinutes ( otpExpiryMinutes );

        // Store OTP with expiry time
        otpStorage.put ( email, new AbstractMap.SimpleEntry<> ( otp, expiryTime ) );
        log.info ( otpStorage.toString ( ) );
        // Send OTP via email
        sendOtpViaEmail ( email, otp );

        return "OTP sent to your email " + email;
    }

    private String generateSecureOtp() {
        SecureRandom random = new SecureRandom ( );
        // Generate a 6-digit OTP (100000-999999)
        return String.valueOf ( random.nextInt ( 900000 ) + 100000 );
    }

    private void sendOtpViaEmail(String email, String otp) {
        String subject = "Your OTP for Login";
        String body = "Your OTP for login is: " + otp + ". It will expire in " + otpExpiryMinutes + " minutes.";

        emailService.sendEmail ( email, subject, body );
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
       Map.Entry<String, LocalDateTime> storedOtpEntry = otpStorage.get ( email );

        if ( storedOtpEntry == null ) {
            return false;
        }
        String storedOtp = storedOtpEntry.getKey ( );
        LocalDateTime expiryTime = storedOtpEntry.getValue ( );
        // Check if OTP is valid and not expired
        boolean isValid = storedOtp.equals ( otp ) && LocalDateTime.now ( ).isBefore ( expiryTime );
        // Log the validation result
        log.info ( "OTP validation result: {}", isValid );
        // Remove OTP after successful verification
        if ( isValid ) {
            otpStorage.remove ( email );
            log.info ( "OTP removed from storage after successful verification" );
        }
        return isValid;
    }

}

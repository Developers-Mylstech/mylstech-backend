package com.mylstech.product.service;

public interface OtpService {
     String sendOtp(String email);

     boolean verifyOtp(String email, String otp);
}

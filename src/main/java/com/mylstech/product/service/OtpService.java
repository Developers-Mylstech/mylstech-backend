package com.mylstech.product.service;

public interface OtpService {
    public String sendOtp(String email);

    public boolean verifyOtp(String email, String otp);
}

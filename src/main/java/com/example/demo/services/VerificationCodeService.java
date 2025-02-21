package com.example.demo.services;

import com.example.demo.entities.Account;
import com.example.demo.entities.VerificationCode;
import jakarta.mail.MessagingException;

public interface VerificationCodeService {
    VerificationCode createVerificationCode(String email) throws MessagingException;

    Account verifyCode(String code);
}

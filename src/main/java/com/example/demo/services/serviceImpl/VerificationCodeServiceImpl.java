package com.example.demo.services.serviceImpl;

import com.example.demo.entities.Account;
import com.example.demo.entities.VerificationCode;
import com.example.demo.exceptions.ShopApiException;
import com.example.demo.repositories.AccountRepository;
import com.example.demo.repositories.VerificationRepository;
import com.example.demo.services.VerificationCodeService;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {
    private final VerificationRepository verificationRepository;

    private final AccountRepository accountRepository;

//    private final EmailService emailService;

    public VerificationCodeServiceImpl(VerificationRepository verificationRepository, AccountRepository accountRepository) {
        this.verificationRepository = verificationRepository;
        this.accountRepository = accountRepository;
//        this.emailService = emailService;
    }

    @Override
    public VerificationCode createVerificationCode(String email) {
        // Tạo mã xác nhận ngẫu nhiên
//        String verificationCodeValue = generateRandomCode();

        // Thiết lập thời gian hết hạn cho mã xác nhận
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);

        Account account = accountRepository.findByEmail(email);
        if (account == null) {
            throw new ShopApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy tài khoản có địa chỉ email của bạn");
        }

        // Tạo đối tượng VerificationCode và lưu vào cơ sở dữ liệu
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setAccount(account);
//        verificationCode.setCode(verificationCodeValue);
        verificationCode.setExpiryTime(expiryTime);

        // Tính thời gian còn lại bằng giây (hoặc phút, nếu cần)
        long secondsRemaining = expiryTime.atZone(java.time.ZoneId.systemDefault())
                .toEpochSecond() - LocalDateTime.now()
                .atZone(java.time.ZoneId.systemDefault())
                .toEpochSecond();

        long minutesRemaining = secondsRemaining / 60;
        long secondsOnly = secondsRemaining % 60;

        // Tạo nội dung email
        StringBuilder str = new StringBuilder();
        str.append("Mã xác nhận của bạn là: ");
//        str.append("<b>").append(verificationCodeValue).append("</b><br><br>");
        str.append("Lưu ý: Mã xác nhận này chỉ có hiệu lực trong vòng ");
        str.append(minutesRemaining).append(" phút ");
        str.append(secondsOnly).append(" giây.<br>");
        str.append("Nếu mã hết hạn, vui lòng yêu cầu mã mới.");

        String subject = "Xác nhận đặt lại mật khẩu";
//        try {
////            emailService.sendEmail(account.getEmail(), subject, str.toString());
//        } catch (MessagingException e) {
//            System.out.println("Lỗi không gửi được email");
//            e.printStackTrace();
//        }

        return verificationRepository.save(verificationCode);
    }


    @Override
    public Account verifyCode(String code) {
        // Tìm mã xác nhận trong cơ sở dữ liệu
//        VerificationCode verificationCode = verificationRepository.findByCode(code);

//        if (verificationCode != null && isValid(verificationCode)) {
//            // Mã xác nhận hợp lệ, trả về người dùng liên kết
//            return verificationCode.getAccount();
//        }

        // Mã xác nhận không hợp lệ hoặc đã hết hạn
        return null;
    }

    private boolean isValid(VerificationCode verificationCode) {
        // Kiểm tra xem mã xác nhận có hợp lệ và chưa hết hạn không
        LocalDateTime now = LocalDateTime.now();
        return verificationCode.getExpiryTime().isAfter(now);
    }

//    private String generateRandomCode() {
//        // Logic để tạo mã xác nhận ngẫu nhiên (ví dụ: sử dụng thư viện RandomStringUtils)
//        return RandomUtils.randomAlphanumeric(6);
//    }
}

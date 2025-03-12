package com.example.demo.configs;

import com.example.demo.entities.DiscountCode;
import com.example.demo.repositories.DiscountCodeRepository;
import com.example.demo.services.DiscountCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ScheduledApp {
    @Autowired
    private DiscountCodeService discountCodeService;

    @Autowired
    private DiscountCodeRepository discountCodeRepository;

    @Scheduled(fixedRate = 60*60*100) // Run every 24 hours, adjust as needed
    public void checkAndSetExpiredStatus() {
        Date currentDate = new Date();
        List<DiscountCode> expiredDiscountCodes = null;
        if(!(expiredDiscountCodes ==null)) {
            for (DiscountCode discountCode : expiredDiscountCodes) {
                if (currentDate.after(discountCode.getEndDate())) {
                    discountCode.setStatus(3);
                    discountCodeRepository.save(discountCode);
                }
            }
        }

    }
}

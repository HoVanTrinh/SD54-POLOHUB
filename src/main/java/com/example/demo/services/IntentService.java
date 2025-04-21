package com.example.demo.services;

import org.springframework.stereotype.Service;

@Service
public class IntentService {
    public String detectIntent(String message) {
        message = message.toLowerCase();

        if (message.contains("giá") || message.contains("bao nhiêu") || message.contains("cost") || message.contains("price")) {
            return "CHECK_PRICE";
        }
        if (message.contains("size") || message.contains("kích cỡ") || message.contains("bảng size") || message.contains("size chart")) {
            return "CHECK_SIZE";
        }
        if (message.contains("đơn hàng") || message.contains("trạng thái đơn hàng") || message.contains("order status") || message.contains("shipping")) {
            return "ORDER_STATUS";
        }
        if (message.contains("mẫu nào") || message.contains("loại áo") || message.contains("models") || message.contains("collections")) {
            return "PRODUCT_LINE";
        }
        if (message.contains("khuyến mãi") || message.contains("giảm giá") || message.contains("promo") || message.contains("sale")) {
            return "PROMOTIONS";
        }
        if (message.contains("chính sách đổi trả") || message.contains("đổi trả") || message.contains("return policy")) {
            return "RETURN_POLICY";
        }
        if (message.contains("hỗ trợ") || message.contains("liên hệ") || message.contains("contact") || message.contains("help")) {
            return "SUPPORT";
        }
        if (message.contains("giờ mở cửa") || message.contains("thời gian làm việc") || message.contains("working hours")) {
            return "WORKING_HOURS";
        }
        if (message.contains("vận chuyển") || message.contains("shipping fee") || message.contains("phí vận chuyển")) {
            return "SHIPPING_INFO";
        }
        // Intent để hỏi về địa chỉ cửa hàng
        if (message.contains("địa chỉ") || message.contains("shop location") || message.contains("cửa hàng")) {
            return "STORE_LOCATION";
        }
        return "UNKNOWN";
    }
}


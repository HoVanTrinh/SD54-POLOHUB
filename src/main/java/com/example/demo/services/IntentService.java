
package com.example.demo.services;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;
import java.util.logging.Logger;
@Service
public class IntentService {
    private static final Logger logger = Logger.getLogger(IntentService.class.getName());

    public String detectIntent(String message) {
        message = message.toLowerCase();

        // Regex pattern cho mỗi intent
        if (Pattern.compile(".*(giá|bao nhiêu|cost|price).*").matcher(message).matches()) {
            return "CHECK_PRICE";
        }
        if (Pattern.compile(".*(size|kích cỡ|bảng size|size chart).*").matcher(message).matches()) {
            return "CHECK_SIZE";
        }
        if (Pattern.compile(".*(đơn hàng|trạng thái đơn hàng|order status|shipping).*").matcher(message).matches()) {
            return "ORDER_STATUS";
        }
        if (Pattern.compile(".*(mẫu nào|loại áo|models|collections).*").matcher(message).matches()) {
            return "PRODUCT_LINE";
        }
        if (Pattern.compile(".*(khuyến mãi|giảm giá|promo|sale).*").matcher(message).matches()) {
            return "PROMOTIONS";
        }
        if (Pattern.compile(".*(chính sách đổi trả|đổi trả|return policy).*").matcher(message).matches()) {
            return "RETURN_POLICY";
        }
        if (Pattern.compile(".*(hỗ trợ|liên hệ|contact|help).*").matcher(message).matches()) {
            return "SUPPORT";
        }
        if (Pattern.compile(".*(giờ mở cửa|thời gian làm việc|working hours).*").matcher(message).matches()) {
            return "WORKING_HOURS";
        }
        if (Pattern.compile(".*(vận chuyển|shipping fee|phí vận chuyển).*").matcher(message).matches()) {
            return "SHIPPING_INFO";
        }
        if (Pattern.compile(".*(địa chỉ|shop location|cửa hàng).*").matcher(message).matches()) {
            return "STORE_LOCATION";
        }
        if (message.contains("phản hồi") || message.contains("feedback") || message.contains("review")) {
            return "FEEDBACK";
        }
        if (message.contains("còn hàng") || message.contains("availability") || message.contains("stock")) {
            return "PRODUCT_AVAILABILITY";
        }
        if (message.contains("câu hỏi thường gặp") || message.contains("faq") || message.contains("help")) {
            return "FAQ";
        }
        if (message.contains("giỏ hàng") || message.contains("cart") || message.contains("thêm vào giỏ hàng")) {
            return "CART";
        }

        String intent = "UNKNOWN";

        if (message.contains("giá") || message.contains("bao nhiêu") || message.contains("cost") || message.contains("price")) {
            intent = "CHECK_PRICE";
        }
        if (Pattern.compile(".*(chất liệu|fabric|material|vải).*").matcher(message).matches()) {
            return "PRODUCT_MATERIAL";
        }
        if (Pattern.compile(".*(hướng dẫn mua hàng|cách đặt hàng|order guide|how to buy).*").matcher(message).matches()) {
            return "ORDER_GUIDE";
        }
        if (Pattern.compile(".*(thanh toán|payment|phương thức thanh toán).*").matcher(message).matches()) {
            return "PAYMENT_METHOD";
        }
        if (Pattern.compile(".*(chăm sóc áo|bảo quản|giặt như thế nào|washing).*").matcher(message).matches()) {
            return "CARE_INSTRUCTIONS";
        }
        if (Pattern.compile(".*(cộng tác viên|ctv|đối tác|hợp tác).*").matcher(message).matches()) {
            return "COLLABORATION";
        }
        if (Pattern.compile(".*(đánh giá|review|feedback).*").matcher(message).matches()) {
            return "REVIEW";
        }
        if (Pattern.compile(".*(ưu đãi thành viên|member|membership).*").matcher(message).matches()) {
            return "MEMBER_BENEFIT";
        }
        if (Pattern.compile(".*(quà tặng|gift|promotion code|mã khuyến mãi).*").matcher(message).matches()) {
            return "GIFT_PROMO";
        }
        if (Pattern.compile(".*(chất lượng|quality|đảm bảo).*").matcher(message).matches()) {
            return "QUALITY_GUARANTEE";
        }
        if (Pattern.compile(".*(đổi size|đổi hàng|thay đổi).*").matcher(message).matches()) {
            return "EXCHANGE_POLICY";
        }
        if (Pattern.compile(".*(đặt hàng hộ|hỗ trợ mua hàng|trợ giúp đặt).*").matcher(message).matches()) {
            return "HELP_ORDERING";
        }
        if (Pattern.compile(".*(giao hàng nhanh|ship nhanh|giao trong ngày).*").matcher(message).matches()) {
            return "FAST_DELIVERY";
        }
        if (Pattern.compile(".*(chào|hello|hi).*").matcher(message).matches()) {
            return "GREETING";
        }
        if (Pattern.compile(".*(cảm ơn|thanks|thank).*").matcher(message).matches()) {
            return "THANKS";
        }
        if (Pattern.compile(".*(tạm biệt|goodbye|bye).*").matcher(message).matches()) {
            return "GOODBYE";
        }
        if(Pattern.compile(".*(vũ|vu).*").matcher(message).matches()){
            return "VU";
        }
        // ... tiếp tục các điều kiện
        if (intent.equals("UNKNOWN")) {
            logger.warning("Không xác định intent: " + message);
        } else {
            logger.info("Nhận diện intent: " + intent);
        }
        return intent;
    }
}


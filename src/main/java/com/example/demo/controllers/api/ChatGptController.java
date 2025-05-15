//
//package com.example.demo.controllers.api;
//
//import com.example.demo.services.ChatGptService;
//import com.example.demo.services.IntentService;
//import com.theokanning.openai.completion.chat.ChatMessage;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/chatgpt")
//public class ChatGptController {
//    private final ChatGptService chatGptService;
//    @Autowired
//    private final IntentService intentService;
//    // Có thể giữ history trong session hoặc gửi kèm từ client
//
//    public ChatGptController(ChatGptService chatGptService, IntentService intentService) {
//        this.chatGptService = chatGptService;
//        this.intentService = intentService;
//    }
//
//    @PostMapping("/message")
//    public ResponseEntity<?> receive(@RequestBody Map<String, Object> payload) {
//        try {
//            List<Map<String,String>> messages = (List<Map<String,String>>) payload.get("messages");
//
//            List<ChatMessage> chatMessages = messages.stream()
//                    .map(map -> {
//                        String role = map.get("role");
//                        String content = map.get("content");
//                        return new ChatMessage(role, content);
//                    })
//                    .toList();
//
//            // Lấy message cuối để detect intent
//            String lastUserMessage = chatMessages.get(chatMessages.size() - 1).getContent();
//
//            String intent = intentService.detectIntent(lastUserMessage);
//
//            // Nếu intent null hoặc không match với các intent có sẵn => fallback GPT
//            if (intent == null || intent.isEmpty()) {
//                String reply = chatGptService.chat(chatMessages);
//                return ResponseEntity.ok(Map.of("reply", reply));
//            }
//
//            // Xử lý theo intent
//            switch (intent) {
//                case "CHECK_PRICE":
//                    return ResponseEntity.ok(Map.of("reply", "Áo polo nam Polohub có giá từ 300,000 đến 700,000 VNĐ tùy mẫu mã và chất liệu."));
//                case "CHECK_SIZE":
//                    return ResponseEntity.ok(Map.of("reply", "Chúng tôi có các size: S, M, L, XL. Bạn cần mình hỗ trợ chọn size không?"));
//                case "ORDER_STATUS":
//                    return ResponseEntity.ok(Map.of("reply", "Bạn vui lòng cung cấp mã đơn hàng để mình kiểm tra trạng thái giúp bạn nhé."));
//                case "PRODUCT_LINE":
//                    return ResponseEntity.ok(Map.of("reply", "Polohub có nhiều mẫu áo polo nam: classic, sporty, slim fit... Bạn thích mẫu nào?"));
//                case "PROMOTIONS":
//                    return ResponseEntity.ok(Map.of("reply", "Hiện tại chúng tôi đang có chương trình giảm giá 10% cho khách hàng mới."));
//                case "RETURN_POLICY":
//                    return ResponseEntity.ok(Map.of("reply", "Bạn có thể đổi trả sản phẩm trong vòng 7 ngày nếu còn nguyên tem và chưa qua sử dụng."));
//                case "SUPPORT":
//                    return ResponseEntity.ok(Map.of("reply", "Bạn cần hỗ trợ gì? Vui lòng liên hệ hotline: 0123 456 789 hoặc email support@polohub.vn"));
//                case "WORKING_HOURS":
//                    return ResponseEntity.ok(Map.of("reply", "Cửa hàng mở cửa từ 8h30 đến 20h00 tất cả các ngày trong tuần."));
//                case "SHIPPING_INFO":
//                    return ResponseEntity.ok(Map.of("reply", "Chúng tôi giao hàng toàn quốc, phí vận chuyển từ 30,000 VNĐ cho đơn hàng dưới 500,000 VNĐ."));
//                case "STORE_LOCATION":
//                    return ResponseEntity.ok(Map.of("reply", "Cửa hàng Polohub hiện tại có 3 chi nhánh tại Hà Nội, Đà Nẵng và TP. Hồ Chí Minh."));
//                case "GREETING":
//                    return ResponseEntity.ok(Map.of("reply", "Chào bạn! 👋 Mình có thể giúp gì cho bạn hôm nay?"));
//                case "THANKS":
//                    return ResponseEntity.ok(Map.of("reply", "Rất vui được hỗ trợ bạn 😊 Nếu cần gì thêm cứ nhắn nhé!"));
//                case "GOODBYE":
//                    return ResponseEntity.ok(Map.of("reply", "Tạm biệt bạn 👋 Hẹn gặp lại lần sau nhé!"));
//                case "PRODUCT_MATERIAL":
//                    return ResponseEntity.ok(Map.of("reply", "Áo Polo Polohub được làm từ chất liệu vải cotton mềm mại, thoáng mát và bền bỉ."));
//                case "ORDER_GUIDE":
//                    return ResponseEntity.ok(Map.of("reply", "Để đặt hàng, bạn chỉ cần chọn sản phẩm yêu thích và làm theo các bước thanh toán trên website."));
//                case "PAYMENT_METHOD":
//                    return ResponseEntity.ok(Map.of("reply", "Chúng tôi hỗ trợ thanh toán qua thẻ tín dụng, chuyển khoản ngân hàng và ví điện tử."));
//                case "CARE_INSTRUCTIONS":
//                    return ResponseEntity.ok(Map.of("reply", "Để bảo quản áo, bạn nên giặt tay và phơi ở nơi thoáng mát, tránh phơi dưới ánh nắng trực tiếp."));
//                case "COLLABORATION":
//                    return ResponseEntity.ok(Map.of("reply", "Chúng tôi rất vui khi được hợp tác với các đối tác. Vui lòng gửi email tới support@polohub.vn để thảo luận thêm."));
//                case "REVIEW":
//                    return ResponseEntity.ok(Map.of("reply", "Bạn có thể để lại đánh giá sau khi trải nghiệm sản phẩm của chúng tôi nhé!"));
//                case "MEMBER_BENEFIT":
//                    return ResponseEntity.ok(Map.of("reply", "Thành viên của Polohub sẽ nhận được ưu đãi đặc biệt, bao gồm giảm giá và quà tặng hàng tháng."));
//                case "GIFT_PROMO":
//                    return ResponseEntity.ok(Map.of("reply", "Chúng tôi đang có chương trình tặng quà cho khách hàng mua hàng trong dịp lễ này. Hãy tham gia ngay nhé!"));
//                case "QUALITY_GUARANTEE":
//                    return ResponseEntity.ok(Map.of("reply", "Chúng tôi cam kết sản phẩm Polohub đạt chất lượng cao và đảm bảo sự hài lòng của khách hàng."));
//                case "EXCHANGE_POLICY":
//                    return ResponseEntity.ok(Map.of("reply", "Nếu sản phẩm không đúng size hoặc lỗi, bạn có thể đổi hàng trong vòng 7 ngày, miễn phí vận chuyển."));
//                case "HELP_ORDERING":
//                    return ResponseEntity.ok(Map.of("reply", "Bạn cần giúp đỡ trong việc đặt hàng? Để mình hướng dẫn nhé."));
//                case "FAST_DELIVERY":
//                    return ResponseEntity.ok(Map.of("reply", "Chúng tôi cung cấp dịch vụ giao hàng nhanh trong vòng 1-2 ngày làm việc với chi phí hợp lý."));
//                case "VU":
//                    return ResponseEntity.ok(Map.of("reply", "Vũ ngu như con chó"));
//                default:
//                    String reply = chatGptService.chat(chatMessages);
//                    return ResponseEntity.ok(Map.of("reply", "Xin lỗi, tôi không thể hiểu được câu hỏi của bạn. Bạn có thể thử lại không?"));
//                // Gọi ChatGPT service chỉ khi cần thiết
//            }
//
//        }
//        catch (Exception e) {
//            return ResponseEntity.status(500).body(Map.of("error", "Đã xảy ra lỗi trong quá trình xử lý", "details", e.getMessage()));
//        }
//    }
//    public void logIntentUsage(String intent) {
//        // Lưu intent vào cơ sở dữ liệu hoặc file log
//        System.out.println("Intent đã được sử dụng: " + intent);
//    }
//}

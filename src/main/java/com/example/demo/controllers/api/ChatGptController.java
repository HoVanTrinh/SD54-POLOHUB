package com.example.demo.controllers.api;

import com.example.demo.services.ChatGptService;
import com.example.demo.services.IntentService;
import com.theokanning.openai.completion.chat.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chatgpt")
public class ChatGptController {
    private final ChatGptService chatGptService;
    @Autowired
    private final IntentService intentService;
    // Có thể giữ history trong session hoặc gửi kèm từ client

    public ChatGptController(ChatGptService chatGptService, IntentService intentService) {
        this.chatGptService = chatGptService;
        this.intentService = intentService;
    }

    @PostMapping("/message")
    public ResponseEntity<?> receive(@RequestBody Map<String, Object> payload) {
        List<Map<String,String>> messages = (List<Map<String,String>>) payload.get("messages");

        List<ChatMessage> chatMessages = messages.stream()
                .map(map -> {
                    String role = map.get("role");
                    String content = map.get("content");
                    return new ChatMessage(role, content);
                })
                .toList();

        // Lấy message cuối để detect intent
        String lastUserMessage = chatMessages.get(chatMessages.size() - 1).getContent();

        String intent = intentService.detectIntent(lastUserMessage);

        // Xử lý theo intent
        switch (intent) {
            case "CHECK_PRICE":
                return ResponseEntity.ok(Map.of("reply", "Áo polo nam Polohub có giá từ 300,000 đến 700,000 VNĐ tùy mẫu mã và chất liệu."));
            case "CHECK_SIZE":
                return ResponseEntity.ok(Map.of("reply", "Chúng tôi có các size: S, M, L, XL. Bạn cần mình hỗ trợ chọn size không?"));
            case "ORDER_STATUS":
                return ResponseEntity.ok(Map.of("reply", "Bạn vui lòng cung cấp mã đơn hàng để mình kiểm tra trạng thái giúp bạn nhé."));
            case "PRODUCT_LINE":
                return ResponseEntity.ok(Map.of("reply", "Polohub có nhiều mẫu áo polo nam: classic, sporty, slim fit... Bạn thích mẫu nào?"));
            case "PROMOTIONS":
                return ResponseEntity.ok(Map.of("reply", "Hiện tại chúng tôi đang có chương trình giảm giá 10% cho khách hàng mới."));
            case "RETURN_POLICY":
                return ResponseEntity.ok(Map.of("reply", "Bạn có thể đổi trả sản phẩm trong vòng 7 ngày nếu còn nguyên tem và chưa qua sử dụng."));
            case "SUPPORT":
                return ResponseEntity.ok(Map.of("reply", "Bạn cần hỗ trợ gì? Vui lòng liên hệ hotline: 0123 456 789 hoặc email support@polohub.vn"));
            case "WORKING_HOURS":
                return ResponseEntity.ok(Map.of("reply", "Cửa hàng mở cửa từ 8h30 đến 20h00 tất cả các ngày trong tuần."));
            case "SHIPPING_INFO":
                return ResponseEntity.ok(Map.of("reply", "Chúng tôi giao hàng toàn quốc, phí vận chuyển từ 30,000 VNĐ cho đơn hàng dưới 500,000 VNĐ."));
            case "STORE_LOCATION":
                return ResponseEntity.ok(Map.of("reply", "Cửa hàng Polohub hiện tại có 3 chi nhánh tại Hà Nội, Đà Nẵng và TP. Hồ Chí Minh."));
            default:
                String reply = chatGptService.chat(chatMessages);
                return ResponseEntity.ok(Map.of("reply", reply));
        }
    }
}

package com.example.demo.configs;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Đăng ký thư mục "uploads" để có thể truy cập qua HTTP
        exposeDirectory("uploads", registry);

        // Đăng ký thư mục "upload-barcode" để có thể truy cập qua HTTP
        exposeDirectory("image-barcode", registry);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Cấu hình Cross-Origin Resource Sharing (CORS)
        registry.addMapping("/**") // Cho phép CORS cho tất cả các endpoint
                .allowedOriginPatterns("*") // Cho phép yêu cầu từ bất kỳ nguồn nào
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Xác định các phương thức HTTP được phép
                .allowedHeaders("*") // Cho phép tất cả các headers trong yêu cầu
                .allowCredentials(true); // Cho phép gửi thông tin xác thực (như cookies) trong các yêu cầu CORS
    }

    private void exposeDirectory(String pathPattern, ResourceHandlerRegistry registry) {
        // Tạo một đối tượng Path đại diện cho thư mục sẽ được cung cấp
        Path path = Paths.get(pathPattern);

        // Lấy đường dẫn tuyệt đối của thư mục trên hệ thống tệp
        String absolutePath = path.toFile().getAbsolutePath();

        // Tạo đường dẫn logic để loại bỏ các phần "../" để đảm bảo an toàn
        String logicalPath = pathPattern.replace("../", "") + "/**";

        // Đăng ký đường dẫn logic với bộ quản lý tài nguyên, gán nó với vị trí thực tế trên hệ thống tệp
        registry.addResourceHandler(logicalPath).addResourceLocations("file:/" + absolutePath + "/");
    }

}

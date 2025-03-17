package com.example.demo.controllers.user;

import com.example.demo.dto.Cart.CartDto;
import com.example.demo.entities.Bill;
import com.example.demo.services.BillService;
import com.example.demo.services.CartService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class OrderStatusController {
    private final BillService billService;
    private final CartService cartService;

    public OrderStatusController(BillService billService, CartService cartService) {
        this.billService = billService;
        this.cartService = cartService;
    }

    @GetMapping("/cart-status")
    public String viewCartStatus(Model model,
                                 @RequestParam(required = false) String status,
                                 @PageableDefault(size = 5, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Bill> billPage = null;
        if(status == null || status.trim().isEmpty()) {
            billPage = billService.getBillByAccount(pageable);
        }else {
            billPage = billService.getBillByStatus(status, pageable);
            model.addAttribute("status", status);
        }

        model.addAttribute("bills", billPage);
        return "user/cart-status";
    }

    @PostMapping("/cancel-bill/{id}")
    public String cancelBill(@PathVariable Long id) {
        billService.updateStatus("HUY", id);
        return "redirect:/cart-status";
    }

    @ResponseBody
    @GetMapping("/api/getAllCart")
    public List<CartDto> getAllCart() {
        return cartService.getAllCartByAccountId();
    }
}

package com.example.demo.controllers.user;

import com.example.demo.dto.Account.AccountDto;
import com.example.demo.dto.AddressShipping.AddressShippingDto;
import com.example.demo.dto.Cart.CartDto;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.exceptions.ShopApiException;
import com.example.demo.services.AccountService;
import com.example.demo.services.AddressShippingService;
import com.example.demo.services.CartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ShoppingCartController {
    private final CartService cartService;
    private final AddressShippingService addressShippingService;

    @Autowired
    private AccountService accountService;

    public ShoppingCartController(CartService cartService, AddressShippingService addressShippingService) {
        this.cartService = cartService;
        this.addressShippingService = addressShippingService;
    }

    @GetMapping("/shoping-cart")
    public String viewShoppingCart(Model model) {
        List<CartDto> cartDtoList = cartService.getAllCartByAccountId();
//        Page<DiscountCodeDto> discountCodeList = discountCodeService.getAllAvailableDiscountCode(PageRequest.of(0, 15));
        List<AddressShippingDto> addressShippingDtos = addressShippingService.getAddressShippingByAccountId();
        AccountDto accountDto = accountService.getAccountLogin();
//        model.addAttribute("discountCodes", discountCodeList.getContent());
        model.addAttribute("addressShippings", addressShippingDtos);
        model.addAttribute("profile", accountDto);
        model.addAttribute("carts", cartDtoList);
        return "user/shoping-cart";
    }

    @ResponseBody
    @PostMapping("/api/addToCart")
    public void addToCart(@RequestBody CartDto cartDto) throws NotFoundException {
        cartService.addToCart(cartDto);
    }

    @ResponseBody
    @PostMapping("/api/deleteCart/{id}")
    public void deleteCart(@PathVariable Long id) {
        cartService.deleteCart(id);
    }

    @ResponseBody
    @PostMapping("/api/updateCart")
    public void updateCart(@RequestBody CartDto cartDto) throws NotFoundException {
        cartService.updateCart(cartDto);
    }

    @ResponseBody
    @RequestMapping(value = "/shoping-cart/update-profile", method = RequestMethod.POST)
    public ResponseEntity<?> updateProfileShopingCart(@RequestBody AccountDto accountDto){
        try {
            accountService.updateProfile(accountDto);
            return ResponseEntity.ok("Cập nhật thông tin thành công !");
        } catch (ShopApiException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body("Cập nhật thông tin thất bại !");
        }
    }
}

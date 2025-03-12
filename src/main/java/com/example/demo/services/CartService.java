package com.example.demo.services;

import com.example.demo.dto.Cart.CartDto;
import com.example.demo.dto.Order.OrderDto;
import com.example.demo.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {
    List<CartDto> getAllCart();
    List<CartDto> getAllCartByAccountId();
    void addToCart(CartDto cartDto) throws NotFoundException;

    void updateCart(CartDto cartDto) throws NotFoundException;

    void orderUser(OrderDto orderDto);
    OrderDto orderAdmin(OrderDto orderDto);

    void deleteCart(Long id);
}

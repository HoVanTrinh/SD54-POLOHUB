package com.example.demo.services.serviceImpl;

import com.example.demo.dto.Cart.CartDto;
import com.example.demo.dto.Cart.ProductCart;
import com.example.demo.dto.product.ProductDetailDto;
import com.example.demo.entities.*;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.exceptions.ShopApiException;
import com.example.demo.repositories.*;
import com.example.demo.services.CartService;
import com.example.demo.untils.UserLoginUtil;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ProductDiscountRepository productDiscountRepository;
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;
//    private final BillRepository billRepository;
//    private final DiscountCodeRepository discountCodeRepository;
//    private final PaymentRepository paymentRepository;
//    private final PayMentMethodRepository paymentMethodRepository;
//    private final BillDetailRepository billDetailRepository;
//    private final TempProductQuantityService tempProductQuantityService;
    private AtomicLong invoiceCounter = new AtomicLong(1);

    public CartServiceImpl(CartRepository cartRepository, ProductDiscountRepository productDiscountRepository, CustomerRepository customerRepository, AccountRepository accountRepository, ProductRepository productRepository, ProductDetailRepository productDetailRepository) {
        this.cartRepository = cartRepository;
        this.productDiscountRepository = productDiscountRepository;
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.productRepository = productRepository;
        this.productDetailRepository = productDetailRepository;
//        this.billRepository = billRepository;
//        this.discountCodeRepository = discountCodeRepository;
//        this.paymentRepository = paymentRepository;
//        this.paymentMethodRepository = paymentMethodRepository;
//        this.billDetailRepository = billDetailRepository;
//        this.tempProductQuantityService = tempProductQuantityService;
    }


    @Override
    public List<CartDto> getAllCart() {
        List<Cart> carts = cartRepository.findAll();
        List<CartDto> cartDtos = new ArrayList<>();
        carts.forEach(cart -> {
            CartDto cartDto = new CartDto();
            cartDto.setId(cart.getId());
            cartDto.setQuantity(cart.getQuantity());
            cartDto.setCreateDate(cart.getCreateDate());
        });
        return cartDtos;
    }

    @Override
    public List<CartDto> getAllCartByAccountId() {
        Account account = UserLoginUtil.getCurrentLogin();
        List<Cart> cartList = cartRepository.findAllByAccount_Id(account.getId());
        List<CartDto> cartDtos = new ArrayList<>();
        cartList.forEach(cart -> {
            Product product = productRepository.findById(cart.getProductDetail().getProduct().getId()).orElseThrow();
            ProductCart productCart = new ProductCart();
            productCart.setProductId(product.getId());
            productCart.setName(product.getName());
            productCart.setCode(product.getCode());
            productCart.setDescribe(product.getDescribe());
            productCart.setImageUrl(product.getImage().get(0).getLink());
            ProductDetailDto productDetailDto = new ProductDetailDto();
            productDetailDto.setId(cart.getProductDetail().getId());
            productDetailDto.setProductId(product.getId());
            productDetailDto.setPrice(cart.getProductDetail().getPrice());
            productDetailDto.setSize(cart.getProductDetail().getSize());
            productDetailDto.setQuantity(cart.getProductDetail().getQuantity());
            productDetailDto.setColor(cart.getProductDetail().getColor());

            ProductDiscount productDiscount = productDiscountRepository.findValidDiscountByProductDetailId(cart.getProductDetail().getId());
            if (productDiscount != null) {
                productDetailDto.setDiscountedPrice(productDiscount.getDiscountedAmount());
            }

            CartDto cartDto = new CartDto();
            cartDto.setId(cart.getId());
            cartDto.setQuantity(cart.getQuantity());
            cartDto.setCreateDate(cart.getCreateDate());
            cartDto.setAccountId(Long.parseLong("2"));
            cartDto.setProduct(productCart);
            cartDto.setDetail(productDetailDto);
            cartDtos.add(cartDto);
        });
        return cartDtos;
    }

    @Override
    public void addToCart(CartDto cartDto) throws NotFoundException {
        Cart cart = new Cart();
        Account account = UserLoginUtil.getCurrentLogin();
        cart.setAccount(account);

        ProductDetail productDetail = productDetailRepository.findById(cartDto.getDetail().getId()).orElseThrow(() -> new NotFoundException("Product not found"));

        cart.setProductDetail(productDetail);
        int quantityAdding = cartDto.getQuantity();
        int quantityRemaining = productDetail.getQuantity();

        if (cartRepository.existsByProductDetail_IdAndAccount_Id(productDetail.getId(), account.getId())) {
            Cart existsCart = cartRepository.findByProductDetail_IdAndAccount_Id(productDetail.getId(), account.getId());
            int currentQuantity = existsCart.getQuantity();
            int quantityNeedToAdd = currentQuantity + quantityAdding;

            existsCart.setQuantity(quantityNeedToAdd);
            existsCart.setUpdateDate(LocalDateTime.now());

            if (quantityRemaining == 0) {
                throw new ShopApiException(HttpStatus.BAD_REQUEST, "Sản phẩm có thuộc tính này đã hết hàng");
            }

            if (quantityRemaining < quantityNeedToAdd) {
                throw new ShopApiException(HttpStatus.BAD_REQUEST, "Số lượng thêm vào giỏ hàng lớn hơn số lượng tồn");
            }
            cartRepository.save(existsCart);
        } else {
            if (quantityRemaining < quantityAdding) {
                throw new ShopApiException(HttpStatus.BAD_REQUEST, "Số lượng thêm vào giỏ hàng lớn hơn số lượng tồn");
            }

            cart.setQuantity(quantityAdding);
            cart.setCreateDate(LocalDateTime.now());
            cart.setUpdateDate(LocalDateTime.now());
            cartRepository.save(cart);
        }

    }

    @Override
    public void updateCart(CartDto cartDto) throws NotFoundException {
        Cart cart = cartRepository.findById(cartDto.getId()).orElseThrow(() -> new NotFoundException("Cart not found"));
        int quantityAdding = cartDto.getQuantity();
        int quantityRemaining = cart.getProductDetail().getQuantity();
        if (quantityAdding > quantityRemaining) {
            throw new ShopApiException(HttpStatus.BAD_REQUEST, "Xin lỗi, số lượng sản phẩm này chỉ còn: " + quantityRemaining);
        }
        cart.setQuantity(cartDto.getQuantity());
        cartRepository.save(cart);
    }

//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public void orderUser(OrderDto orderDto) {
//        // Generate a new bill code
//        Bill billCurrent = billRepository.findTopByOrderByIdDesc();
//        int nextCode = (billCurrent == null) ? 1 : Integer.parseInt(billCurrent.getCode().substring(2)) + 1;
//        String billCode = "HD" + String.format("%03d", nextCode);
//
//        // Create a new bill
//        Bill bill = new Bill();
//        bill.setBillingAddress(orderDto.getBillingAddress());
//        bill.setCreateDate(LocalDateTime.now());
//        bill.setUpdateDate(LocalDateTime.now());
//        bill.setCode(billCode);
//        bill.setInvoiceType(InvoiceType.ONLINE);
//        bill.setPromotionPrice(orderDto.getPromotionPrice());
//        bill.setReturnStatus(false);
//
//        if (UserLoginUtil.getCurrentLogin() != null) {
//            Account account = UserLoginUtil.getCurrentLogin();
//            bill.setCustomer(account.getCustomer());
//        }
//
//        Double total = 0.0;
//        List<BillDetail> billDetailList = new ArrayList<>();
//
//        for (OrderDetailDto item : orderDto.getOrderDetailDtos()) {
//            BillDetail billDetail = new BillDetail();
//            billDetail.setBill(bill);
//            billDetail.setQuantity(item.getQuantity());
//
//            ProductDetail productDetail = productDetailRepository.findById(item.getProductDetailId())
//                    .orElseThrow(() -> new NotFoundException("Product not found"));
//
//            Product product = productRepository.findByProductDetail_Id(productDetail.getId());
//            if (product.getStatus() == 2) {
//                throw new ShopApiException(HttpStatus.BAD_REQUEST,
//                        "Sản phẩm " + productDetail.getProduct().getName() + "-" +
//                                productDetail.getSize().getName() + "-" +
//                                productDetail.getColor().getName() + " đã ngừng bán");
//            }
//
//            if (productDetail.getQuantity() - item.getQuantity() < 0) {
//                throw new ShopApiException(HttpStatus.BAD_REQUEST,
//                        "Sản phẩm " + productDetail.getProduct().getName() + "-" +
//                                productDetail.getSize().getName() + "-" +
//                                productDetail.getColor().getName() + " chỉ còn lại " + productDetail.getQuantity());
//            }
//
//            ProductDiscount productDiscount = productDiscountRepository.findValidDiscountByProductDetailId(productDetail.getId());
//            if (productDiscount != null) {
//                billDetail.setMomentPrice(productDiscount.getDiscountedAmount());
//                total += productDiscount.getDiscountedAmount() * item.getQuantity();
//            } else {
//                billDetail.setMomentPrice(productDetail.getPrice());
//                total += productDetail.getPrice() * item.getQuantity();
//            }
//
//            billDetail.setProductDetail(productDetail);
//            billDetailList.add(billDetail);
//        }
//
//        if (orderDto.getVoucherId() != null) {
//            DiscountCode discountCode = discountCodeRepository.findById(orderDto.getVoucherId())
//                    .orElseThrow(() -> new ShopApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy voucher"));
//            if (discountCode.getMaximumUsage() == 0) {
//                throw new ShopApiException(HttpStatus.BAD_REQUEST, "Mã giảm giá đã hết");
//            }
//            discountCode.setMaximumUsage(discountCode.getMaximumUsage() - 1);
//            discountCodeRepository.save(discountCode);
//            bill.setDiscountCode(discountCode);
//        }
//
//        bill.setAmount(total);
//        bill.setBillDetail(billDetailList);
//
//        PaymentMethod paymentMethod = paymentMethodRepository.findById(orderDto.getPaymentMethodId())
//                .orElseThrow(() -> new NotFoundException("Payment method not found"));
//        bill.setPaymentMethod(paymentMethod);
//
//        if (paymentMethod.getName() == PaymentMethodName.TIEN_MAT) {
//            bill.setStatus(BillStatus.CHO_XAC_NHAN);
//        } else if (paymentMethod.getName() == PaymentMethodName.CHUYEN_KHOAN) {
//            deductProductQuantities(billDetailList);
//            bill.setStatus(BillStatus.CHO_LAY_HANG);
//        }
//
//        Bill billNew = billRepository.save(bill);
//
//        if (paymentMethod.getName() == PaymentMethodName.TIEN_MAT) {
//            Payment payment = new Payment();
//            payment.setPaymentDate(LocalDateTime.now());
//            payment.setOrderStatus("1");
//            payment.setBill(billNew);
//            payment.setAmount(total.toString());
//            payment.setOrderId(RandomUtils.generateRandomOrderId(8));
//            payment.setStatusExchange(0);
//            paymentRepository.save(payment);
//        } else if (paymentMethod.getName() == PaymentMethodName.CHUYEN_KHOAN) {
//            Payment payment = paymentRepository.findByOrderId(orderDto.getOrderId());
//            payment.setBill(billNew);
//            payment.setStatusExchange(0);
//            paymentRepository.save(payment);
//        }
//
//        cartRepository.deleteAllByAccount_Id(UserLoginUtil.getCurrentLogin().getId());
//    }
//
//    private void deductProductQuantities(List<BillDetail> billDetails) {
//        for (BillDetail billDetail : billDetails) {
//            ProductDetail productDetail = billDetail.getProductDetail();
//            if (productDetail.getQuantity() - billDetail.getQuantity() < 0) {
//                throw new ShopApiException(HttpStatus.BAD_REQUEST,
//                        "Không đủ số lượng sản phẩm trong kho: " + productDetail.getProduct().getName());
//            }
//            productDetail.setQuantity(productDetail.getQuantity() - billDetail.getQuantity());
//            productDetailRepository.save(productDetail);
//        }
//    }


//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public OrderDto orderAdmin(OrderDto orderDto) {
//        try {
//            tempProductQuantityService.delete(orderDto.getCreateId());
//            System.out.println("Đã hoàn nguyên số lượng khi tạo hóa đơn");
//
//        }catch (Exception e) {
//            System.out.println("Lỗi hoàn nguyên số lượng khi tạo hóa đơn");
//        }
//        Bill billCurrent = billRepository.findTopByOrderByIdDesc();
//        int nextCode = (billCurrent == null) ? 1 : Integer.parseInt(billCurrent.getCode().substring(2)) + 1;
//        String billCode = "HD" + String.format("%03d", nextCode);
//
//        Bill bill = new Bill();
//        bill.setBillingAddress(orderDto.getBillingAddress());
//        bill.setCreateDate(LocalDateTime.now());
//        bill.setUpdateDate(LocalDateTime.now());
//        bill.setCode(billCode);
//        bill.setInvoiceType(InvoiceType.OFFLINE);
//        bill.setStatus(BillStatus.HOAN_THANH);
//        bill.setPromotionPrice(orderDto.getPromotionPrice());
//        bill.setReturnStatus(false);
//        Customer customer = null;
//        if (orderDto.getCustomer().getId() != null) {
//            customer = customerRepository.findById(orderDto.getCustomer().getId()).orElseThrow(() -> new NotFoundException("Customer not found"));
//        }
//        bill.setCustomer(customer);
//        Double total = Double.valueOf(0);
//        List<BillDetail> billDetailList = new ArrayList<>();
//        for (OrderDetailDto item :
//                orderDto.getOrderDetailDtos()) {
//            var isPromotionalProduct=false;
//            {
//                ProductDetail productDetail = productDetailRepository.findById(item.getProductDetailId()).orElseThrow(() -> new NotFoundException("Product detail not found"));
//                var promotional = productDetail.getProductDiscount();
//                if(promotional!=null){
//                    isPromotionalProduct=(!promotional.isClosed())
//                            && (promotional.getEndDate().compareTo(new Date()) >= 0)
//                            && (promotional.getStartDate().compareTo(new Date()) <= 0);
//                }
//
//            }
//
//
//            BillDetail billDetail = new BillDetail();
//            billDetail.setBill(bill);
//            billDetail.setQuantity(item.getQuantity());
//            ProductDetail productDetail = productDetailRepository.findById(item.getProductDetailId()).orElseThrow(() -> new NotFoundException("Product not found"));
//            billDetail.setProductDetail(productDetail);
//            billDetail.setIsPromotionalProduct(isPromotionalProduct);
//
//            ProductDiscount productDiscount = productDiscountRepository.findValidDiscountByProductDetailId(productDetail.getId());
//
//            if (productDetail.getQuantity() - item.getQuantity() < -1) {
//                throw new ShopApiException(HttpStatus.BAD_REQUEST, "Sản phẩm " + productDetail.getProduct().getName() + "-" + productDetail.getSize().getName() + "-" + productDetail.getColor().getName() + " chỉ còn lại " + productDetail.getQuantity());
//            }
//
//            if (productDiscount != null) {
//                billDetail.setMomentPrice(productDiscount.getDiscountedAmount());
//                total += productDiscount.getDiscountedAmount() * item.getQuantity();
//
//            } else {
//                billDetail.setMomentPrice(productDetail.getPrice());
//                total += productDetail.getPrice() * item.getQuantity();
//
//            }
//
//            int beforeQuantity = productDetail.getQuantity();
//            productDetail.setQuantity(beforeQuantity - item.getQuantity());
//            productDetailRepository.save(productDetail);
//            billDetailList.add(billDetail);
//        }
//
//        if (orderDto.getVoucherId() != null) {
//            DiscountCode discountCode = discountCodeRepository.findById(orderDto.getVoucherId()).orElseThrow(() -> new ShopApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy voucher"));
//            Integer currentQuaCode = discountCode.getMaximumUsage();
//            if (currentQuaCode == 0) {
//                throw new ShopApiException(HttpStatus.BAD_REQUEST, "Mã giảm giá đã hết");
//            }
//            discountCode.setMaximumUsage(currentQuaCode - 1);
//            discountCodeRepository.save(discountCode);
//            bill.setDiscountCode(discountCode);
//        }
//
//        bill.setAmount(total);
//        bill.setBillDetail(billDetailList);
//        PaymentMethod paymentMethod = paymentMethodRepository.findById(orderDto.getPaymentMethodId()).orElseThrow(() -> new NotFoundException("Payment not found"));
//        bill.setPaymentMethod(paymentMethod);
//
//        Bill billNew = billRepository.save(bill);
//
//        Payment payment = new Payment();
//        payment.setPaymentDate(LocalDateTime.now());
//        payment.setOrderStatus("1");
//        payment.setBill(billNew);
//        payment.setAmount(total.toString());
//        payment.setStatusExchange(0);
//        payment.setOrderId(RandomUtils.generateRandomOrderId(8));
//        paymentRepository.save(payment);
//
//        return new OrderDto(billNew.getId().toString(), orderDto.getCustomer(), billNew.getInvoiceType(), billNew.getStatus(), billNew.getPaymentMethod().getId(), billNew.getBillingAddress(), billNew.getPromotionPrice(), null, null, null,null);
//    }


    @Override
    public void deleteCart(Long id) {
        cartRepository.deleteById(id);
    }
}

package com.demo.order;

import com.demo.common.api.order.CreateOrderRequest;
import com.demo.common.api.product.ProductDTO;
import com.demo.common.api.product.ProductFacade;
import com.demo.order.entity.OrderEntity;
import com.demo.order.repository.OrderRepository;
import com.demo.order.service.OrderService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

//    @Mock
//    private ProductFacade productFacade;
//
//    @Mock
//    private OrderRepository orderRepository;
//
//    @InjectMocks
//    private OrderService orderService;
//
//    @Test
//    public void testCreateOrderSuccess() {
//        CreateOrderRequest req = new CreateOrderRequest(1L, 10L, 2);
//
//        ProductDTO pdto = new ProductDTO(10L, "P", "desc", BigDecimal.valueOf(100), 10);
//        when(productFacade.decreaseStock(10L, 2)).thenReturn(true);
//        when(productFacade.getProductById(10L)).thenReturn(pdto);
//
//        OrderEntity saved = OrderEntity.builder()
//                .id(100L)
//                .userId(1L)
//                .productId(10L)
//                .quantity(2)
//                .totalAmount(BigDecimal.valueOf(200))
//                .status("CREATED")
//                .build();
//
//        when(orderRepository.save(any(OrderEntity.class))).thenReturn(saved);
//
//        var dto = orderService.createOrder(req);
//
//        assertNotNull(dto);
//        assertEquals(100L, dto.getId());
//        assertEquals(BigDecimal.valueOf(200), dto.getTotalAmount());
//        verify(productFacade, times(1)).decreaseStock(10L, 2);
//    }
//
//    @Test
//    public void testCreateOrderInsufficientStock() {
//        CreateOrderRequest req = new CreateOrderRequest(1L, 10L, 50);
//
//        when(productFacade.decreaseStock(10L, 50)).thenReturn(false);
//
//        RuntimeException ex = assertThrows(RuntimeException.class, () -> orderService.createOrder(req));
//        assertNotNull(ex.getMessage());
//        assertFalse(ex.getMessage().isBlank());
////        assertTrue(ex.getMessage().contains("Insufficient stock"));
//    }
}

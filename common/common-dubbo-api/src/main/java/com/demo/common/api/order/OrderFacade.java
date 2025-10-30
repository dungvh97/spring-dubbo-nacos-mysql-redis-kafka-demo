package com.demo.common.api.order;

import com.demo.common.api.product.ProductDTO;

/**
 * Order RPC interface (optional, if other services need to call order-service).
 * For now we'll implement locally in order-service, but include an interface for future.
 */
public interface OrderFacade {
    OrderDTO createOrder(CreateOrderRequest req);
}

package com.duckstore.store.order.service;

import java.math.BigDecimal;

import com.duckstore.store.order.model.OrderRequest;
import com.duckstore.store.order.model.OrderResponse;
import com.duckstore.warehouse.entity.Duck;
import com.duckstore.warehouse.service.WarehouseService;
import org.springframework.stereotype.Service;

import com.duckstore.store.packaging.Packaging;
import com.duckstore.store.packaging.PackagingService;
import com.duckstore.store.pricing.PriceQuote;
import com.duckstore.store.pricing.PricingContext;
import com.duckstore.store.pricing.PricingService;

/** Orchestrates warehouse lookup, packaging and pricing. Contains no business rules itself. */
@Service
public class OrderService {

    private final WarehouseService warehouseService;
    private final PackagingService packagingService;
    private final PricingService pricingService;

    public OrderService(WarehouseService warehouseService, PackagingService packaging, PricingService pricing) {
        this.warehouseService = warehouseService;
        this.packagingService = packaging;
        this.pricingService = pricing;
    }

    public OrderResponse quote(OrderRequest request) {
        Duck duck = warehouseService.findCheapestAvailable(request.color(), request.size(), request.quantity());
        Packaging pack = packagingService.pack(request.size(), request.shippingMode());
        BigDecimal base = duck.getPrice().multiply(BigDecimal.valueOf(request.quantity()));
        PriceQuote quote = pricingService.quote(new PricingContext(
                base, request.quantity(), pack.type(), request.country(), request.shippingMode()));
        return new OrderResponse(pack.type(), pack.protections(), duck.getPrice(), quote.total(), quote.breakdown());
    }
}

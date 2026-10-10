package com.duckstore.store.packaging;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.duckstore.store.order.enums.ShippingMode;
import com.duckstore.warehouse.enums.DuckSize;
import org.springframework.stereotype.Service;


@Service
public class PackagingService {

    private final PackageTypeResolver resolver;
    private final Map<ShippingMode, ProtectionStrategy> strategies;

    public PackagingService(PackageTypeResolver resolver, List<ProtectionStrategy> strategies) {
        this.resolver = resolver;
        this.strategies = strategies.stream()
                .collect(Collectors.toMap(ProtectionStrategy::mode, Function.identity()));
    }

    public Packaging pack(DuckSize size, ShippingMode mode) {
        PackageType type = resolver.resolve(size);
        return new Packaging(type, strategies.get(mode).getProtections(type));
    }
}

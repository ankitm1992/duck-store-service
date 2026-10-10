package com.duckstore.store.packaging;

import java.util.Map;

import com.duckstore.warehouse.enums.DuckSize;
import org.springframework.stereotype.Component;


/** Rules 1-3: package depends on duck size only. */
@Component
public class PackageTypeResolver {

    private static final Map<DuckSize, PackageType> BY_SIZE = Map.of(
            DuckSize.XLARGE, PackageType.WOOD,
            DuckSize.LARGE, PackageType.WOOD,
            DuckSize.MEDIUM, PackageType.CARDBOARD,
            DuckSize.SMALL, PackageType.PLASTIC,
            DuckSize.XSMALL, PackageType.PLASTIC);

    public PackageType resolve(DuckSize size) {
        return BY_SIZE.get(size);
    }
}

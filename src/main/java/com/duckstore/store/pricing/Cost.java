package com.duckstore.store.pricing;

import java.math.BigDecimal;
import java.math.RoundingMode;

final class Cost {
    private Cost() {
    }

    static BigDecimal round(BigDecimal v) {
        return v.setScale(2, RoundingMode.HALF_UP);
    }

    static BigDecimal percent(BigDecimal base, int pct) {
        return round(base.multiply(BigDecimal.valueOf(pct)).divide(BigDecimal.valueOf(100)));
    }

    static BigDecimal of(long units) {
        return round(BigDecimal.valueOf(units));
    }
}
